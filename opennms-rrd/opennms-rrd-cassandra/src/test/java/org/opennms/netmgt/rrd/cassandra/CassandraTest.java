package org.opennms.netmgt.rrd.cassandra;

public class CassandraTest {

    private static final String POOL_NAME = "datacollection_pool";
    private static final String KEYSPACE = "OpenNMSDataCollectionV1";
    private static final String COLUMN_FAMILY = "datapoints";


    private static final long MILLIS_PER_HOUR = 3600L * 1000L;
    private static final long MILLIS_PER_DAY = 24L * MILLIS_PER_HOUR;

    interface Function {
        double evaluate(long timestamp);
    }

    class Const implements Function {
	double m_value;

	public Const(double value) {
		m_value = value;
	}

	public double evaluate(long timestamp) {
		return m_value;
	}
    }

    class Sin implements Function {

        long m_startTime;
        double m_offset;
        double m_amplitude;
        double m_period;
        double m_factor;

        Sin(long startTime, double offset, double amplitude, double period) {
            m_startTime = startTime;
            m_offset = offset;
            m_amplitude = amplitude;
            m_period = period;
            m_factor = 2 * Math.PI / period;
        }

        public double evaluate(long timestamp) {
            long x = timestamp - m_startTime;
            double ret = (m_amplitude * Math.sin(m_factor * x)) + m_offset;
            return ret;
        }
    }

    class Cos implements Function {

        long m_startTime;
        double m_offset;
        double m_amplitude;
        double m_period;

        double m_factor;

        Cos(long startTime, double offset, double amplitude, double period) {
            m_startTime = startTime;
            m_offset = offset;
            m_amplitude = amplitude;
            m_period = period;

            m_factor = 2 * Math.PI / period;
        }

        public double evaluate(long timestamp) {
            long x = timestamp - m_startTime;
            double ret = (m_amplitude * Math.cos(m_factor * x)) + m_offset;
            System.out.println("Cos("+ x + ") = " + ret);
            return ret;
        }
    }

    class Times implements Function {
        Function m_a;
        Function m_b;

        Times(Function a, Function b) {
            m_a = a;
            m_b = b;
        }

        public double evaluate(long timestamp) {
            return m_a.evaluate(timestamp)*m_b.evaluate(timestamp);
        }
    }

    class Counter implements Function {
        double m_prevValue;
        Function m_function;

        Counter(double initialValue, Function function) {
            m_prevValue = initialValue;
            m_function = function;
        }

        public double evaluate(long timestamp) {
            double m_diff = m_function.evaluate(timestamp);
            m_prevValue += m_diff;
            return m_prevValue;
        }

    }

    class Metric {
	long m_start;
	long m_step;
	long m_next;
	String m_name;
	String m_dsName;
	String m_type;
	Function m_valueGenerator;

	public Metric(String name, String dsName, String type, long start, long step, Function valueGenerator) {
		m_name = name;
		m_dsName = dsName;
		m_type = type;
		m_start = start;
		m_step = step;
		m_valueGenerator = valueGenerator;
		m_next = m_start;
	}

	public String getName() {
		return m_name;
	}

	public String getType() {
		return m_type;
	}

	public Datapoint getNextDatapoint() {
		Datapoint data = new Datapoint(m_name, m_dsName, m_next/1000, m_valueGenerator.evaluate(m_next));
		m_next += m_step;
		return data;
	}


    }

    private Metric bigSineCounter(String name, String dsName, long start, long step) {
	Function bigSine = new Sin(start, 15, -10, MILLIS_PER_DAY);
	return new Metric(name, dsName, "COUNTER", start, step, new Counter(0, bigSine));
    }

    private Metric bigSineGauge(String name, String dsName, long start, long step) {
	Function bigSine = new Sin(start, 15, -10, MILLIS_PER_DAY);
	return new Metric(name, dsName, "GAUGE", start, step, bigSine);
    }

    private Metric smallSineCounter(String name, String dsName, long start, long step) {
	Function smallSine = new Sin(start, 7, 5, MILLIS_PER_DAY);
	return new Metric(name, dsName, "COUNTER", start, step, new Counter(0, smallSine));
    }

    private Metric smallSineGauge(String name, String dsName, long start, long step) {
	Function smallSine = new Sin(start, 7, 5, MILLIS_PER_DAY);
	return new Metric(name, dsName, "GAUGE", start, step, smallSine);
    }

    private Metric constRateCounter(String name, String dsName, long start, long step, double rate) {
	return new Metric(name, dsName, "COUNTER", start, step, new Const(rate));
    }

    private Metric constGauge(String name, String dsName, long start, long step, double value) {
	return new Metric(name, dsName, "GAUGE", start, step, new Const(value));
    }

    class Timer {
	long m_start;
	long m_end;

	public void start() {
		m_start = System.currentTimeMillis();
	}

	public void end() {
		m_end = System.currentTimeMillis();
	}

	public String toString() {
		return (m_end - m_start)+" ms";
	}
    }


}

package org.opennms.netmgt.rrd.cassandra;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SuperColumn;
import org.jrobin.core.RrdBackendFactory;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.RrdMemoryBackendFactory;
import org.jrobin.core.Sample;
import org.jrobin.graph.RrdGraph;
import org.jrobin.graph.RrdGraphDef;
import org.junit.Ignore;
import org.junit.Test;
import org.scale7.cassandra.pelops.Bytes;
import org.scale7.cassandra.pelops.Cluster;
import org.scale7.cassandra.pelops.Mutator;
import org.scale7.cassandra.pelops.Pelops;
import org.scale7.cassandra.pelops.Selector;

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


    @Test
    //@Ignore
    public void testWriteDataToSuperColumn() throws InterruptedException {

	/*
	 * create keyspace DataCollection;
	 * use DataCollection;
	 * create column family Data with column_type='Super' with key_validation_class=UTF8Type and comparator=LongType and subcomparator=UTF8Type and default_validation_class=DoubleType;
	 */

	Cluster cluster = new Cluster("192.168.2.1", 9160);
	Pelops.addPool(POOL_NAME, cluster, KEYSPACE);

	// only let data live for 30 seconds
	Persister persister = new Persister(POOL_NAME, COLUMN_FAMILY, 86400);


	Timer writeTime = new Timer();

	writeTime.start();
	for(int f = 1; f <= 1000000; f++) {

		Metric metric = constRateCounter("latency/10.1.4."+f+"/icmp", "icmp", 5000, 1000, 1);

		for(int i = 0; i < 2; i++) {

			Datapoint datapoint = metric.getNextDatapoint();
			//System.err.println(datapoint);
				persister.persist(datapoint);
		}

	}

	persister.waitForFinish();
	writeTime.end();

	System.err.println(String.format("Time to write all of the data: %s", writeTime));

	// read back the data we just wrote
	Selector selector = Pelops.createSelector(POOL_NAME);
	SlicePredicate timestamps = Selector.newColumnsPredicate(Bytes.fromLong(1), Bytes.fromLong(333), false, Integer.MAX_VALUE);

	Timer queryTime = new Timer();
	queryTime.start();
	List<SuperColumn> datapoints = selector.getSuperColumnsFromRow(COLUMN_FAMILY, "latency/10.1.4.254/icmp", timestamps, ConsistencyLevel.ONE);
	queryTime.end();

	System.err.println("Found " + datapoints.size() + " datapoints (" + queryTime + ")");

	for(SuperColumn datapoint : datapoints) {
		System.err.print("collectTime = "	+ Bytes.fromByteArray(datapoint.getName()).toLong());
		List<Column> datasources = datapoint.getColumns();
		for(Column ds : datasources) {
			System.err.print(" " + Bytes.fromByteArray(ds.getName()).toUTF8() + " = " + Bytes.fromByteArray(ds.getValue()).toDouble());
		}
		System.err.println();
	}


	// shut down the pool
	Pelops.shutdown();


    }

    @Test
    public void testGenerateGraph() throws Exception {

//    	long now = System.currentTimeMillis();
//      long end = now/MILLIS_PER_DAY*MILLIS_PER_DAY + (MILLIS_PER_HOUR * 4);

	final int daysToWrite = 365*2;
	final int daysToGraph = 3;

        long step = 300000;
        final long end = System.currentTimeMillis()/step * step;

        final long start = end - (MILLIS_PER_DAY*daysToWrite);

        long numSamples = (end - start)/step +1;

	/*
	 * create keyspace DataCollection;
	 * use DataCollection;
	 * create column family Data with column_type='Super' with key_validation_class=UTF8Type and comparator=LongType and subcomparator=UTF8Type and default_validation_class=DoubleType;
	 */


	Cluster cluster = new Cluster("localhost", 9160);
	Pelops.addPool(POOL_NAME, cluster, KEYSPACE);

	// only let data live for 30 seconds
	Persister persister = new Persister(POOL_NAME, COLUMN_FAMILY, 30);

	Timer writeTime = new Timer();

	writeTime.start();


	int numMetrics = 10;

	Metric[] metrics = new Metric[10];


	for(int i = 0; i < metrics.length; i++) {
		if (i % 4 == 0) {
			metrics[i] = bigSineCounter("latency/10.1.1."+i+"/icmp", "icmp", start-step, step);
		} else if (i % 4 == 1) {
			metrics[i] = smallSineCounter("latency/10.1.1."+i+"/icmp", "icmp", start-step, step);
		} else if (i % 4 == 2) {
			metrics[i] = bigSineGauge("latency/10.1.1."+i+"/icmp", "icmp", start-step, step);
		} else if (i % 4 == 3) {
			metrics[i] = smallSineGauge("latency/10.1.1."+i+"/icmp", "icmp", start-step, step);
		}

	}



	for(int i = 0; i < numSamples; i++) {
		for(Metric m : metrics) {
			persister.persist(m.getNextDatapoint());
		}
	}


	persister.waitForFinish();

	writeTime.end();

	System.err.println(String.format("Time to write all of the data: %s", writeTime));


	final long graphEndMillis = end;
	final long graphStartMillis = (end - (MILLIS_PER_DAY*daysToGraph));


	for(int i = 0; i < metrics.length; i++) {
		Metric m = metrics[i];
		graphData(m.getName(), m.getType(), graphStartMillis, graphEndMillis, "/tmp/metric"+(i+1)+".png");
	}

	// shut down the pool
	Pelops.shutdown();


    }

	private void graphData(final String name, final String type, final long graphStartMillis, final long graphEndMillis, final String fileName) throws Exception, RrdException {
		apply(name, graphStartMillis/1000, graphEndMillis/1000, new ForEachDatapoint() {
		RrdMemoryBackendFactory m_factory = (RrdMemoryBackendFactory) RrdBackendFactory.getFactory("MEMORY");
		RrdDb m_rrd;
		Timer m_rrdWriteTime = new Timer();

		@Override
			public void start(String name) throws Exception {

			m_rrdWriteTime.start();
			RrdDef def = new RrdDef("/tmp/cassandra.rrd");
			def.setStartTime(1000);
			def.setStep(300);
			def.addDatasource("icmp", type, 600, Double.NaN, Double.NaN);
			def.addArchive("AVERAGE", 0.5, 1, 2016);
			def.addArchive("AVERAGE", 0.5, 12, 1488);
			def.addArchive("AVERAGE", 0.5, 288, 366);
			def.addArchive("MAX", 0.5, 288, 366);
			def.addArchive("MIN", 0.5, 288, 366);

			m_rrd = new RrdDb(def, m_factory) ;
			}

			@Override
			public void withDatapoint(String name, long timestamp, double value) throws Exception {

				Sample sample = m_rrd.createSample(timestamp);
				sample.setValue(0, value);
				sample.update();
			}

			@Override
			public void finish(String name) throws Exception {

				m_rrd.close();
				m_rrdWriteTime.end();

				System.err.println("Time to write rrd: " + m_rrdWriteTime);
				/*
				 * Day graph
				 *
				 *  rrdtool graph - \
				 *  --start 1200518100 --end 1200604500 \
				 *  --imgformat PNG  \
				 *  --font DEFAULT:7 \
				 *  --font TITLE:10 \
				 *  --title="ICMP Response Time" \
				 *  --vertical-label="Seconds" \
				 *  DEF:rt=cassandra.rrd:icmp:AVERAGE \
				 *  DEF:minRt=cassandra.rrd:icmp:MIN \
				 *  DEF:maxRt=cassandra.rrd:icmp:MAX \
				 *  LINE1:rt#0000ff:"Response Time" \
				 *  GPRINT:rt:AVERAGE:" Avg  \: %8.2lf %s" \
				 *  GPRINT:rt:MIN:"Min  \: %8.2lf %s" \
				 *  GPRINT:rt:MAX:"Max  \: %8.2lf %s\\n"
				 */
				RrdGraphDef dayGraph = new RrdGraphDef();
				dayGraph.setStartTime(graphStartMillis/1000);
				dayGraph.setEndTime(graphEndMillis/1000);
				dayGraph.setImageFormat("PNG");
				dayGraph.setTitle("ICMP Response Time :-)");
				dayGraph.setVerticalLabel(")-: Seconds (-:");
				dayGraph.datasource("rt", "/tmp/cassandra.rrd", "icmp", "AVERAGE", "MEMORY");
				dayGraph.datasource("minRt", "/tmp/cassandra.rrd", "icmp", "MIN", "MEMORY");
				dayGraph.datasource("maxRt", "/tmp/cassandra.rrd", "icmp", "MAX", "MEMORY");
				dayGraph.line("rt", Color.red, "Response Time");
				dayGraph.gprint("rt", "AVERAGE", "Avg : %8.2lf %s");
				dayGraph.gprint("rt", "MIN", "Min : %8.2lf %s");
				dayGraph.gprint("rt", "MAX", "Max : %8.2lf %s\\l");
		        dayGraph.setHeight(100);
		        dayGraph.setWidth(400);

		        Timer graphCreateTime = new Timer();
		        graphCreateTime.start();
		        RrdGraph graph = new RrdGraph(dayGraph);
		        graphCreateTime.end();

		        System.err.println("Time taken to create graph:" + graphCreateTime);

		        Timer graphWriteTime = new Timer();
		        graphWriteTime.start();
		        FileOutputStream out = new FileOutputStream(fileName);
		        out.write(graph.getRrdGraphInfo().getBytes());
		        out.close();
		        graphWriteTime.end();

		        System.err.println("Time taken to write graph: " + graphWriteTime);

		        m_factory.delete("/tmp/cassandra.rrd");


			}
		});
	}

    public void apply(String name, long startSecond, long endSecond, ForEachDatapoint processor) throws Exception {

	Selector selector = Pelops.createSelector(POOL_NAME);

	SlicePredicate timestamps = Selector.newColumnsPredicate(Bytes.fromLong(startSecond), Bytes.fromLong(endSecond), false, Integer.MAX_VALUE);

	Timer queryTimer = new Timer();
	queryTimer.start();
	List<SuperColumn> datapoints = selector.getSuperColumnsFromRow(COLUMN_FAMILY, name, timestamps, ConsistencyLevel.ONE);
	queryTimer.end();

	System.err.println(String.format("Found %d datapoints in range[%d,%d] (%s)", datapoints.size(), startSecond, endSecond, queryTimer));

	Timer processingTimer = new Timer();
	processingTimer.start();
	processor.start(name);
	for(SuperColumn datapoint : datapoints) {
		long timestamp = Bytes.fromByteArray(datapoint.getName()).toLong();
		List<Column> datasources = datapoint.getColumns();
		for(Column ds : datasources) {
			String dsName = Bytes.fromByteArray(ds.getName()).toUTF8();
				double value = Bytes.fromByteArray(ds.getValue()).toDouble();
				processor.withDatapoint(name, timestamp, value);
		}
	}
	processor.finish(name);
	processingTimer.end();

	System.err.println("Time taken to process all " + datapoints.size() + " datapoints: " + processingTimer);
    }

    interface ForEachDatapoint {
	public void start(String name) throws Exception;
	public void withDatapoint(String name, long timestamp, double value) throws Exception;
	public void finish(String name) throws Exception;
    }


	@Test
    @Ignore
	public void testWriteDataToColumn() {

		/*
		 * create keyspace DataCollection;
		 * use DataCollection;
		 * create column family Data  with key_validation_class=UTF8Type and comparator=LongType and default_validation_class=DoubleType;
		 */

		String pool = "dataCollection";
		String keyspace = "DataCollection";
		String colFamily = "Data";

		Cluster cluster = new Cluster("localhost", 9160);
		Pelops.addPool(pool, cluster, keyspace);

		// write out some data
		Mutator mutator = Pelops.createMutator(pool);

		for(int f = 1; f <= 255; f++) {

			long start = System.currentTimeMillis();
			System.err.print("Creating RRD file 10.1.1." + f + "...");

			for(int i = 0; i < 1000; i++) {

				mutator.writeColumn(
						colFamily,
						"latency/10.1.1."+f+"/icmp",
						mutator.newColumn(Bytes.fromLong(i), Bytes.fromDouble(i))
						);



			}

			mutator.execute(ConsistencyLevel.ANY);

			long end = System.currentTimeMillis();
			System.err.println((end-start) + " ms.");
		}
		//		// read back the data we just wrote
		//		Selector selector = Pelops.createSelector(pool);
		//		List<Column> columns = selector.getColumnsFromRow(colFamily, rowKey, false, ConsistencyLevel.ONE);
		//
		//		System.out.println("Name: " + Selector.getColumnStringValue(columns, "name"));
		//		System.out.println("Age: " + Selector.getColumnValue(columns, "age").toInt());

		// shut down the pool

		Pelops.shutdown();


	}

}

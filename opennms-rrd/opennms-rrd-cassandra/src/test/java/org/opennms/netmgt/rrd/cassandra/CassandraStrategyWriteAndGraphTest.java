/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.rrd.cassandra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdStrategy;
import org.opennms.test.mock.MockLogAppender;

/**
 * Unit tests for the JrobinRrdStrategy.
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class CassandraStrategyWriteAndGraphTest {

//    private static final String POOL_NAME = "datacollection_pool";
//    private static final String KEYSPACE = "DataCollection";
//    private static final String COLUMN_FAMILY = "Data";

    private RrdStrategy<CassRrdDef,CassRrd> m_strategy;

    @Before
    public void setUp() throws Exception {
        // Make sure that AWT headless mode is enabled
        System.setProperty("java.awt.headless", "true");

        MockLogAppender.setupLogging();

        m_strategy = new CassandraRrdStrategy();

        Properties props = new Properties();
        props.put(CassandraRrdStrategy.TTL_PROPERTY, "30");
//        props.put(CassandraRrdStrategy.KEYSPACE_NAME_PROPERTY, KEYSPACE);
//        props.put(CassandraRrdStrategy.DATA_COLUMN_FAMILY_NAME_PROPERTY, COLUMN_FAMILY);
//        props.put(CassandraRrdStrategy.POOL_NAME_PROPERTY, POOL_NAME);

        m_strategy.setConfigurationProperties(props);
    }

    private static final long MILLIS_PER_HOUR = 3600L * 1000L;
    private static final long MILLIS_PER_DAY = 24L * MILLIS_PER_HOUR;


    @Test
    public void testWriteDataAndGraphIt() throws Exception {




	Timer createTime = new Timer();
	createTime.start();
	RrdDataSource ds = new RrdDataSource("icmp", "GAUGE", 600, "U", "U");
	String[] rraList = new String[] {
		"RRA:AVERAGE:0.5:1:2016",
            "RRA:AVERAGE:0.5:12:1488",
            "RRA:AVERAGE:0.5:288:366",
            "RRA:MAX:0.5:288:366",
            "RRA:MIN:0.5:288:366",
	};
	CassRrdDef def = m_strategy.createDefinition("me", "/tmp", "icmp", 300, Collections.singletonList(ds), Arrays.asList(rraList));
	if (def != null) {
		m_strategy.createFile(def);
	}
	createTime.end();

	System.err.println("Time to create rrd: " + createTime);

	final int daysToWrite = 365*2;
	final int daysToGraph = 62;

        long step = 300000;
        final long end = System.currentTimeMillis()/step * step;
        //final long end = 1200000000L + (MILLIS_PER_DAY*daysToWrite);

        final long start = end - (MILLIS_PER_DAY*daysToWrite);
        final long graphEnd = end;
        final long graphStart = end - (MILLIS_PER_DAY*daysToGraph);

        long numSamples = (end - start)/step +1;

        Metric metric = bigSineCounter("/tmp/icmp" , "icmp", start-step, step);

        Timer updateTime = new Timer();

        updateTime.start();
	CassRrd rrd = m_strategy.openFile("/tmp/icmp" + m_strategy.getDefaultFileExtension());

	for(int i = 0; i < numSamples; i++) {
		Datapoint dp = metric.getNextDatapoint();
		m_strategy.updateFile(rrd, "me", dp.getTimestamp()+":"+dp.getValue());
	}

	m_strategy.closeFile(rrd);
	updateTime.end();


	System.err.println("Time to update rrd with " + numSamples + " datapoints: " + updateTime);

	Thread.sleep(10000);

	String graphCmd = "rrdtool graph - " +
				 "--start "+ graphStart/1000 + " " +
				 "--end " + graphEnd/1000 + " " +
				 "--height 300 " +
				 "--width 1200 " +
				 "--imgformat PNG  " +
				 "--font DEFAULT:7 " +
				 "--font TITLE:10 " +
				 "--title=\"Gen'd by CassandraRrdStrategy!!!\" " +
				 "--vertical-label=\"Seconds\" " +
				 "DEF:rt=icmp.jrb:icmp:AVERAGE " +
				 "DEF:minRt=icmp.jrb:icmp:MIN " +
				 "DEF:maxRt=icmp.jrb:icmp:MAX " +
				 "LINE1:rt#0000ff:\"Response Time\" " +
				 "GPRINT:rt:AVERAGE:\" Avg  \\: %8.2lf %s\" " +
				 "GPRINT:rt:MIN:\"Min  \\: %8.2lf %s\" " +
				 "GPRINT:rt:MAX:\"Max  \\: %8.2lf %s\\n\" " +
				 "";


	Timer graphCreate = new Timer();
	graphCreate.start();
	InputStream in = m_strategy.createGraph(graphCmd, new File("/tmp"));
	graphCreate.end();

	System.err.println("Time to create the graph: " + graphCreate);

	Timer graphWrite = new Timer();
	graphWrite.start();
	FileOutputStream out = new FileOutputStream("/tmp/cassandra.png");

	byte[] b = new byte[1024];

	int n = -1;
	while (-1 != (n = in.read(b))) {
		out.write(b, 0, n);
	}

	in.close();
	out.close();

	graphWrite.end();
	System.err.println("Time to write graph: " + graphWrite);



    }

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

    class Plus implements Function {
        Function m_a;
        Function m_b;

        Plus(Function a, Function b) {
            m_a = a;
            m_b = b;
        }

        public double evaluate(long timestamp) {
            return m_a.evaluate(timestamp)+m_b.evaluate(timestamp);
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
	Function yearCos = new Cos(start, 15, 20, MILLIS_PER_DAY*365);
	Function val = new Plus(bigSine, yearCos);
	return new Metric(name, dsName, "COUNTER", start, step, new Counter(0, val));
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

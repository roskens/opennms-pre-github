package org.opennms.netmgt.rrd.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.opennms.core.utils.LogUtils;

class Persister implements Runnable {
	Keyspace m_keyspace;
	String m_dpColumnFamily;
	int m_ttl;
	BlockingQueue<Datapoint> m_queue = new LinkedBlockingQueue<Datapoint>();
	int m_maxBatchSize = 100000;
	int m_naglesDelay = 0;
	Thread m_thread;
	AtomicBoolean m_finish = new AtomicBoolean(false);
	CountDownLatch m_finishLatch = new CountDownLatch(1);

	private static final StringSerializer s_ss = StringSerializer.get();

	Persister(Keyspace keyspace, String dpColumnFamily, int ttl) {
		m_keyspace = keyspace;
		m_dpColumnFamily = dpColumnFamily;
		m_ttl = ttl;
		m_thread = new Thread(this, "Cassandra-Persister");
		m_thread.setDaemon(true);
		m_thread.start();
	}

    public List<Datapoint> getDatapoints() throws InterruptedException {
        List<Datapoint> datapoints = m_maxBatchSize > 0 ? new ArrayList<Datapoint>(m_maxBatchSize) : new ArrayList<Datapoint>();

        Datapoint d = m_queue.take();
        datapoints.add(d);

        drainTo(datapoints);

        if (m_naglesDelay <= 0) {
            return datapoints;
        }

        long now = System.currentTimeMillis();
        long expirationTime = now + m_naglesDelay;
        while (hasMoreRoom(datapoints) && now < expirationTime) {
            Datapoint datapoint = m_queue.poll(expirationTime - now, TimeUnit.MILLISECONDS);

            if (datapoint != null) {
                datapoints.add(datapoint);
                drainTo(datapoints);
            }

            now = System.currentTimeMillis();
        }

        return datapoints;

    }

	public void drainTo(List<Datapoint> datapoints) {
		if (m_maxBatchSize > 0) {
			m_queue.drainTo(datapoints, m_maxBatchSize - datapoints.size());
		} else {
			m_queue.drainTo(datapoints);
		}
	}

	public boolean hasMoreRoom(List<Datapoint> datapoints) {
		return m_maxBatchSize <= 0 || datapoints.size() < m_maxBatchSize;
	}

	public void run() {
		while (true) {
		try {
			if (m_finish.get() && m_queue.peek() == null) {
				m_finishLatch.countDown();
				return;
			}
			List<Datapoint> datapoints = getDatapoints();
			if (datapoints == null) continue;

			long start = System.currentTimeMillis();

			Mutator<String> mutator = HFactory.createMutator(m_keyspace, s_ss);

			for(Datapoint datapoint : datapoints) {
				datapoint.persist(mutator, m_dpColumnFamily, m_ttl);
			}

			mutator.execute();
			long end = System.currentTimeMillis();

			LogUtils.debugf(this, "Wrote %d datapoints in %d ms.", datapoints.size(), end-start);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtils.errorf(this, e, "failure?");
		}
		}

	}

	public void persist(Datapoint datapoint) {
		m_queue.offer(datapoint);
	}

	public void waitForFinish() throws InterruptedException {
		m_finish.set(true);
		m_finishLatch.await();
	}
}

/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
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

package org.opennms.core.concurrent;

import java.util.BitSet;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

import org.slf4j.MDC;

/**
 * A factory for creating LogPreservingThread objects.
 */
public class LogPreservingThreadFactory implements ThreadFactory {

    /** The m_slot numbers. */
    private final BitSet m_slotNumbers;

    /** The m_name. */
    private final String m_name;

    /** The m_pool size. */
    private final int m_poolSize;

    /** The m_mdc. */
    private Map m_mdc = null;

    /** The m_counter. */
    private int m_counter = 0;

    /**
     * Instantiates a new log preserving thread factory.
     *
     * @param poolName
     *            the pool name
     * @param poolSize
     *            the pool size
     * @param preserveMDC
     *            the preserve mdc
     */
    public LogPreservingThreadFactory(String poolName, int poolSize, boolean preserveMDC) {
        m_name = poolName;
        m_poolSize = poolSize;
        // Make the bitset of thread numbers one larger so that we can 1-index
        // it.
        // If pool size is Integer.MAX_VALUE, then the BitSet will not be used.
        m_slotNumbers = poolSize < Integer.MAX_VALUE ? new BitSet(poolSize + 1) : new BitSet(1);
        if (preserveMDC) {
            m_mdc = MDC.getCopyOfContextMap();
        }
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    @Override
    public Thread newThread(final Runnable r) {
        if (m_poolSize == Integer.MAX_VALUE) {
            return getIncrementingThread(r);
        } else if (m_poolSize > 1) {
            return getPooledThread(r);
        } else {
            return getSingleThread(r);
        }
    }

    /**
     * Gets the copy of context map.
     *
     * @return the copy of context map
     */
    private Map getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }

    /**
     * Sets the context map.
     *
     * @param map
     *            the new context map
     */
    private void setContextMap(Map map) {
        if (map == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(map);
        }
    }

    /**
     * Gets the incrementing thread.
     *
     * @param r
     *            the r
     * @return the incrementing thread
     */
    private Thread getIncrementingThread(final Runnable r) {
        String name = String.format("%s-Thread-%d", m_name, ++m_counter);
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Map mdc = getCopyOfContextMap();
                try {
                    // Set the logging prefix if it was stored during creation
                    setContextMap(m_mdc);
                    // Run the delegate Runnable
                    r.run();
                } finally {
                    setContextMap(mdc);
                }
            }
        }, name);
    }

    /**
     * Gets the single thread.
     *
     * @param r
     *            the r
     * @return the single thread
     */
    private Thread getSingleThread(final Runnable r) {
        String name = String.format("%s-Thread", m_name);
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Map mdc = getCopyOfContextMap();
                try {
                    // Set the logging prefix if it was stored during creation
                    setContextMap(m_mdc);
                    // Run the delegate Runnable
                    r.run();
                } finally {
                    setContextMap(mdc);
                }
            }
        }, name);
    }

    /**
     * Gets the pooled thread.
     *
     * @param r
     *            the r
     * @return the pooled thread
     */
    private Thread getPooledThread(final Runnable r) {
        final int threadNumber = getOpenThreadSlot(m_slotNumbers);
        String name = String.format("%s-Thread-%d-of-%d", m_name, threadNumber, m_poolSize);
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Map mdc = getCopyOfContextMap();
                try {
                    try {
                        setContextMap(m_mdc);
                        r.run();
                    } finally {
                        setContextMap(mdc);
                    }
                } finally {
                    // And make sure the mark the thread as unused afterwards if
                    // the thread ever exits
                    synchronized (m_slotNumbers) {
                        m_slotNumbers.set(threadNumber, false);
                    }
                }
            }
        }, name);
    }

    /**
     * Gets the open thread slot.
     *
     * @param bs
     *            the bs
     * @return the open thread slot
     */
    private static int getOpenThreadSlot(BitSet bs) {
        synchronized (bs) {
            // Start at 1 so that we always return a positive integer
            for (int i = 1; i < bs.size(); i++) {
                if (bs.get(i)) {
                    continue;
                } else {
                    bs.set(i, true);
                    return i;
                }
            }
            // We should never return zero
            return 0;
        }
    }
}

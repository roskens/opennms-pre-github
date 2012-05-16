package org.opennms.netmgt.rrd.cassandra;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.opennms.core.utils.LogUtils;

public class CassRrdPool {
    private int m_maxEntries;
    private final Map<String,File> m_pool;

    public CassRrdPool() {
        this(100);
    }

    public CassRrdPool(final int size) {
        m_maxEntries = size;
        m_pool = new LinkedHashMap<String,File>(m_maxEntries, 0.75f, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String,File> eldest) {
                LogUtils.debugf(this, "removeEldestEntry(key='%s', file='%s')", eldest.getKey(), eldest.getValue().getAbsolutePath());

                return size() > m_maxEntries;
            }
        };
    }

    public synchronized void put(String key, File file) {
        m_pool.put(key, file);
    }

    public synchronized File get(String key) {
        return m_pool.get(key);
    }

    public synchronized File atomicGetAndSet(String key, File file) {
        File result = get(key);
        put(key, file);
        return result;
    }
}

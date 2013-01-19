package org.opennms.netmgt.rrd.cassandra;

import com.netflix.astyanax.Keyspace;

public class CassandraRrdConnection {
    private final Keyspace m_keyspace;
    private final String m_mdColumnFamily;
    private final String m_dpColumnFamily;
    private final int m_ttl;

    CassandraRrdConnection(final Keyspace keyspace, final String mdColumnFamily, final String dpColumnFamily, int ttl)
    {
        m_keyspace = keyspace;
        m_mdColumnFamily = mdColumnFamily;
        m_dpColumnFamily = dpColumnFamily;
        m_ttl = ttl;
    }

    public Keyspace getKeyspace() {
        return m_keyspace;
    }

    public String getMetaDataCFName() {
        return m_mdColumnFamily;
    }

    public String getDataPointCFName() {
        return m_dpColumnFamily;
    }

    public int getTTL() {
        return m_ttl;
    }
}

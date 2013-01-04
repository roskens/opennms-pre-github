package org.opennms.netmgt.rrd.cassandra;

import com.netflix.astyanax.Keyspace;

public class CassandraRrdConnection {
    private Keyspace m_keyspace;
    private String m_mdColumnFamily;
    private String m_dpColumnFamily;
    private Persister m_persister;

    CassandraRrdConnection(final Keyspace keyspace, final String mdColumnFamily, final String dpColumnFamily, int ttl)
    {
        m_keyspace = keyspace;
        m_mdColumnFamily = mdColumnFamily;
        m_dpColumnFamily = dpColumnFamily;
        m_persister = new Persister(this, ttl);
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

    public Persister getPersister() {
        return m_persister;
    }

    public void persist(Datapoint dp) {
        if(m_persister != null) {
            m_persister.persist(dp);
        }
    }
}

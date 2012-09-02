package org.opennms.netmgt.rrd.cassandra;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.opennms.core.xml.JaxbUtils;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.cassandra.config.Archive;
import org.opennms.netmgt.rrd.cassandra.config.Datasource;
import org.opennms.netmgt.rrd.cassandra.config.RrdDef;

public class CassRrd {
    private String m_fileName;
    private RrdDef m_rrddef;

    Persister m_persister;
    
    public CassRrd(CassRrdDef def) {
        m_fileName = def.getFileName();
        m_rrddef = def.getRrdDef();
    }
    public CassRrd(Keyspace keyspace, String mdColumnFamily, String fileName) throws Exception {
        this(keyspace, mdColumnFamily, fileName, null);
    }
        
    public CassRrd(Keyspace keyspace, String mdColumnFamily, String fileName, Persister persister) throws Exception {
        m_fileName = fileName;
        m_persister = persister;

        try {
            ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
            columnQuery.setColumnFamily(mdColumnFamily).setKey(fileName).setName(fileName);
            QueryResult<HColumn<String, String>> result = columnQuery.execute();

            HColumn<String, String> hc = result.get();

            if (hc == null) {
                throw new RrdException("file "+m_fileName+" does not exist!");
            }
            if (hc.getValue().isEmpty()) {
                
            }

            LogUtils.debugf(this, "metadata: found xml data for %s", m_fileName);
            LogUtils.debugf(this, "xml: %s", hc.getValue());
            m_rrddef = JaxbUtils.unmarshal(RrdDef.class, hc.getValue(), true);
        } catch (HectorException e) {
            LogUtils.errorf(this, e, "exception on search");
            throw new RrdException(e.getMessage());
        } catch (Exception e) {
            LogUtils.errorf(this, e, "exception on search");
            throw e;
        }
    }
    
    public void setPersister(Persister persister) {
        m_persister = persister;
    }

    public String getFileName() {
        return m_fileName;
    }

    public Long getStep() {
        return m_rrddef.getStep();
    }

    public List<String> getDatasourceNames() {
        List<String> dsNames = new ArrayList<String>();
        for (Datasource ds : m_rrddef.getDatasourceCollection()) {
            dsNames.add(ds.getName());
        }
        return dsNames;
    }
    
    public String getDsName(int i) {
        return m_rrddef.getDatasource(i).getName();
    }
    
    public int getDsCount() {
        return m_rrddef.getDatasourceCount();
    }

    public void close() {
    }
    
    public void storeValues(String timeAndValues) throws RrdException {
        long timestamp;

        final StringTokenizer tokenizer = new StringTokenizer(timeAndValues, ":", false);
        final int tokenCount = tokenizer.countTokens();
        if (tokenCount > getDsCount() + 1) {
            throw new RrdException("Invalid number of values specified (found " + (tokenCount - 1) + ", "
                    + getDsCount() + " allowed)");
        }
        final String timeToken = tokenizer.nextToken();
        try {
            timestamp = Long.parseLong(timeToken);
        } catch (final NumberFormatException nfe) {
            if (timeToken.equalsIgnoreCase("N") || timeToken.equalsIgnoreCase("NOW")) {
                timestamp = (System.currentTimeMillis() + 500L) / 1000L;
            } else {
                throw new RrdException("Invalid sample timestamp: " + timeToken);
            }
        }
        timestamp = normalize(timestamp, getStep());
        
        for (int i = 0; tokenizer.hasMoreTokens(); i++) {
            try {
                Double value = Double.parseDouble(tokenizer.nextToken());
                Datapoint dp = new Datapoint(getFileName(), getDsName(i), timestamp, value);

                m_persister.persist(dp);
            } catch (final NumberFormatException nfe) {
                // NOP, value is already set to NaN
            }
        }
    }

    public String toXml() {
        final StringWriter writer = new StringWriter();
        JaxbUtils.marshal(m_rrddef, writer);
        final String xml = writer.toString();

        return xml;
    }
    
    /**
     * Rounds the given timestamp to the nearest whole &quote;step&quote;. Rounded value is obtained
     * from the following expression:<p>
     * <code>timestamp - timestamp % step;</code><p>
     *
     * @param timestamp Timestamp in seconds
     * @param step    Step in seconds
     * @return "Rounded" timestamp
     */
    private static long normalize(long timestamp, long step) {
            return timestamp - timestamp % step;
    }

}

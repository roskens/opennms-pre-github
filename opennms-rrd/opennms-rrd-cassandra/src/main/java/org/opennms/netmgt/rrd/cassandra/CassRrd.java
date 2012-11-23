package org.opennms.netmgt.rrd.cassandra;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.opennms.core.xml.JaxbUtils;

import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SubSliceQuery;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.cassandra.config.Archive;
import org.opennms.netmgt.rrd.cassandra.config.Datasource;
import org.opennms.netmgt.rrd.cassandra.config.RrdDef;

public class CassRrd {
    private String m_fileName;

    private RrdDef m_rrddef;

    private CassandraRrdConnection m_connection;

    private static final StringSerializer s_ss = StringSerializer.get();

    private static final LongSerializer s_ls = LongSerializer.get();

    private static final DoubleSerializer s_ds = DoubleSerializer.get();

    public CassRrd(CassRrdDef def) {
        m_fileName = def.getFileName();
        m_rrddef = def.getRrdDef();
    }

    public CassRrd(CassandraRrdConnection connection, String fileName) throws Exception {
        m_connection = connection;
        m_fileName = fileName;

        try {
            ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(m_connection.getKeyspace());
            columnQuery.setColumnFamily(m_connection.getMetaDataCFName()).setKey(fileName).setName(fileName);
            QueryResult<HColumn<String, String>> result = columnQuery.execute();

            HColumn<String, String> hc = result.get();

            if (hc == null) {
                throw new RrdException("file " + m_fileName + " does not exist!");
            }
            if (hc.getValue().isEmpty()) {
                throw new RrdException("file " + m_fileName + " had no valid metadata?");
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

    public Datasource getDatasource(int i) {
        return m_rrddef.getDatasource(i);
    }

    public String getDsName(int i) {
        return m_rrddef.getDatasource(i).getName();
    }

    public int getDsCount() {
        return m_rrddef.getDatasourceCount();
    }

    public int getArcCount() {
        return m_rrddef.getArchiveCount();
    }

    public void close() {
    }

    public void storeValues(String timeAndValues) throws RrdException {
        long timestamp;

        final StringTokenizer tokenizer = new StringTokenizer(timeAndValues, ":", false);
        final int tokenCount = tokenizer.countTokens();
        if (tokenCount > getDsCount() + 1) {
            throw new RrdException("Invalid number of values specified (found " + (tokenCount - 1) + ", " + getDsCount()
                    + " allowed)");
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
                Datasource ds = getDatasource(i);
                Double minVal = convertToDouble(ds.getMin());
                Double maxVal = convertToDouble(ds.getMax());

                Double value = convertToDouble(tokenizer.nextToken());

                // Handle min/max
                if (!Double.isNaN(value)) {
                    if (!Double.isNaN(minVal) && value < minVal) {
                        value = Double.NaN;
                    }
                    if (!Double.isNaN(maxVal) && value > maxVal) {
                        value = Double.NaN;
                    }
                }

                Datapoint dp = new Datapoint(getFileName(), ds.getName(), timestamp, value);

                m_connection.persist(dp);
            } catch (final NumberFormatException nfe) {
                // NOP, value is already set to NaN
            }
        }
    }

    private Double convertToDouble(final String input) {
        Double val = Double.NaN;
        try {
            if (input != null) {
                val = Double.parseDouble(input);
            }
        } catch (final NumberFormatException nfe) {
            // NOP, value is already set to NaN
        }
        return val;
    }

    public String toXml() {
        final StringWriter writer = new StringWriter();
        JaxbUtils.marshal(m_rrddef, writer);
        final String xml = writer.toString();

        return xml;
    }

    /**
     * Rounds the given timestamp to the nearest whole &quote;step&quote;. Rounded value is obtained
     * from the following expression:
     * <p>
     * <code>timestamp - timestamp % step;</code>
     * <p>
     *
     * @param timestamp
     *            Timestamp in seconds
     * @param step
     *            Step in seconds
     * @return "Rounded" timestamp
     */
    private static long normalize(long timestamp, long step) {
        return timestamp - timestamp % step;
    }

    public List<TimeSeriesPoint> fetchRequest(final String ds, final String consolFun,
            final Long earliestUpdateTime, final Long latestUpdateTime) throws org.opennms.netmgt.rrd.RrdException {
        LogUtils.debugf(this, "fetchRequest(): fileName=%s, datasource=%s, consolFun=%s, begin=%d, end=%d", m_fileName, ds,
                        consolFun, earliestUpdateTime, latestUpdateTime);
        ArrayList<TimeSeriesPoint> tspoints = new ArrayList<TimeSeriesPoint>();

        try {
            SubSliceQuery<String, String, Long, Double> ssquery = HFactory.createSubSliceQuery(m_connection.getKeyspace(), s_ss, s_ss, s_ls,
                                                                                               s_ds);
            ssquery.setColumnFamily(m_connection.getDataPointCFName());
            ssquery.setKey(m_fileName);
            ssquery.setSuperColumn(ds);
            ssquery.setRange(earliestUpdateTime, latestUpdateTime, false, Integer.MAX_VALUE);

            QueryResult<ColumnSlice<Long, Double>> ssresults = ssquery.execute();

            List<HColumn<Long, Double>> ssdatapoints = ssresults.get().getColumns();

            LogUtils.debugf(this, "found %d datapoints\n", ssdatapoints.size());
            for (HColumn<Long, Double> dp : ssdatapoints) {
                tspoints.add(new TimeSeriesPoint(dp.getName().longValue(), dp.getValue().doubleValue()));
            }
        } catch (HectorException e) {
            throw new org.opennms.netmgt.rrd.RrdException(e.getMessage());
        }
        return tspoints;
    }

}

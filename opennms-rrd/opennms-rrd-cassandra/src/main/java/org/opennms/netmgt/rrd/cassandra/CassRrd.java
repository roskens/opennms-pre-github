package org.opennms.netmgt.rrd.cassandra;

import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.DoubleSerializer;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.util.RangeBuilder;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Comparator;
import java.util.Collections;

import org.opennms.core.xml.JaxbUtils;

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
            ColumnFamily<String, String> columnFamily = new ColumnFamily(connection.getMetaDataCFName(), s_ss, s_ss);

            Column<String> result = connection.getKeyspace().prepareQuery(columnFamily)
                    .getKey(fileName)
                    .getColumn(fileName)
                    .execute().getResult();

            if (result == null) {
                throw new RrdException("file " + m_fileName + " does not exist!");
            }
            if (result.getStringValue().isEmpty()) {
                throw new RrdException("file " + m_fileName + " had no valid metadata?");
            }

            LogUtils.debugf(this, "metadata: found xml data for %s", m_fileName);
            m_rrddef = JaxbUtils.unmarshal(RrdDef.class, result.getStringValue(), true);
        } catch (ConnectionException e) {
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

    // FIXME: need to handle the case where we are missing midpoints inside our range.
    public List<TimeSeriesPoint> fetchRequest(final String ds, final String consolFun,
            final Long earliestUpdateTime, final Long latestUpdateTime) throws org.opennms.netmgt.rrd.RrdException {
        LogUtils.debugf(this, "fetchRequest(): fileName=%s, datasource=%s, consolFun=%s, begin=%d, end=%d", m_fileName, ds,
                        consolFun, earliestUpdateTime, latestUpdateTime);
        ArrayList<TimeSeriesPoint> tspoints = new ArrayList<TimeSeriesPoint>();

        try {
            AnnotatedCompositeSerializer<DataPointColumn> dpSerializer = new AnnotatedCompositeSerializer<DataPointColumn>(DataPointColumn.class);

            OperationResult<ColumnList<DataPointColumn>> result;
            ColumnFamily<String, DataPointColumn> columnFamily = new ColumnFamily(m_connection.getDataPointCFName(), StringSerializer.get(), dpSerializer);

            result = m_connection.getKeyspace().prepareQuery(columnFamily)
                    .getKey(m_fileName)
                    .withColumnRange(new RangeBuilder().setStart(earliestUpdateTime).setEnd(latestUpdateTime).build())
                    .execute();

            ColumnList<DataPointColumn> columnList = result.getResult();

            LogUtils.tracef(this, "found %d datapoints", columnList.size());
            Long t0 = null;
            for(int i = 0; i < columnList.size(); i++) {
                Column<DataPointColumn> col = columnList.getColumnByIndex(i);
                DataPointColumn dp = col.getName();
                Long dpTs = dp.m_timestamp;
                if (t0 != null){
                    while (t0 < dpTs) {
                        tspoints.add(new TimeSeriesPoint(t0, Double.valueOf(Double.NaN)));
                        t0 += getStep();
                    }
                }
                tspoints.add(new TimeSeriesPoint(normalize(dpTs, getStep()), col.getDoubleValue()));
                t0 += dpTs + getStep();
            }


        } catch (ConnectionException e) {
            throw new org.opennms.netmgt.rrd.RrdException(e.getMessage());
        }

        Collections.sort(tspoints, new Comparator<TimeSeriesPoint>() {
            public int compare(TimeSeriesPoint o1, TimeSeriesPoint o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });

        return tspoints;
    }

}

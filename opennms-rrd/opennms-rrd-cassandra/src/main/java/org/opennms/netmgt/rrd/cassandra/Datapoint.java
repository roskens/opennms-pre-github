package org.opennms.netmgt.rrd.cassandra;

import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class Datapoint extends TimeSeriesPoint {
    private String m_fileName;

    private String m_dsName;

    private static final StringSerializer s_ss = StringSerializer.get();

    private static AnnotatedCompositeSerializer<DataPointColumn> dpSerializer
            = new AnnotatedCompositeSerializer<DataPointColumn>(DataPointColumn.class);

    public Datapoint(String fileName, String dsName, long timestamp, double value) {
        super(timestamp, value);
        m_fileName = fileName;
        m_dsName = dsName;
    }

    public String getFileName() {
        return m_fileName;
    }

    public String getDsName() {
        return m_dsName;
    }

    public void persist(MutationBatch mutator, String dpColumnFamily, int ttl) {
        ColumnFamily<String, DataPointColumn> columnFamily = new ColumnFamily(dpColumnFamily, s_ss, dpSerializer);

        mutator.withRow(columnFamily, getFileName())
                .putColumn(new DataPointColumn(getDsName(), getTimestamp()), getValue(), ttl);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getFileName()).append("(").append(getDsName()).append("):");
        buf.append(getTimestamp()).append("=").append(getValue());
        return buf.toString();
    }
}

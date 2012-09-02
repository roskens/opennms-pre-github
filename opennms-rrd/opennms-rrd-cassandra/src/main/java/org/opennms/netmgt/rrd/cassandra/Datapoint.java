package org.opennms.netmgt.rrd.cassandra;

import java.util.Collections;

import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.opennms.core.utils.LogUtils;

public class Datapoint extends TimeSeriesPoint {
    private String m_fileName;

    private String m_dsName;

    private static final StringSerializer s_ss = StringSerializer.get();

    private static final LongSerializer s_ls = LongSerializer.get();

    private static final DoubleSerializer s_ds = DoubleSerializer.get();

    Datapoint(String fileName, String dsName, long timestamp, double value) {
        super(timestamp, value);
        m_fileName = fileName;
        m_dsName = dsName;
    }

    public String getName() {
        return m_fileName;
    }

    public String getDsName() {
        return m_dsName;
    }

    public void persist(Mutator<String> mutator, String columnFamily, int ttl) {
        LogUtils.debugf(this, "datapoint['%s']['%s'][ %d ] = %f", getName(), getDsName(), getTimestamp(), getValue());

        HColumn<Long, Double> c = HFactory.createColumn(Long.valueOf(getTimestamp()), getValue(), ttl, s_ls, s_ds);
        HSuperColumn<String, Long, Double> superColumn = HFactory.createSuperColumn(getDsName(), Collections.singletonList(c),
                                                                                    s_ss, s_ls, s_ds);
        mutator.addInsertion(getName(), columnFamily, superColumn);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(m_fileName).append("(").append(m_dsName).append("):");
        buf.append(getTimestamp()).append("=").append(getValue());
        return buf.toString();
    }
}

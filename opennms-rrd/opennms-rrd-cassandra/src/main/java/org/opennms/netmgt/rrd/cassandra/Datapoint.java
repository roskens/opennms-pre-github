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

<<<<<<< HEAD
class Datapoint {
=======
public class Datapoint extends TimeSeriesPoint {
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
    private String m_fileName;

    private String m_dsName;

<<<<<<< HEAD
    private long m_timestamp;

    private double m_value;

    Datapoint(String fileName, String dsName, long timestamp, double value) {
        m_fileName = fileName;
        m_dsName = dsName;
        m_timestamp = timestamp;
        m_value = value;
=======
    private static final StringSerializer s_ss = StringSerializer.get();

    private static final LongSerializer s_ls = LongSerializer.get();

    private static final DoubleSerializer s_ds = DoubleSerializer.get();

    Datapoint(String fileName, String dsName, long timestamp, double value) {
        super(timestamp, value);
        m_fileName = fileName;
        m_dsName = dsName;
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
    }

    public String getName() {
        return m_fileName;
    }

    public String getDsName() {
        return m_dsName;
    }

<<<<<<< HEAD
    public long getTimestamp() {
        return m_timestamp;
    }

    public double getValue() {
        return m_value;
    }

    public void persist(Mutator<String> mutator, String columnFamily, int ttl) {
        LogUtils.debugf(this, "datapoint['%s']['%s'][ %d ] = %f", getName(), getDsName(), getTimestamp(), getValue());

        HColumn<Long, Double> c = HFactory.createColumn(Long.valueOf(getTimestamp()),
                                                        getValue(),
                                                        ttl,
                                                        LongSerializer.get(),
                                                        DoubleSerializer.get());
        HSuperColumn<String, Long, Double> superColumn = HFactory.createSuperColumn(getDsName(),
                                                                                    Collections.singletonList(c),
                                                                                    StringSerializer.get(),
                                                                                    LongSerializer.get(),
                                                                                    DoubleSerializer.get());
=======
    public void persist(Mutator<String> mutator, String columnFamily, int ttl) {
        LogUtils.debugf(this, "datapoint['%s']['%s'][ %d ] = %f", getName(), getDsName(), getTimestamp(), getValue());

        HColumn<Long, Double> c = HFactory.createColumn(Long.valueOf(getTimestamp()), getValue(), ttl, s_ls, s_ds);
        HSuperColumn<String, Long, Double> superColumn = HFactory.createSuperColumn(getDsName(), Collections.singletonList(c),
                                                                                    s_ss, s_ls, s_ds);
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
        mutator.addInsertion(getName(), columnFamily, superColumn);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(m_fileName).append("(").append(m_dsName).append("):");
<<<<<<< HEAD
        buf.append(m_timestamp).append("=").append(m_value);
=======
        buf.append(getTimestamp()).append("=").append(getValue());
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
        return buf.toString();
    }
}

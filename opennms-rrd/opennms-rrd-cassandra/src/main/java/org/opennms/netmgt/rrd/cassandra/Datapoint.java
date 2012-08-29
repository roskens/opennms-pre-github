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

class Datapoint {
    private String m_fileName;

    private String m_dsName;

    private long m_timestamp;

    private double m_value;

    Datapoint(String fileName, String dsName, long timestamp, double value) {
        m_fileName = fileName;
        m_dsName = dsName;
        m_timestamp = timestamp;
        m_value = value;
    }

    public String getName() {
        return m_fileName;
    }

    public String getDsName() {
        return m_dsName;
    }

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
        mutator.addInsertion(getName(), columnFamily, superColumn);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(m_fileName).append("(").append(m_dsName).append("):");
        buf.append(m_timestamp).append("=").append(m_value);
        return buf.toString();
    }
}

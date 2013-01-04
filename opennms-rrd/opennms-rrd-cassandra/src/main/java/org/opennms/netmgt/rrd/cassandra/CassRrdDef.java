package org.opennms.netmgt.rrd.cassandra;

import com.netflix.astyanax.ColumnMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opennms.core.utils.LogUtils;
import org.opennms.core.xml.JaxbUtils;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.cassandra.config.Archive;
import org.opennms.netmgt.rrd.cassandra.config.Datasource;
import org.opennms.netmgt.rrd.cassandra.config.RrdDef;

public class CassRrdDef {
    /**
     * default RRD step to be used if not specified in constructor (300 seconds)
     */
    public static final long DEFAULT_STEP = 300L;

    private String m_fileName;

    private RrdDef m_rrddef;

    private static final StringSerializer s_ss = StringSerializer.get();

    public CassRrdDef(String fileName, int step) {
        m_fileName = fileName;
        m_rrddef = new RrdDef();
        m_rrddef.setStep(Long.valueOf(step));
    }

    public String getFileName() {
        return m_fileName;
    }

    public Long getStep() {
        return m_rrddef.getStep().longValue();
    }

    public Datasource[] getDatasources() {
        return m_rrddef.getDatasource();
    }

    public Archive[] getArchives() {
        return m_rrddef.getArchive();
    }

    public List<String> getDatasourceNames() {
        List<String> dsNames = new ArrayList<String>();
        for (Datasource ds : m_rrddef.getDatasourceCollection()) {
            dsNames.add(ds.getName());
        }
        return dsNames;
    }

    public void addDatasource(String name, String type, int heartBeat, Double dsMin, Double dsMax) {
        Datasource ds = new Datasource();
        ds.setName(name);
        ds.setType(type);
        ds.setHeartbeat(Long.valueOf(heartBeat));
        ds.setMin((dsMin == null || dsMin.isNaN()) ? "U" : dsMin.toString());
        ds.setMax((dsMax == null || dsMax.isNaN()) ? "U" : dsMax.toString());
        m_rrddef.addDatasource(ds);
    }

    public void addDatasources(List<RrdDataSource> datasources) {
        for (RrdDataSource ds : datasources) {
            Datasource nds = new Datasource();
            nds.setName(ds.getName());
            nds.setType(ds.getType());
            nds.setHeartbeat(Long.valueOf(ds.getHeartBeat()));
            nds.setMin(ds.getMin());
            nds.setMax(ds.getMax());
            m_rrddef.addDatasource(nds);
        }
    }

    public void addArchive(String rra) {
        String[] a = rra.split(":");
        if (a.length == 5) {
            Archive arc = new Archive();
            arc.setCf(a[1]);
            arc.setXff(Double.valueOf(a[2]));
            arc.setSteps(Integer.valueOf(a[3]));
            arc.setRows(Integer.valueOf(a[4]));
            m_rrddef.addArchive(arc);
        }
    }

    public void addArchives(List<String> rraList) {
        for (int i = 0; i < rraList.size(); i++) {
            String[] a = rraList.get(i).split(":");
            if (a.length == 5) {
                Archive arc = new Archive();
                arc.setCf(a[1]);
                arc.setXff(Double.valueOf(a[2]));
                arc.setSteps(Integer.valueOf(a[3]));
                arc.setRows(Integer.valueOf(a[4]));
                m_rrddef.addArchive(arc);
            }
        }
    }

    public void create(CassandraRrdConnection connection) throws RrdException {
        File f = new File(m_fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
                LogUtils.debugf(this, "Created new CassRrd file %s", m_fileName);
            } catch (IOException e) {
                throw new RrdException("Could not create local file " + m_fileName + ": " + e.getMessage());
            }
        }
        LogUtils.debugf(this, "create: keyspace: %s", connection.getKeyspace().getKeyspaceName());

        ColumnFamily<String, String> columnFamily = new ColumnFamily(connection.getMetaDataCFName(), s_ss, s_ss);
        ColumnMutation mutation = connection.getKeyspace().prepareColumnMutation(columnFamily, m_fileName, m_fileName);
        try {
            OperationResult<Void> result = mutation.putValue(toXml(), null).execute();
        } catch (ConnectionException ex) {
            LogUtils.errorf(this, ex, "ConnectionException: m_fileName: %s", m_fileName);
        }
    }

    public String toXml() {
        final StringWriter writer = new StringWriter();
        JaxbUtils.marshal(m_rrddef, writer);
        final String xml = writer.toString();

        return xml;
    }

    public RrdDef getRrdDef() {
        return m_rrddef;
    }
}

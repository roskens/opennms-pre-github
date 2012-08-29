package org.opennms.netmgt.rrd.cassandra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.ResultStatus;
import me.prettyprint.hector.api.exceptions.HectorException;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdException;

public class CassRrdDef {
    /**
     * default RRD step to be used if not specified in constructor (300 seconds)
     */
    public static final long DEFAULT_STEP = 300L;

    private String m_fileName;

    private long m_step = DEFAULT_STEP;

    private ArrayList<RrdDataSource> m_datasources = new ArrayList<RrdDataSource>();

    private ArrayList<String> m_archives = new ArrayList<String>();
    
    private static final StringSerializer s_ss = StringSerializer.get();
    
    public CassRrdDef(String fileName, int step) {
        m_fileName = fileName;
        m_step = step;
        m_datasources = new ArrayList<RrdDataSource>();
        m_archives = new ArrayList<String>();
    }

    public String getFileName() {
        return m_fileName;
    }

    public long getStep() {
        return m_step;
    }

    public ArrayList<RrdDataSource> getDatasources() {
        return m_datasources;
    }

    public ArrayList<String> getArchives() {
        return m_archives;
    }

    public List<String> getDatasourceNames() {
        List<String> dsNames = new ArrayList<String>();
        for (RrdDataSource ds : m_datasources) {
            dsNames.add(ds.getName());
        }
        return dsNames;
    }

    public void addDatasource(String name, String type, int heartBeat, Double dsMin, Double dsMax) {
        // throw new UnsupportedOperationException("CassRrdDef.addDatasource is not yet implemented.");
        m_datasources.add(new RrdDataSource(name, type, heartBeat, dsMin != null ? dsMin.toString() : "U",
                                            dsMax != null ? dsMax.toString() : "U"));
    }

    public void addDatasources(List<RrdDataSource> datasources) {
        m_datasources.addAll(datasources);
    }

    public void addArchive(String rra) {
        // throw new UnsupportedOperationException("CassRrdDef.addArchive is not yet implemented.");
        m_archives.add(rra);
    }

    public void addArchives(List<String> rraList) {
        m_archives.addAll(rraList);
    }

    public void create(Keyspace keyspace, String mdColumnFamily) throws RrdException {
        LogUtils.debugf(this, "begin");
        // throw new UnsupportedOperationException("CassRrdDef.create is not yet implemented.");
        // TODO: Only need this while ResourceTypeUtils does file system scans for datasources.
        File f = new File(m_fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
                LogUtils.debugf(this, "Created new CassRrd file %s", m_fileName);
            } catch (IOException e) {
                throw new RrdException("Could not create local file " + m_fileName + ": " + e.getMessage());
            }
        }
        LogUtils.debugf(this, "create: keyspace: %s", keyspace.getKeyspaceName());

        // metadata[$m_fileName][$m_fileName] = (rrd def as xml)

        Mutator<String> mutator = HFactory.createMutator(keyspace, s_ss);
        mutator.insert(m_fileName, mdColumnFamily, HFactory.createStringColumn(m_fileName, toXml()));

        try {
            LogUtils.debugf(this, "mutator.execute()");
            ResultStatus result = mutator.execute();
            if (result == null) {
                LogUtils.warnf(this, "result was null after execute()");
            }
        } catch (HectorException e) {
            LogUtils.errorf(this, e, "exception");
        }
        LogUtils.debugf(this, "finished");
    }

    public String toXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<rrd_def>");
        sb.append("<step>").append(m_step).append("</step>");
        for (RrdDataSource ds : m_datasources) {
            sb.append("<datasource>");
            sb.append("<name>").append(ds.getName()).append("</name>");
            sb.append("<type>").append(ds.getType()).append("</type>");
            sb.append("<heartbeat>").append(ds.getHeartBeat()).append("</heartbeat>");
            sb.append("<min>").append(ds.getMin()).append("</min>");
            sb.append("<max>").append(ds.getMax()).append("</max>");
            sb.append("</datasource>");
        }
        for (int i = 0; i < m_archives.size(); i++) {
            String[] a = m_archives.get(i).split(":");
            if (a.length == 5) {
                sb.append("<archive>");
                sb.append("<cf>").append(a[1]).append("</cf>");
                sb.append("<xff>").append(a[2]).append("</xff>");
                sb.append("<steps>").append(a[3]).append("</steps>");
                sb.append("<rows>").append(a[4]).append("</rows>");
                sb.append("</archive>");
            }
        }
        sb.append("</rrd_def>");

        return sb.toString();
    }

}

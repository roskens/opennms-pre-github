package org.opennms.netmgt.rrd.cassandra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.ResultStatus;
import me.prettyprint.hector.api.exceptions.HectorException;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.rrd.RrdDataSource;

public class CassRrdDef {
    private String m_fileName;
    private int m_step;
    private List<RrdDataSource> m_datasources;
    private List<String> m_archives;

    public CassRrdDef(String creator, String fileName, int step) {
        //throw new UnsupportedOperationException("CassRrdDef constructor is not yet implemented.");
        m_fileName = fileName;
        m_step = step;
        m_datasources = new ArrayList<RrdDataSource>();
        m_archives = new ArrayList<String>();
    }

    public String getFileName() {
        return m_fileName;
    }

    public int getStep() {
        return m_step;
    }

    public List<String> getDatasourceNames() {
        List<String> dsNames = new ArrayList<String>();
        for(RrdDataSource ds : m_datasources) {
            dsNames.add(ds.getName());
        }
        return dsNames;
    }

    public List<String> getArchiveNames() {
        return m_archives;
    }

    public void addDatasource(String name, String type, int heartBeat, Double dsMin, Double dsMax) {
        //throw new UnsupportedOperationException("CassRrdDef.addDatasource is not yet implemented.");
        m_datasources.add(new RrdDataSource(name, type, heartBeat, dsMin != null ? dsMin.toString() : "U", dsMax != null ? dsMax.toString() : "U"));
    }

    public void addDatasources(List<RrdDataSource> datasources) {
        m_datasources.addAll(datasources);
    }

    public void addArchive(String rra) {
        //throw new UnsupportedOperationException("CassRrdDef.addArchive is not yet implemented.");
        m_archives.add(rra);
    }

    public void addArchives(List<String> rraList) {
        m_archives.addAll(rraList);
    }

    public void create(Keyspace keyspace) {
        LogUtils.debugf(this, "begin");
        //throw new UnsupportedOperationException("CassRrdDef.create is not yet implemented.");
        // TODO: Only need this while ResourceTypeUtils does file system scans for datasources.
        File f = new File(m_fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
                LogUtils.debugf(this, "Created new CassRrd file %s", m_fileName);
            } catch (IOException e) {
            }
        }
        LogUtils.debugf(this, "create: keyspace: %s", keyspace.getKeyspaceName());

        Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());

        // metadata[$m_fileName][ds:$dsname][type=type, heartbeat=${heartBeat}, min=${dsMin}, max=${dsMax}]
        // metadata[$m_fileName][rra:${rra}][consolefun, xff, steps, rows]

        List<HColumn<String,String>> mdColumns = new ArrayList<HColumn<String,String>>();
        mdColumns.add(HFactory.createStringColumn("step", Integer.toString(m_step)));
        mutator.addInsertion(m_fileName, "metadata",
                HFactory.createSuperColumn("fileinfo", mdColumns, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
        );

        LogUtils.debugf(this, "do datasources");
        for(RrdDataSource ds : m_datasources) {
            mdColumns.clear();
            mdColumns.add(HFactory.createStringColumn("type", ds.getType()));
            mdColumns.add(HFactory.createStringColumn("heartbeat", Integer.toString(ds.getHeartBeat())));
            mdColumns.add(HFactory.createStringColumn("min", ds.getMin()));
            mdColumns.add(HFactory.createStringColumn("max", ds.getMax()));

            mutator.addInsertion(m_fileName, "metadata",
                    HFactory.createSuperColumn("ds:"+ds.getName(), mdColumns, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
            );
        }

        LogUtils.debugf(this, "do archives");
        for(int i = 0; i < m_archives.size(); i++) {
            mdColumns.clear();
            String[] a = m_archives.get(i).split(":");
            if (a.length == 5) {
                mdColumns.add(HFactory.createStringColumn("cfun",  a[1]));
                mdColumns.add(HFactory.createStringColumn("xff",   a[2]));
                mdColumns.add(HFactory.createStringColumn("steps", a[3]));
                mdColumns.add(HFactory.createStringColumn("rows",  a[4]));
            }
            mutator.addInsertion(m_fileName, "metadata",
                HFactory.createSuperColumn("archives["+i+"]", mdColumns, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
            );
        }
        LogUtils.debugf(this, "mutator.execute()");
	try {
            ResultStatus result = mutator.execute();
            if (result == null) {
                LogUtils.warnf(this, "result was null after execute()");
            }
	} catch (HectorException e) {
            LogUtils.errorf(this, e, "exception");
        }
        LogUtils.debugf(this, "finished");
    }

}

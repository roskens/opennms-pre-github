package org.opennms.netmgt.rrd.cassandra;

import java.util.List;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SuperSliceQuery;

import org.opennms.core.utils.LogUtils;

public class CassRrd {

    private String m_fileName;

    private int m_step;

    private List<String> m_dsNames;

    private List<String> m_archiveNames;

    public CassRrd(CassRrdDef def) {
        m_fileName = def.getFileName();
        m_step = def.getStep();
        m_dsNames = def.getDatasourceNames();
        m_archiveNames = def.getArchiveNames();
    }

    public CassRrd(Keyspace keyspace, String mdColumnFamily, String fileName) {
        m_fileName = fileName;

        // XXX: search cassandra for fileName
        try {
            SuperSliceQuery<String, String, String, String> mdQuery = HFactory.createSuperSliceQuery(keyspace,
                                                                                                     StringSerializer.get(),
                                                                                                     StringSerializer.get(),
                                                                                                     StringSerializer.get(),
                                                                                                     StringSerializer.get());
            mdQuery.setColumnFamily(mdColumnFamily);
            mdQuery.setKey(fileName);
            mdQuery.setRange("", "", false, Integer.MAX_VALUE); // Return all results.

            QueryResult<SuperSlice<String, String, String>> mdResults = mdQuery.execute();
            List<HSuperColumn<String, String, String>> mdList = mdResults.get().getSuperColumns();

            LogUtils.debugf(this, "metadata: %d results returned", mdList.size());

            for (HSuperColumn<String, String, String> metadata : mdList) {
                LogUtils.debugf(this, "metadata['%s'](%d)", metadata.getName(), metadata.getSize());
                if(metadata.getName().equals("fileinfo")) {

                } else if (metadata.getName().startsWith("ds:")) {

                } else if (metadata.getName().startsWith("archives[")) {

                }
                for (HColumn<String, String> mdata : metadata.getColumns()) {
                    LogUtils.debugf(this, "metadata['%s']['%s'] = '%s'", metadata.getName(), mdata.getName(), mdata.getValue());
                }
            }

        } catch (Exception e) {
            LogUtils.errorf(this, e, "exception on search");
        }

    }

    public String getFileName() {
        return m_fileName;
    }

    public int getStep() {
        return m_step;
    }

    public List<String> getDatasourceNames() {
        return m_dsNames;
    }

    public List<String> getArchiveNames() {
        return m_archiveNames;
    }

    public void close() {
    }

}

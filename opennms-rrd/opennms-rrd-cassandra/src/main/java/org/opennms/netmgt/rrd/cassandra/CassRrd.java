package org.opennms.netmgt.rrd.cassandra;

import java.util.List;

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

    public CassRrd(String fileName) {
        m_fileName = fileName;
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

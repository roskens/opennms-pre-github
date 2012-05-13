package org.opennms.netmgt.rrd.cassandra;
import java.io.File;
import java.io.IOException;

public class CassRrdDef {
	private String m_fileName;

	public CassRrdDef(String creator, String fileName, int step) {
		//throw new UnsupportedOperationException("CassRrdDef constructor is not yet implemented.");
		m_fileName = fileName;
	}

	public void addDatasource(String name, String type, int heartBeat, double min, double max) {
		//throw new UnsupportedOperationException("CassRrdDef.addDatasource is not yet implemented.");
	}

	public void addArchive(String rra) {
		//throw new UnsupportedOperationException("CassRrdDef.addArchive is not yet implemented.");
	}

	public void create() {
		//throw new UnsupportedOperationException("CassRrdDef.create is not yet implemented.");
		File f = new File(m_fileName);
		try {
			f.createNewFile();
		} catch (IOException e) {
		}
	}

}

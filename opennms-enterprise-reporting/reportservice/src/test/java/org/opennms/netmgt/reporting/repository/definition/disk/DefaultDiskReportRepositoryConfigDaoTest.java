package org.opennms.netmgt.reporting.repository.definition.disk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.disk.dao.DefaultDiskReportRepositoryConfigDao;

import java.util.List;

import static junit.framework.Assert.*;

public class DefaultDiskReportRepositoryConfigDaoTest {
    private DefaultDiskReportRepositoryConfigDao dao;

    @Before
    public void setUp() {
        //TODO Tak: Provide a test data as xml file something
        //TODO Tak: Make path to configFile changeable on runtime for testing
        //Test requires report-disk-repository.xml at java.io.tmpdir/etc
        System.setProperty("opennms.home", System.getProperty("java.io.tmpdir"));
        dao = new DefaultDiskReportRepositoryConfigDao();
    }

    @After
    public void cleanUp() {
        dao = null;
        System.setProperty("opennms.home", "nullPath");
    }

    @Test
    public final void getAllReportDefinitions() {
        List<ReportDefinition> results = dao.getReportDefinitionList();
        assertEquals(2, results.size());
        System.out.println(results.get(0));
        System.out.println(results.get(1));
    }
    @Test
    public final void getAllReportDefinitionsIfConfigFileNotFound() {
        List<ReportDefinition> results = dao.getReportDefinitionList();
        assertEquals(0, results.size());
    }
}

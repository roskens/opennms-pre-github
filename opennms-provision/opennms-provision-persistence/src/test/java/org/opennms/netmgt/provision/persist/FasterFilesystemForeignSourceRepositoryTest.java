/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.netmgt.provision.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.Duration;
import org.junit.Test;
import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;

/**
 * The Class FasterFilesystemForeignSourceRepositoryTest.
 */
public class FasterFilesystemForeignSourceRepositoryTest {

    /**
     * Test active foreign source names.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testActiveForeignSourceNames() throws Exception {
        FileSystemBuilder bldr = new FileSystemBuilder("target", "testActiveForeignSourceNames");

        File fsDir = bldr.dir("foreignSource").file("test.xml").file("noreq.xml").pop();
        File reqDir = bldr.dir("requisitions").file("test.xml").file("pending.xml").pop();

        FasterFilesystemForeignSourceRepository repo = repo(fsDir, reqDir);

        assertEquals(set("test", "pending", "noreq"), repo.getActiveForeignSourceNames());
    }

    /**
     * Test get foreign source count.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetForeignSourceCount() throws Exception {
        FileSystemBuilder bldr = new FileSystemBuilder("target", "testGetForeignSourceCount");

        File fsDir = bldr.dir("foreignSource").file("test.xml", fs("test")).file("noreq.xml", fs("noreq")).file("another.xml",
                                                                                                                fs("another")).pop();
        File reqDir = bldr.dir("requisitions").file("test.xml").file("pending.xml").pop();

        FasterFilesystemForeignSourceRepository repo = repo(fsDir, reqDir);

        assertEquals(3, repo.getForeignSourceCount());
    }

    /**
     * Test get foreign sources.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetForeignSources() throws Exception {
        FileSystemBuilder bldr = new FileSystemBuilder("target", "testGetForeignSources");

        File fsDir = bldr.dir("foreignSource").file("test.xml", fs("test")).pop();
        File reqDir = bldr.dir("requisitions").file("test.xml").file("pending.xml").pop();

        FasterFilesystemForeignSourceRepository repo = repo(fsDir, reqDir);

        Set<ForeignSource> foreignSources = repo.getForeignSources();

        assertEquals(1, foreignSources.size());
        ForeignSource testFS = foreignSources.iterator().next();
        assertEquals("test", testFS.getName());
        assertEquals(Duration.standardDays(1), testFS.getScanInterval());
    }

    /**
     * Test get foreign source.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetForeignSource() throws Exception {

        FileSystemBuilder bldr = new FileSystemBuilder("target", "testGetForeignSource");

        File fsDir = bldr.dir("foreignSource").file("test.xml", fs("test")).file("noreq.xml", fs("noreq")).pop();
        File reqDir = bldr.dir("requisitions").file("test.xml").file("pending.xml").pop();

        FasterFilesystemForeignSourceRepository repo = repo(fsDir, reqDir);

        ForeignSource testFS = repo.getForeignSource("test");

        assertEquals("test", testFS.getName());
        assertEquals(Duration.standardDays(1), testFS.getScanInterval());
    }

    /**
     * Test get requisition.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetRequisition() throws Exception {

        FileSystemBuilder bldr = new FileSystemBuilder("target", "testGetForeignSource");

        File fsDir = bldr.dir("foreignSource").file("test.xml", fs("test")).file("noreq.xml", fs("noreq")).pop();
        File reqDir = bldr.dir("requisitions").file("test.xml", req("test")).file("pending.xml", req("pending")).pop();

        FasterFilesystemForeignSourceRepository repo = repo(fsDir, reqDir);

        Requisition testReq = repo.getRequisition("test");

        assertEquals("test", testReq.getForeignSource());
        RequisitionNode node = testReq.getNode("1234");
        assertNotNull(node);
        assertEquals("node1", node.getNodeLabel());
    }

    /**
     * Repo.
     *
     * @param foreignSourceDir
     *            the foreign source dir
     * @param requisitionDir
     *            the requisition dir
     * @return the faster filesystem foreign source repository
     * @throws Exception
     *             the exception
     */
    private FasterFilesystemForeignSourceRepository repo(File foreignSourceDir, File requisitionDir) throws Exception {
        FasterFilesystemForeignSourceRepository repo = new FasterFilesystemForeignSourceRepository();
        repo.setForeignSourcePath(foreignSourceDir.getAbsolutePath());
        repo.setRequisitionPath(requisitionDir.getAbsolutePath());
        repo.afterPropertiesSet();
        return repo;
    }

    /**
     * Sets the.
     *
     * @param <T>
     *            the generic type
     * @param items
     *            the items
     * @return the sets the
     */
    private <T> Set<T> set(T... items) {
        Set<T> set = new HashSet<T>();
        Collections.addAll(set, items);
        return set;
    }

    /**
     * Fs.
     *
     * @param name
     *            the name
     * @return the string
     */
    private String fs(String name) {
        String template = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<foreign-source date-stamp=\"2012-12-17T13:59:04.299-05:00\" name=\"_TEMPLATE_\" xmlns=\"http://xmlns.opennms.org/xsd/config/foreign-source\">\n"
                + "    <scan-interval>1d</scan-interval>\n"
                + "    <detectors>\n"
                + "        <detector class=\"org.opennms.netmgt.provision.detector.icmp.IcmpDetector\" name=\"ICMP\"/>\n"
                + "        <detector class=\"org.opennms.netmgt.provision.detector.snmp.SnmpDetector\" name=\"SNMP\"/>\n"
                + "    </detectors>\n" + "    <policies/>\n" + "</foreign-source>";

        return template.replaceAll("_TEMPLATE_", name);
    }

    /**
     * Req.
     *
     * @param name
     *            the name
     * @return the string
     */
    private String req(String name) {
        String template = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<model-import last-import=\"2012-12-17T14:00:08.997-05:00\" foreign-source=\"_TEMPLATE_\" date-stamp=\"2012-12-17T14:00:08.757-05:00\" xmlns=\"http://xmlns.opennms.org/xsd/config/model-import\">\n"
                + "    <node node-label=\"node1\" foreign-id=\"1234\" building=\"_TEMPLATE_\">\n"
                + "        <interface snmp-primary=\"P\" status=\"1\" ip-addr=\"127.0.0.1\" descr=\"\"/>\n"
                + "    </node>\n" + "</model-import>\n" + "";

        return template.replaceAll("_TEMPLATE_", name);

    }

}

/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.dao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.*;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.opennms.core.test.ConfigurationTestUtils;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.config.DefaultDataCollectionConfigDao;
import org.opennms.netmgt.config.MibObject;
import org.opennms.netmgt.config.datacollection.DatacollectionConfig;
import org.opennms.netmgt.config.datacollection.Group;
import org.opennms.netmgt.config.datacollection.MibObj;
import org.opennms.netmgt.config.datacollection.ResourceType;
import org.opennms.netmgt.config.datacollection.SnmpCollection;
import org.opennms.netmgt.config.datacollection.SystemDef;
import org.opennms.netmgt.rrd.RrdRepository;
import org.opennms.test.mock.MockUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;

/**
 * DefaultDataCollectionConfigDaoTest
 *
 * @author <a href="mail:agalue@opennms.org">Alejandro Galue</a>
 */
public class DefaultDataCollectionConfigDaoTest {

    @Rule
    public TestName m_testName = new TestName();

    @Before
    public void setUp() {
        MockUtil.println("------------ Begin Test " + m_testName.getMethodName() + " --------------------------");
        MockLogAppender.setupLogging();
        ConfigurationTestUtils.setRelativeHomeDirectory("src/test/opennms-home");
    }

    @After
    public void tearDown() {
        MockLogAppender.assertNoWarningsOrGreater();
        MockUtil.println("------------ End Test " + m_testName.getMethodName() + " --------------------------");
    }

    @Test
    public void testNewStyle() throws Exception {
        DefaultDataCollectionConfigDao dao = instantiateDao("datacollection-config.xml", true);
        executeTests(dao);
        SnmpCollection def =  dao.getContainer().getObject().getSnmpCollection("default");
        Assert.assertEquals(0, def.getResourceTypes().size());
        SnmpCollection rt =  dao.getContainer().getObject().getSnmpCollection("__resource_type_collection");
        Assert.assertEquals(88, rt.getResourceTypes().size());
        Assert.assertEquals(0, rt.getSystems().getSystemDefs().size());
        Assert.assertEquals(0, rt.getGroups().getGroups().size());
    }

    @Test
    public void testOldStyle() throws Exception {
        DefaultDataCollectionConfigDao oldDao = instantiateDao("examples/old-datacollection-config.xml", false);
        executeTests(oldDao);
    }

    @Test
    public void testCompareOldAndNewStyles() throws Exception {
        DefaultDataCollectionConfigDao newDao = instantiateDao("datacollection-config.xml", true);
        DefaultDataCollectionConfigDao oldDao = instantiateDao("examples/old-datacollection-config.xml", false);
        compareContent(oldDao.getContainer().getObject(), newDao.getContainer().getObject());
    }

    @Test
    public void testReload() throws Exception {
        Path source = Paths.get("src", "test", "opennms-home", "etc");
        Path dest = Paths.get("target", "opennms-home-test", "etc");
        Files.createDirectories(dest);
        copyDirectory(source, dest);
        Path target = dest.resolve("datacollection-config.xml");
        Date currentDate = new Date(Files.getLastModifiedTime(target).toMillis());

        // Initialize the DAO with auto-reload
        DefaultDataCollectionConfigDao dao = new DefaultDataCollectionConfigDao();
        dao.setConfigDirectory(dest.resolve("datacollection"));
        dao.setConfigResource(new FileSystemResource(target.toFile()));
        dao.setReloadCheckInterval(1000l);
        dao.afterPropertiesSet();

        // Verify that it has not been reloaded
        Assert.assertTrue(currentDate.after(dao.getLastUpdate()));

        // Modify the file to trigger the reload.
        try (BufferedWriter w = Files.newBufferedWriter(target, Charset.defaultCharset(), StandardOpenOption.APPEND, StandardOpenOption.WRITE);) {
            w.write("<!-- Adding a comment to make it different. -->");
        }
        currentDate = new Date(Files.getLastModifiedTime(target).toMillis());

        // Wait and check if the data was changed.
        Thread.sleep(2000l);
        Assert.assertFalse(currentDate.after(dao.getLastUpdate()));

        deleteDirectory(dest);
    }

    /**
     * A {@code FileVisitor} that removes a file-tree ("rm -r")
     */
    static class TreeDeleter implements FileVisitor<Path> {

        TreeDeleter(Path dir) {
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            MockUtil.println("TreeDeleter.preVisitDirectory: " + dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            MockUtil.println("TreeDeleter.visitFile: " + file);
            try {
                Files.delete(file);
            } catch (IOException ex) {
                System.err.format("Unable to remove file: %s: %s%n", file, ex);
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            MockUtil.println("TreeDeleter.postVisitDirectory: " + dir);
            try {
                Files.delete(dir);
            } catch (IOException ex) {
                System.err.format("Unable to remove directory: %s: %s%n", dir, ex);
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            if (exc instanceof FileSystemLoopException) {
                System.err.println("cycle detected: " + file);
            } else {
                System.err.format("Unable to copy: %s: %s%n", file, exc);
            }
            return CONTINUE;
        }
    }

    private void deleteDirectory(Path dir) {
        try {
            EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            TreeDeleter tc = new TreeDeleter(dir);
            Files.walkFileTree(dir, opts, Integer.MAX_VALUE, tc);
            Files.delete(dir);
        } catch (IOException e) {

        }
    }

    /**
     * A {@code FileVisitor} that copies a file-tree ("cp -r")
     */
    static class TreeCopier implements FileVisitor<Path> {

        private final Path source;
        private final Path target;

        TreeCopier(Path source, Path target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            // before visiting entries in a directory we copy the directory
            // (okay if directory already exists).
            CopyOption[] options = new CopyOption[]{COPY_ATTRIBUTES, REPLACE_EXISTING};

            Path newdir = target.resolve(source.relativize(dir));
            try {
                Files.copy(dir, newdir, options);
            } catch (FileAlreadyExistsException x) {
                // ignore
            } catch (IOException x) {
                System.err.format("Unable to create: %s: %s%n", newdir, x);
                return SKIP_SUBTREE;
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            try {
                CopyOption[] options = new CopyOption[]{COPY_ATTRIBUTES, REPLACE_EXISTING};
                Files.copy(file, target.resolve(source.relativize(file)), options);
            } catch (IOException ex) {
                System.err.format("Unable to copy: %s: %s%n", source, ex);
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            // fix up modification time of directory when done
            if (exc == null) {
                Path newdir = target.resolve(source.relativize(dir));
                try {
                    FileTime time = Files.getLastModifiedTime(dir);
                    Files.setLastModifiedTime(newdir, time);
                } catch (IOException x) {
                    System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
                }
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            if (exc instanceof FileSystemLoopException) {
                System.err.println("cycle detected: " + file);
            } else {
                System.err.format("Unable to copy: %s: %s%n", file, exc);
            }
            return CONTINUE;
        }
    }

    private static boolean copyDirectory(Path sourceDir, Path destDir) {
        try {
            EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            TreeCopier tc = new TreeCopier(sourceDir, destDir);
            Files.walkFileTree(sourceDir, opts, Integer.MAX_VALUE, tc);
        } catch (IOException e) {

        }
        return true;
    }

    /**
     * Use this test to test speed improvements for the data collection config parsing code.
     */
    @Test
    @Ignore
    public void testLoadTimeOfDao() throws Exception {
        for (int i = 0; i < 100; i++) {
            instantiateDao("datacollection-config.xml", true);
        }
    }

    private void executeTests(DefaultDataCollectionConfigDao dao) {
        // Expected Values
        int netsnmpObjectsCount = 197; //  bluecat.xml, netsnmp.xml, zeus.xml
        int ciscoObjectsCount = 44;
        int resourceTypesCount = 88;
        int systemDefCount = 141;

        // Execute Tests
        executeRepositoryTest(dao);
        executeMibObjectsTest(dao, ".1.3.6.1.4.1.8072.3.2.255", netsnmpObjectsCount);
        executeMibObjectsTest(dao, ".1.3.6.1.4.1.9.1.222", ciscoObjectsCount);
        executeResourceTypesTest(dao, resourceTypesCount);
        executeSystemDefCount(dao, systemDefCount);
    }

    private void executeRepositoryTest(DefaultDataCollectionConfigDao dao) {
        Assert.assertEquals("select", dao.getSnmpStorageFlag("default"));

        // Test Repository
        RrdRepository repository = dao.getRrdRepository("default");
        Assert.assertNotNull(repository);
        Assert.assertEquals(300, repository.getStep());

        // Test Step
        Assert.assertEquals(repository.getStep(), dao.getStep("default"));

        // Test RRA List
        List<String> rras = dao.getRRAList("default");
        Assert.assertEquals(repository.getRraList().size(), rras.size());
    }

    private DefaultDataCollectionConfigDao instantiateDao(String fileName, boolean setConfigDirectory) throws Exception {
        DefaultDataCollectionConfigDao dao = new DefaultDataCollectionConfigDao();
        Path configFile = Paths.get("src/test/opennms-home/etc", fileName);
        if (setConfigDirectory) {
            Path configFolder = configFile.resolveSibling("datacollection");
            Assert.assertTrue(Files.isDirectory(configFolder));
            dao.setConfigDirectory(configFolder.toAbsolutePath());
        }
        dao.setConfigResource(new InputStreamResource(Files.newInputStream(configFile)));
        dao.afterPropertiesSet();
        return dao;
    }

    private void executeSystemDefCount(DefaultDataCollectionConfigDao dao, int expectedCount) {
        DatacollectionConfig config = dao.getContainer().getObject();
        int systemDefCount = 0;
        for (SnmpCollection collection : config.getSnmpCollections()) {
            systemDefCount += collection.getSystems().getSystemDefs().size();
        }
        Assert.assertEquals(expectedCount, systemDefCount);
    }

    private void executeResourceTypesTest(DefaultDataCollectionConfigDao dao, int expectedCount) {
        Map<String,ResourceType> resourceTypesMap = dao.getConfiguredResourceTypes();
        Assert.assertNotNull(resourceTypesMap);
        Assert.assertEquals(expectedCount, resourceTypesMap.size());
        Assert.assertTrue(resourceTypesMap.containsKey("frCircuitIfIndex")); // Used resource type
        Assert.assertTrue(resourceTypesMap.containsKey("wmiTcpipNetworkInterface")); // Unused resource type
        Assert.assertTrue(resourceTypesMap.containsKey("xmpFilesys")); // Unused resource type
    }

    private void executeMibObjectsTest(DefaultDataCollectionConfigDao dao, String systemOid, int expectedCount) {
        List<MibObject> mibObjects = dao.getMibObjectList("default", systemOid, "127.0.0.1", -1);
        Assert.assertNotNull(mibObjects);
        Assert.assertEquals(expectedCount, mibObjects.size());
    }

    private void compareContent(DatacollectionConfig refObj, DatacollectionConfig newObj) {
        Set<String> resourceTypes = new HashSet<String>();
        Set<String> systemDefs = new HashSet<String>();
        Set<String> groups = new HashSet<String>();

        for (SnmpCollection collection : refObj.getSnmpCollections()) {
            for (SystemDef sd : collection.getSystems().getSystemDefs()) {
                systemDefs.add(sd.getName());
                for (String group : sd.getCollect().getIncludeGroups()) {
                    groups.add(group);
                }
            }
            for (Group g : collection.getGroups().getGroups()) {
                if (groups.contains(g.getName())) {
                    for (MibObj mo : g.getMibObjs()) {
                        String i = mo.getInstance();
                        if (!i.matches("\\d+") && !i.equals("ifIndex"))
                            resourceTypes.add(mo.getInstance());
                    }
                }
            }
        }

        for (SnmpCollection collection : newObj.getSnmpCollections()) {
            for (Group g : collection.getGroups().getGroups()) {
                for (MibObj mo : g.getMibObjs()) {
                    String i = mo.getInstance();
                    if (!i.matches("\\d+") && !i.equals("ifIndex"))
                        resourceTypes.remove(mo.getInstance());
                }
            }
            for (SystemDef sd : collection.getSystems().getSystemDefs()) {
                systemDefs.remove(sd.getName());
                for (String group : sd.getCollect().getIncludeGroups()) {
                    groups.remove(group);
                }
            }
        }

        if (systemDefs.size() > 0) {
            Assert.fail("There are un-configured system definitions on the new datacollection: " + systemDefs);
        }

        if (groups.size() > 0) {
            Assert.fail("There are un-configured groups on the new datacollection: " + groups);
        }

        if (resourceTypes.size() > 0) {
            Assert.fail("There are un-configured resourceTypes on the new datacollection: " + resourceTypes);
        }
    }

}

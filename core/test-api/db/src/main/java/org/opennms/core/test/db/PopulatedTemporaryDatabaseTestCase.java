/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2014 The OpenNMS Group, Inc.
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

package org.opennms.core.test.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.opennms.core.db.install.InstallerDb;
import org.opennms.core.test.ConfigurationTestUtils;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.springframework.util.StringUtils;

/**
 * @deprecated Use an annotation-based temporary database with {@link JUnitTemporaryDatabase} and autowire a
 * DatabasePopulator to insert a standard set of content into the database. The context that contains the
 * DatabasePopulator is <code>classpath:/META-INF/opennms/applicationContext-databasePopulator.xml</code>.
 */
public class PopulatedTemporaryDatabaseTestCase extends
        TemporaryDatabaseTestCase {

    private InstallerDb m_installerDb = new InstallerDb();

    private ByteArrayOutputStream m_outputStream;

    private boolean m_setupIpLike = false;

    private boolean m_insertData = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        try {
            initializeDatabase();
        } finally {
            m_installerDb.closeConnection();
        }
    }

    protected void initializeDatabase() throws Exception {
        if (!isEnabled()) {
            return;
        }

        // Create a ByteArrayOutputSteam to effectively throw away output.
        resetOutputStream();
        m_installerDb.setDatabaseName(getTestDatabase());
        m_installerDb.setDataSource(getDataSource());

        m_installerDb.setAdminDataSource(getAdminDataSource());
        m_installerDb.setPostgresOpennmsUser(getAdminUser());

        m_installerDb.setCreateSqlLocation(ConfigurationTestUtils.getFileForConfigFile("create.sql"));
        m_installerDb.setStoredProcedureDirectory(ConfigurationTestUtils.getFileForConfigFile("getPercentAvailabilityInWindow.sql").getParent());

        //m_installerDb.setDebug(true);

        m_installerDb.readTables();

        m_installerDb.createSequences();
        m_installerDb.updatePlPgsql();
        m_installerDb.addStoredProcedures();

        /*
         * Here's an example of an iplike function that always returns true.
         * CREATE OR REPLACE FUNCTION iplike(text, text) RETURNS bool AS ' BEGIN RETURN true; END; ' LANGUAGE 'plpgsql';
         *
         * Found this in BaseIntegrationTestCase.
         */

        if (m_setupIpLike) {
            m_installerDb.setPostgresIpLikeLocation(null);
            m_installerDb.updateIplike();
        }

        m_installerDb.createTables();

        if(m_insertData) {
            m_installerDb.insertData();
        }

    }

    protected Path findIpLikeLibrary() {
        Path topDir = ConfigurationTestUtils.getTopProjectDirectory();

        Path ipLikeDir = topDir.resolve("opennms-iplike");
        assertTrue("iplike directory exists at ../opennms-iplike: " + ipLikeDir.toAbsolutePath(), Files.exists(ipLikeDir));
        DirectoryStream.Filter<Path> dirFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) {
                return (path.getFileName().toString().matches("opennms-iplike-.*") && Files.isDirectory(path));
            }
        };
        DirectoryStream.Filter<Path> fileFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) {
                return (path.getFileName().toString().matches("opennms-iplike-.*\\.(so|dylib)") && Files.isRegularFile(path));
            }
        };

        List<Path> ipLikePlatformDirs = new ArrayList<Path>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(ipLikeDir, dirFilter);) {
            for (Path path : stream) {
                ipLikePlatformDirs.add(path);
            }
        } catch (IOException ex) {
        }

        assertTrue("expecting at least one opennms iplike platform directory in " + ipLikeDir.toAbsolutePath() + "; got: " + StringUtils.collectionToDelimitedString(ipLikePlatformDirs, ", "), ipLikePlatformDirs.size() > 0);

        Path ipLikeFile = null;
        for (Path ipLikePlatformDir : ipLikePlatformDirs) {
            assertTrue("iplike platform directory does not exist but was listed in directory listing: " + ipLikePlatformDir.toAbsolutePath(), Files.exists(ipLikePlatformDir));

            Path ipLikeTargetDir = ipLikePlatformDir.resolve("target");
            if (!Files.exists(ipLikeTargetDir) || !Files.isDirectory(ipLikeTargetDir)) {
                // Skip this one
                continue;
            }

            List<Path> ipLikeFiles = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(ipLikeTargetDir, fileFilter);) {
                for (Path path : stream) {
                    ipLikeFiles.add(path);
                }
            } catch (IOException ex) {
            }
            assertFalse("expecting zero or one iplike file in " + ipLikeTargetDir.toAbsolutePath() + "; got: " + StringUtils.collectionToDelimitedString(ipLikeFiles, ", "), ipLikeFiles.size() > 1);

            if (ipLikeFiles.size() == 1) {
                ipLikeFile = ipLikeFiles.get(0);
            }

        }

        assertNotNull("Could not find iplike shared object in a target directory in any of these directories: " + StringUtils.collectionToDelimitedString(ipLikePlatformDirs, ", "), ipLikeFile);

        return ipLikeFile;
    }

    public ByteArrayOutputStream getOutputStream() {
        return m_outputStream;
    }

    public void resetOutputStream() {
        m_outputStream = new ByteArrayOutputStream();
        m_installerDb.setOutputStream(new PrintStream(m_outputStream));
    }

    public void setInsertData(boolean insertData) throws Exception {
        m_insertData = insertData;
    }

    public void setSetupIpLike(boolean setupIpLike) {
        m_setupIpLike = setupIpLike;
    }

    protected InstallerDb getInstallerDb() {
        return m_installerDb;
    }
}

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

package org.opennms.core.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

/**
 * <p>ConfigurationTestUtils class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public abstract class ConfigurationTestUtils extends Assert {
    private static final String POM_FILE = "pom.xml";
    // TODO: rename this constant
    private static final String DAEMON_DIRECTORY = "opennms-base-assembly";

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationTestUtils.class);


    /**
     * <p>getUrlForResource</p>
     *
     * @param obj a {@link java.lang.Object} object.
     * @param resource a {@link java.lang.String} object.
     * @return a {@link java.net.URL} object.
     */
    public static URL getUrlForResource(Object obj, String resource) {
        URL url = getClass(obj).getResource(resource);
        assertNotNull("could not get resource '" + resource + "' as a URL", url);
        return url;
    }

    private static Class<? extends Object> getClass(Object obj) {
        return (obj != null) ? obj.getClass() : ConfigurationTestUtils.class;
    }

    /**
     * <p>getSpringResourceForResource</p>
     *
     * @param obj a {@link java.lang.Object} object.
     * @param resource a {@link java.lang.String} object.
     * @return a {@link org.springframework.core.io.Resource} object.
     */
    public static Resource getSpringResourceForResource(Object obj, String resource) {
        try {
            return new FileSystemResource(getFileForResource(obj, resource).toFile());
        } catch (Throwable t) {
            return new InputStreamResource(getInputStreamForResource(obj, resource));
        }
    }

    public static Resource getSpringResourceForResourceWithReplacements(final Object obj, final String resource, final String[] ... replacements) throws IOException {
        try {
        	String config = getConfigForResourceWithReplacements(obj, resource, replacements);
            Path tmp = Files.createTempFile("testConfigFile", ".xml");
            tmp.toFile().deleteOnExit();
            try (BufferedWriter fw = Files.newBufferedWriter(tmp, Charset.defaultCharset())) {
                fw.write(config);
            }
            return new FileSystemResource(tmp.toFile());
        } catch (final Throwable t) {
            return new InputStreamResource(getInputStreamForResourceWithReplacements(obj, resource, replacements));
        }
    }

    /**
     * <p>getFileForResource</p>
     *
     * @param obj a {@link java.lang.Object} object.
     * @param resource a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     */
    public static Path getFileForResource(Object obj, String resource) {
        URL url = getUrlForResource(obj, resource);

        String path = url.getFile();
        assertNotNull("could not get resource '" + resource + "' as a file", path);

        Path file = Paths.get(path);
        assertTrue("could not get resource '" + resource + "' as a file--the file at path '" + path + "' does not exist", Files.exists(file));

        return file;
    }

    /**
     * @deprecated Use getInputStreamForResource instead.
     *
     * @param obj a {@link java.lang.Object} object.
     * @param resource a {@link java.lang.String} object.
     * @return a {@link java.io.Reader} object.
     */
    public static Reader getReaderForResource(Object obj, String resource) {
        Reader retval = null;
        try {
            retval = new InputStreamReader(getInputStreamForResource(obj, resource), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            fail("Your JVM doesn't support UTF-8 encoding, which is pretty much impossible.");
        }
        return retval;
    }

    /**
     * <p>getInputStreamForResource</p>
     *
     * @param obj a {@link java.lang.Object} object.
     * @param resource a {@link java.lang.String} object.
     * @return a {@link java.io.InputStream} object.
     */
    public static InputStream getInputStreamForResource(Object obj, String resource) {
        assertFalse("obj should not be an instance of java.lang.Class; you usually want to use 'this'", obj instanceof Class<?>);
        InputStream is = getClass(obj).getResourceAsStream(resource);
        assertNotNull("could not get resource '" + resource + "' as an input stream", is);
        return is;
    }

    /**
     * <p>getReaderForResourceWithReplacements</p>
     *
     * @param obj a {@link java.lang.Object} object.
     * @param resource a {@link java.lang.String} object.
     * @param replacements an array of {@link java.lang.String} objects.
     * @return a {@link java.io.Reader} object.
     * @throws java.io.IOException if any.
     */
    public static Reader getReaderForResourceWithReplacements(Object obj,
            String resource, String[] ... replacements) throws IOException {
        String newConfig = getConfigForResourceWithReplacements(obj, resource,
                                                                replacements);
        return new StringReader(newConfig);
    }


    /**
     * <p>getInputStreamForResourceWithReplacements</p>
     *
     * @param obj a {@link java.lang.Object} object.
     * @param resource a {@link java.lang.String} object.
     * @param replacements an array of {@link java.lang.String} objects.
     * @return a {@link java.io.InputStream} object.
     * @throws java.io.IOException if any.
     */
    public static InputStream getInputStreamForResourceWithReplacements(Object obj,
            String resource, String[] ... replacements) throws IOException {
        String newConfig = getConfigForResourceWithReplacements(obj, resource,
                                                                replacements);
        return new ByteArrayInputStream(newConfig.getBytes());
    }


    /**
     * <p>getConfigForResourceWithReplacements</p>
     *
     * @param obj a {@link java.lang.Object} object.
     * @param resource a {@link java.lang.String} object.
     * @param replacements an array of {@link java.lang.String} objects.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public static String getConfigForResourceWithReplacements(Object obj,
            String resource, String[] ... replacements) throws IOException {

        Reader inputReader = getReaderForResource(obj, resource);
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        StringBuffer buffer = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            buffer.append(line);
            buffer.append("\n");
        }

        String newConfig = buffer.toString();
        for (String[] replacement : replacements) {
            // The quoting around the replacement is necessary for file paths to work
            // correctly on Windows.
            // @see http://issues.opennms.org/browse/NMS-4853
            newConfig = newConfig.replaceAll(replacement[0], Matcher.quoteReplacement(replacement[1]));
        }

        return newConfig;
    }

    /**
     * Use getInputStreamForConfigFile instead.
     *
     * @param configFile a {@link java.lang.String} object.
     * @return a {@link java.io.Reader} object.
     * @throws java.io.FileNotFoundException if any.
     */
    public static Reader getReaderForConfigFile(Path configFile) throws IOException {
        Reader retval = null;
        try {
            retval = Files.newBufferedReader(configFile, Charset.forName("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            fail("Your JVM doesn't support UTF-8 encoding, which is pretty much impossible.");
        }
        return retval;
    }

    /**
     * <p>getInputStreamForConfigFile</p>
     *
     * @param configFile a {@link java.lang.String} object.
     * @return a {@link java.io.InputStream} object.
     * @throws java.io.FileNotFoundException if any.
     */
    public static InputStream getInputStreamForConfigFile(String configFile) {
        try {
            return Files.newInputStream(getFileForConfigFile(configFile));
        } catch (IOException e) {

        }
        return new ByteArrayInputStream("".getBytes(Charset.defaultCharset()));
    }

    /**
     * <p>getFileForConfigFile</p>
     *
     * @param configFile a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     */
    public static Path getFileForConfigFile(String configFile) {
        Path file = getDaemonEtcDirectory().resolve(configFile);
        assertTrue("configuration file '" + configFile + "' does not exist at " + file.toAbsolutePath(), Files.exists(file));
        return file;
    }

    /**
     * <p>getDaemonEtcDirectory</p>
     *
     * @return a {@link java.io.File} object.
     */
    public static Path getDaemonEtcDirectory() {
        Path etcPath = Paths.get("src", "main", "filtered", "etc");
        return getDaemonProjectDirectory().resolve(etcPath);
    }

    /**
     * <p>setRelativeHomeDirectory</p>
     *
     * @param relativeHomeDirectory a {@link java.lang.String} object.
     */
    public static void setRelativeHomeDirectory(String relativeHomeDirectory) {
        setAbsoluteHomeDirectory(getCurrentDirectory().resolve(relativeHomeDirectory).toAbsolutePath().toString());
    }

    /**
     * <p>setAbsoluteHomeDirectory</p>
     *
     * @param absoluteHomeDirectory a {@link java.lang.String} object.
     */
    public static void setAbsoluteHomeDirectory(final String absoluteHomeDirectory) {
        System.setProperty("opennms.home", absoluteHomeDirectory);
    }

    /**
     * <p>getTopProjectDirectory</p>
     *
     * @return a {@link java.io.File} object.
     */
    public static Path getTopProjectDirectory() {
        Path currentDirectory = getCurrentDirectory();

        Path pomFile = currentDirectory.resolve(POM_FILE);
        assertTrue("pom.xml in current directory should exist: " + pomFile.toAbsolutePath(), Files.exists(pomFile));

        return findTopProjectDirectory(currentDirectory);
    }

    private static Path getCurrentDirectory() {
        Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        assertTrue("current directory should exist: " + currentDirectory.toAbsolutePath(), Files.exists(currentDirectory));
        assertTrue("current directory should be a directory: " + currentDirectory.toAbsolutePath(), Files.isDirectory(currentDirectory));
        return currentDirectory;
    }

    /**
     * <p>getDaemonProjectDirectory</p>
     *
     * @return a {@link java.io.File} object.
     */
    public static Path getDaemonProjectDirectory() {
        Path topLevelDirectory = getTopProjectDirectory();
        Path daemonDirectory = topLevelDirectory.resolve(DAEMON_DIRECTORY);
        if (!Files.exists(daemonDirectory)) {
            throw new IllegalStateException("Could not find a " + DAEMON_DIRECTORY + " in the location top-level directory: " + topLevelDirectory);
        }

        Path pomFile = daemonDirectory.resolve(POM_FILE);
        assertTrue("pom.xml in " + DAEMON_DIRECTORY + " directory should exist: " + pomFile.toAbsolutePath(), Files.exists(pomFile));

        return daemonDirectory;
    }

    private static Path findTopProjectDirectory(Path currentDirectory) {
        Path buildFile = currentDirectory.resolve("compile.pl");
        if (Files.exists(buildFile)) {
            Path pomFile = currentDirectory.resolve(POM_FILE);
            assertTrue("pom.xml in " + DAEMON_DIRECTORY + " directory should exist: " + pomFile.toAbsolutePath(), Files.exists(pomFile));

            return currentDirectory;
        } else {
            Path parentDirectory = currentDirectory.getParent();

            if (parentDirectory == null || parentDirectory == currentDirectory) {
                return null;
            } else {
                return findTopProjectDirectory(parentDirectory);
            }
        }
    }

    /**
     * <p>setRrdBinary</p>
     *
     * @param path a {@link java.lang.String} object.
     */
    public static void setRrdBinary(String path) {
        System.setProperty("rrd.binary", path);
    }

    /**
     * <p>setRelativeRrdBaseDirectory</p>
     *
     * @param relativePath a {@link java.lang.String} object.
     */
    public static void setRelativeRrdBaseDirectory(String relativePath) {
        Path rrdDir = getCurrentDirectory().resolve(relativePath);
        if (!Files.exists(rrdDir)) {
            try {
                Files.createDirectories(rrdDir);
            } catch (IOException e) {
                LOG.warn("Could not make directory: {}", rrdDir);
        	}
        }
        System.setProperty("rrd.base.dir", rrdDir.toAbsolutePath().toString());
    }

    /**
     * <p>setRelativeImporterDirectory</p>
     *
     * @param relativeImporterDirectory a {@link java.lang.String} object.
     */
    public static void setRelativeImporterDirectory(String relativeImporterDirectory) {
        Path cacheDir = getCurrentDirectory().resolve(relativeImporterDirectory);
        if (!Files.exists(cacheDir)) {
            try {
                Files.createDirectories(cacheDir);
            } catch (IOException e) {
                LOG.warn("Could not make directory: {}", cacheDir);
        	}
        }
        System.setProperty("importer.requisition.dir", cacheDir.toAbsolutePath().toString());
    }

    /**
     * <p>setRelativeForeignSourceDirectory</p>
     *
     * @param relativeForeignSourceDirectory a {@link java.lang.String} object.
     */
    public static void setRelativeForeignSourceDirectory(String relativeForeignSourceDirectory) {
        Path xmlDir = getCurrentDirectory().resolve(relativeForeignSourceDirectory);
        if (!Files.exists(xmlDir)) {
            try {
                Files.createDirectories(xmlDir);
            } catch (IOException e) {
                LOG.warn("Could not make directory: {}", xmlDir);
            	}
            }
        System.setProperty("importer.foreign-source.dir", xmlDir.toAbsolutePath().toString());
    }

}

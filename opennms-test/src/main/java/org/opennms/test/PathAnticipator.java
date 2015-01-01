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

package org.opennms.test;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.junit.Assert;
import junit.framework.AssertionFailedError;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File anticipator.
 *
 * Example usage with late initialization:
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class PathAnticipator extends Assert {

	private static final Logger LOG = LoggerFactory.getLogger(PathAnticipator.class);

    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    private List<Path> m_expecting = new LinkedList<>();
    private List<Path> m_deleteMe = new LinkedList<>();
    private Path m_tempDir = null;
    private boolean m_initialized = false;

    /**
     * <p>Constructor for PathAnticipator.</p>
     *
     * @throws java.io.IOException if any.
     */
    public PathAnticipator() throws IOException {
        this(true);
    }

    /**
     * <p>Constructor for PathAnticipator.</p>
     *
     * @param initialize a boolean.
     * @throws java.io.IOException if any.
     */
    public PathAnticipator(boolean initialize) throws IOException {
        if (initialize) {
            initialize();
        }
    }

    /** {@inheritDoc}
     * @throws Throwable */
    @Override
    protected void finalize() throws Throwable {
        tearDown();
        super.finalize();
    }

    /**
     * <p>tearDown</p>
     */
    public void tearDown() {
        if (!isInitialized()) {
            return;
        }

        try {
        	// Windows is really picky about filehandle reaping, triggering a GC
        	// keeps it from holding on to files when we're trying to delete them
            deleteExpected(true);

            for (ListIterator<Path> i = m_deleteMe.listIterator(m_deleteMe.size()); i.hasPrevious();) {
                Path path = i.previous();
                if (!Files.exists(path)) {
                    continue;
                }
                if (!FileUtils.deleteQuietly(path.toFile())) {
                    final StringBuffer b = new StringBuffer();
                    b.append("Could not delete " + path.toAbsolutePath() + ": is it a non-empty directory?");
                    b.append("\nDirectory listing:");
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path);) {
                        for (Path file : stream) {
                            b.append("\n\t");
                            b.append(file.getFileName());
                        }
                    }
                    fail(b.toString());
                }
            }
            if (m_tempDir != null) {
                assertFalse(m_tempDir + " exists", Files.exists(m_tempDir));
            }
        } catch (Throwable t) {
            if (m_tempDir != null && Files.exists(m_tempDir)) {
            	try {
                    FileUtils.forceDelete(m_tempDir.toFile());
            		return;
            	} catch (final IOException innerThrowable) {
                    LOG.warn("an error occurred while forcibly removing temporary directory {}", m_tempDir, innerThrowable);
                }
            } else {
            	LOG.warn("does not exist? {}", m_tempDir, t);
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
    }

    /**
     * <p>initialize</p>
     *
     * @throws java.io.IOException if any.
     */
    public void initialize() throws IOException {
        if (m_initialized) {
            return;
        }

        String systemTempDir = System.getProperty(JAVA_IO_TMPDIR);
        assertNotNull(JAVA_IO_TMPDIR + " system property is not set, but must be", systemTempDir);

        Path path = Paths.get(systemTempDir);
        assertTrue("path specified in system property " + JAVA_IO_TMPDIR + ", \"" + systemTempDir + "\" is not a directory", Files.isDirectory(path));

        String tempFileName = "PathAnticipator_temp_" + System.currentTimeMillis() + "_" + generateRandomHexString(8);
        m_tempDir = internalTempDir(path, tempFileName);

        m_initialized = true;
    }

    /**
     * <p>generateRandomHexString</p>
     *
     * @param length a int.
     * @return a {@link java.lang.String} object.
     */
    protected static String generateRandomHexString(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length argument is " + length + " and cannot be below zero");
        }

        Random random=new Random();
        /*
        SecureRandom sometimes gets tied up in knots in testing (the test process goes off into lala land and never returns from .nextBytes)
        Slow debugging (with pauses) seems to work most of the time, but manual Thread.sleeps doesn't
        Using Random instead of SecureRandom (which should be fine in this context) works much better.  Go figure

        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            fail("Could not initialize SecureRandom: " + e);
        }*/

        byte[] bytes = new byte[length];
        random.nextBytes(bytes);

        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * <p>getTempDir</p>
     *
     * @return a {@link java.io.File} object.
     */
    public Path getTempDir() {
        assertInitialized();

        return m_tempDir;
    }

    private void assertInitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException("not initialized");
        }
    }

    /**
     * <p>tempFile</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public Path tempFile(String name) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }

        assertInitialized();

        return internalTempFile(m_tempDir, name);
    }

    /**
     * <p>tempFile</p>
     *
     * @param parent a {@link java.io.File} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public Path tempFile(Path parent, String name) throws IOException {
        if (parent == null) {
            throw new IllegalArgumentException("parent argument cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }

        assertInitialized();

        return internalTempFile(parent, name);
    }

    /**
     * <p>tempFile</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param contents a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public Path tempFile(String name, String contents) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }
        if (contents == null) {
            throw new IllegalArgumentException("contents argument cannot be null");
        }

        assertInitialized();

        return internalTempFile(m_tempDir, name, contents);
    }

    /**
     * <p>tempFile</p>
     *
     * @param parent a {@link java.io.File} object.
     * @param name a {@link java.lang.String} object.
     * @param contents a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public Path tempFile(Path parent, String name, String contents) throws IOException {
        if (parent == null) {
            throw new IllegalArgumentException("parent argument cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }
        if (contents == null) {
            throw new IllegalArgumentException("contents argument cannot be null");
        }

        assertInitialized();

        return internalTempFile(parent, name, contents);
    }

    /**
     * Non-asserting version of tempDir that can be used in initialize()
     *
     * @param parent
     * @param name
     * @return object representing the newly created temporary directory
     * @throws IOException
     */
    private Path internalTempDir(Path parent, String name) throws IOException {
        Path path = parent.resolve(name);
        assertFalse("temporary directory exists but it shouldn't: " + path.toAbsolutePath(), Files.exists(path));
        assertEquals("could not create temporary directory: " + path.toAbsolutePath(), Files.createDirectories(path), path);
        m_deleteMe.add(path);
        return path;
    }

    private Path internalTempFile(Path parent, String name) throws IOException {
        Path path = parent.resolve(name);
        assertFalse("temporary file exists but it shouldn't: " + path.toAbsolutePath(), Files.exists(path));
        assertEquals("createNewFile: " + path.toAbsolutePath(), Files.createFile(path), path);
        m_deleteMe.add(path);
        return path;
    }

    private Path internalTempFile(Path parent, String name, String contents) throws IOException {
        Path path = internalTempFile(parent, name);
        try (Writer writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));) {
            writer.write(contents);
        }
        return path;
    }

    /**
     * <p>tempDir</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public Path tempDir(String name) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }

        return tempDir(m_tempDir, name);
    }

    /**
     * <p>tempDir</p>
     *
     * @param parent a {@link java.io.File} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public Path tempDir(Path parent, String name) throws IOException {
        if (parent == null) {
            throw new IllegalArgumentException("parent argument cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }

        assertInitialized();

        return internalTempDir(parent, name);
    }

    /**
     * <p>expecting</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     */
    public Path expecting(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }
        assertInitialized();

        return internalExpecting(m_tempDir, name);
    }

    /**
     * <p>expecting</p>
     *
     * @param parent a {@link java.io.File} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     */
    public Path expecting(Path parent, String name) {
        if (parent == null) {
            throw new IllegalArgumentException("parent argument cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }

        assertInitialized();

        return internalExpecting(parent, name);
    }

    private Path internalExpecting(Path parent, String name) {
        Path f = parent.resolve(name);
        m_expecting.add(f);
        return f;
    }

    /**
     * Delete expected files, throwing an AssertionFailedError if any of
     * the expected files don't exist.
     */
    public void deleteExpected() {
        deleteExpected(false);
    }

    /**
     * Delete expected files, throwing an AssertionFailedError if any of
     * the expected files don't exist.
     *
     * @param ignoreNonExistantFiles if true, non-existant files will be
     *      ignored and will not throw an AssertionFailedError
     * @throws AssertionFailedError if ignoreNonExistantFiles is false
     *      and an expected file does not exist, or if a file cannot be deleted
     */
    public void deleteExpected(boolean ignoreNonExistantFiles) {
        assertInitialized();

        Collections.sort(m_expecting, new Comparator<Path>() {
            @Override
            public int compare(Path a, Path b) {
                return a.toAbsolutePath().compareTo(b.toAbsolutePath());
            }
        });

        List<String> errors = new ArrayList<String>();

        for (ListIterator<Path> i = m_expecting.listIterator(m_expecting.size()); i.hasPrevious();) {
            Path p = i.previous();
            if (!Files.exists(p)) {
                if (!ignoreNonExistantFiles) {
                    errors.add("Expected file that needs to be deleted does not exist: " + p.toAbsolutePath());
                }
            } else {
                if (Files.isDirectory(p)) {
                    List<Path> files = new ArrayList<>();
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(p);) {
                        for (Path file : stream) {
                            files.add(file);
                        }
                    } catch (IOException e) {
                    }

                    if (!files.isEmpty()) {
                        StringBuilder fileList = new StringBuilder("{ ");
                        fileList.append(files.get(0));
                        for (int j = 1; j < files.size(); j++) {
                            fileList.append(", ").append(files.get(j));
                        }
                        fileList.append(" }");
                        errors.add("Directory was not empty: " + p.toAbsolutePath() + ": " + fileList.toString());
                    } else {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            errors.add("Could not delete directory: " + p.toAbsolutePath());
                        }
                    }
                } else {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        errors.add("Could not delete directory: " + p.toAbsolutePath());
                    }
                }
            }
            i.remove();
        }
        assertEquals("No expected files left over", m_expecting.size(), 0);
        if (errors.size() > 0) {
            StringBuilder errorString = new StringBuilder();
            for (String error : errors) {
                errorString.append(error).append("\n");
            }
            fail("Errors occurred inside PathAnticipator:\n" + errorString.toString().trim());
        }
    }

    /**
     * <p>isInitialized</p>
     *
     * @return a boolean.
     */
    public boolean isInitialized() {
        return m_initialized;
    }

    public boolean foundExpected() {
        LOG.debug("checking for {} expected files...", m_expecting.size());
        for (final Path expected : m_expecting) {
            if (!Files.exists(expected)) {
                return false;
            }
        }
        return true;
    }

}

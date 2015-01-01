/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.rrd;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A convenience class containing RRD file and directory related constants.
 *
 * @author <a href="mailto:mike@opennms.org">Mike Davidson </a>
 * @author <a href="mailto:larry@opennms.org">Lawrence Karnowski </a>
 */
public class RrdFileConstants extends Object {

    private static final Logger LOG = LoggerFactory.getLogger(RrdFileConstants.class);
    private static final Pattern GRAPHING_ESCAPE_PATTERN;
    static {
        // IPv6: ':' and '%'
        if (File.separatorChar == '\\') {
            // If Windows, escape '\' as well
            GRAPHING_ESCAPE_PATTERN = Pattern.compile("([\\:\\%\\\\])");
        } else {
            GRAPHING_ESCAPE_PATTERN = Pattern.compile("([\\:\\%])");
        }
    }

	/** The longest an RRD filename can be, currently 1024 characters. */
    public static final int MAX_RRD_FILENAME_LENGTH = 1024;

    /** Convenience filter that matches only RRD files. */
    public static final DirectoryStream.Filter<Path> RRD_FILENAME_FILTER = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(final Path path) {
            return path.getFileName().toString().endsWith(getRrdSuffix());
        }
    };

    /** Convenience filter that matches directories with RRD files in them. */
    public static final DirectoryStream.Filter<Path> INTERFACE_DIRECTORY_FILTER = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(final Path file) {
            return isValidRRDInterfaceDir(file);
        }
    };

    /** Convenience filter that matches directories with RRD files in them. */
    public static final DirectoryStream.Filter<Path> DOMAIN_INTERFACE_DIRECTORY_FILTER = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(final Path file) {
            return isValidRRDDomainInterfaceDir(file);
        }
    };

    /**
     * Convenience filter that matches integer-named directories that either
     * contain RRD files or directories that contain RRD files.
     */
    public static final DirectoryStream.Filter<Path> NODE_DIRECTORY_FILTER = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(Path file) {
            return isValidRRDNodeDir(file);
        }
    };

    /**
     * <p>isValidRRDNodeDir</p>
     *
     * @param path a {@link java.io.File} object.
     * @return a boolean.
     */
    public static final boolean isValidRRDNodeDir(final Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        try {
            // if the directory name is an integer
            Long.valueOf(path.getFileName().toString());
        } catch (final NumberFormatException e) {
            return false;
        }

        // if the node dir contains RRDs, then it is queryable
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, RRD_FILENAME_FILTER);) {
            List<Path> nodeRRDs = new ArrayList<>();
            for (Path entry : stream) {
                nodeRRDs.add(entry);
            }
            if (nodeRRDs.size() > 0) {
                return true;
            }
        } catch (IOException ex) {
            LOG.error("exception", ex);
        }

        // if the node dir contains queryable interface directories, then
        // it is queryable
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, INTERFACE_DIRECTORY_FILTER);) {
            List<Path> intfDirs = new ArrayList<>();
            for (Path entry : stream) {
                intfDirs.add(entry);
            }
            if (intfDirs.size() > 0) {
                return true;
            }
        } catch (IOException ex) {
            LOG.error("exception", ex);
        }

        return false;
    }

    /**
     * Convenience filter that matches integer-named directories that either
     * contain RRD files or directories that contain RRD files.
     */
    public static final DirectoryStream.Filter<Path> NODESOURCE_DIRECTORY_FILTER = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(Path file) {
            return isValidRRDNodeSourceDir(file);
        }
    };

    /**
     * <p>isValidRRDNodeSourceDir</p>
     *
     * @param path a {@link java.io.File} object.
     * @return a boolean.
     */
    public static final boolean isValidRRDNodeSourceDir(final Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        // if the nodeSource dir contains RRDs, then it is queryable
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, RRD_FILENAME_FILTER);) {
            List<Path> nodeRRDs = new ArrayList<>();
            for (Path entry : stream) {
                nodeRRDs.add(entry);
            }
            if (nodeRRDs.size() > 0) {
                return true;
            }
        } catch (IOException ex) {
            LOG.error("exception", ex);
        }

        // if the nodeSource dir contains queryable interface directories, then
        // it is queryable
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, INTERFACE_DIRECTORY_FILTER);) {
            List<Path> intfDirs = new ArrayList<>();
            for (Path entry : stream) {
                intfDirs.add(entry);
            }
            if (intfDirs.size() > 0) {
                return true;
            }
        } catch (IOException ex) {
            LOG.error("exception", ex);
        }

        return false;
    }

    /**
     * Convenience filter that matches non-integer-named directories that
     * contain directories that contain RRD files.
     */
    public static final DirectoryStream.Filter<Path> DOMAIN_DIRECTORY_FILTER = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(final Path path) {
            return isValidRRDDomainDir(path);
        }
    };

    public static final DirectoryStream.Filter<Path> SOURCE_DIRECTORY_FILTER = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(final Path path) {
            return isValidRRDSourceDir(path);
        }
    };

    // FIXME This is not working and it is not being used
    public static final boolean isValidRRDSourceDir(final Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        try {
            // if the directory name is an integer
            Integer.parseInt(path.getFileName().toString());
        } catch (final Throwable e) {

            // if the source dir contains integer-named directories, then
            // it is queryable
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, NODE_DIRECTORY_FILTER);) {
                List<Path> idDirs = new ArrayList<>();
                for (Path entry : stream) {
                    idDirs.add(entry);
                }
                if (idDirs.size() > 0) {
                    return true;
                }
            } catch (IOException ex) {
                LOG.error("exception", ex);
            }

        }
        return false;
    }

    /**
     * <p>isValidRRDDomainDir</p>
     *
     * @param path
     * @return a boolean.
     */
    public static final boolean isValidRRDDomainDir(final Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        try {
            // if the directory name is an integer
            Integer.parseInt(path.getFileName().toString());
        } catch (final Throwable e) {

            // if the domain dir contains queryable interface directories, then
            // it is queryable
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, INTERFACE_DIRECTORY_FILTER);) {
                List<Path> dirs = new ArrayList<>();
                for (Path entry : stream) {
                    dirs.add(entry);
                }
                if (!dirs.isEmpty()) {
                    for (Path intf : dirs) {
                        try {
                            // if the interface directory name is an integer (Long)
                            Long.valueOf(intf.getFileName().toString());
                        } catch (final NumberFormatException ee) {
                            return true;
                        }
                    }
                }
                return false;
            } catch (IOException ex) {
                LOG.error("exception", ex);
            }
        }
        return false;
    }

    /**
     * <p>isValidRRDInterfaceDir</p>
     *
     * @param path
     * @return a boolean.
     */
    public static final boolean isValidRRDInterfaceDir(final Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, RRD_FILENAME_FILTER);) {
            List<Path> intfRRDs = new ArrayList<>();
            for (Path entry : stream) {
                intfRRDs.add(entry);
            }
            if (intfRRDs.size() > 0) {
                return true;
            }
        } catch (IOException ex) {
            LOG.error("exception", ex);
        }

        return false;
    }

    public static final boolean isValidRRDDomainInterfaceDir(final Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        try {
            // if the interface directory name is an integer (Long) its not part of a domain
            Long.valueOf(path.getFileName().toString());
        } catch (final NumberFormatException ee) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, RRD_FILENAME_FILTER);) {
                List<Path> nodeRRDs = new ArrayList<>();
                for (Path entry : stream) {
                    nodeRRDs.add(entry);
                }
                if (nodeRRDs.size() > 0) {
                    return true;
                }
            } catch (IOException ex) {
                LOG.error("exception", ex);
            }
        }

        return false;
    }

    /**
     * Determines if the provided File object represents a valid RRD latency
     * directory.
     *
     * @param file a {@link java.io.File} object.
     * @return a boolean.
     */
    public static final boolean isValidRRDLatencyDir(final Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        // if the directory contains RRDs, then it is queryable
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, RRD_FILENAME_FILTER);) {
            List<Path> nodeRRDs = new ArrayList<>();
            for (Path entry : stream) {
                nodeRRDs.add(entry);
            }
            if (nodeRRDs.size() > 0) {
                return true;
            }
        } catch (IOException ex) {
            LOG.error("exception", ex);
        }

        return false;
    }

    /**
     * Checks an RRD filename to make sure it is of the proper length and does
     * not contain any unexpected charaters.
     *
     * The maximum length is specified by the
     * {@link #MAX_RRD_FILENAME_LENGTH MAX_RRD_FILENAME_LENGTH}constant. The
     * only valid characters are letters (A-Z and a-z), numbers (0-9), dashes
     * (-), dots (.), and underscores (_). These precautions are necessary since
     * the RRD filename is used on the commandline and specified in the graph
     * URL.
     *
     * @param rrd a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean isValidRRDName(final String rrd) {
        if (rrd == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }

        final int length = rrd.length();

        if (length > MAX_RRD_FILENAME_LENGTH) {
            return false;
        }

        // cannot contain references to higher directories for security's sake
        if (rrd.indexOf("..") >= 0) {
            return false;
        }

        for (int i = 0; i < length; i++) {
        	final char c = rrd.charAt(i);

            if (!(('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || (c == '_') || (c == '.') || (c == '-') || (c == '/'))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Note this method will <strong>not </strong> handle references to higher
     * directories ("..").
     *
     * @param rrd a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String convertToValidRrdName(final String rrd) {
        if (rrd == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }

        final StringBuffer buffer = new StringBuffer(rrd);

        // truncate after the max length
        if (rrd.length() > MAX_RRD_FILENAME_LENGTH) {
            buffer.setLength(MAX_RRD_FILENAME_LENGTH - 1);
        }

        final int length = buffer.length();

        for (int i = 0; i < length; i++) {
            char c = buffer.charAt(i);

            if (!(('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || (c == '_') || (c == '.') || (c == '-') || (c == '/'))) {
                buffer.setCharAt(i, '_');
            }
        }

        return buffer.toString();
    }

    public static String escapeForGraphing(final Path path) {
        return escapeForGraphing(path.toString());
    }

    public static String escapeForGraphing(final String path) {
    	final Matcher matcher = GRAPHING_ESCAPE_PATTERN.matcher(path);
    	return matcher.replaceAll("\\\\$1");
    }

    /**
     * <p>getRrdSuffix</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getRrdSuffix() {
        return RrdUtils.getExtension();
    }

}

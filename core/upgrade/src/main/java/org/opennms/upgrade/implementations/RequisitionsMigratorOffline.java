/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013-2014 The OpenNMS Group, Inc.
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

package org.opennms.upgrade.implementations;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.IOUtils;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.core.xml.JaxbUtils;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.upgrade.api.AbstractOnmsUpgrade;
import org.opennms.upgrade.api.OnmsUpgradeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Requisitions Migrator.
 *
 * <p>The attributes <code>non-ip-snmp-primary</code> and <code>non-ip-interfaces</code> which are valid in 1.10 have been removed in 1.12.</p>
 * <p>This tool will parse the raw requisitions XML and remove those tags.</p>
 *
 * <p>Issues fixed:</p>
 * <ul>
 * <li>NMS-5630</li>
 * <li>NMS-5571</li>
 * </ul>
 *
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public class RequisitionsMigratorOffline extends AbstractOnmsUpgrade {

	private static final Logger LOG = LoggerFactory.getLogger(RequisitionsMigratorOffline.class);

    /** The requisition directory. */
    private Path requisitionDir;

    /**
     * Instantiates a new requisitions migrator offline.
     *
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    public RequisitionsMigratorOffline() throws OnmsUpgradeException {
        super();
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#getOrder()
     */
    @Override
    public int getOrder() {
        return 1;
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#getDescription()
     */
    @Override
    public String getDescription() {
        return "Remove non-ip-snmp-primary and non-ip-interfaces from requisitions: NMS-5630, NMS-5571";
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#requiresOnmsRunning()
     */
    @Override
    public boolean requiresOnmsRunning() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#preExecute()
     */
    @Override
    public void preExecute() throws OnmsUpgradeException {
        log("Backing up: %s\n", getRequisitionDir());
        zipDir(getBackupFile(), getRequisitionDir());
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#postExecute()
     */
    @Override
    public void postExecute() throws OnmsUpgradeException {
        Path zip = getBackupFile();
        if (Files.exists(zip)) {
            log("Removing backup %s\n", zip);
            try {
                Files.delete(zip);
            } catch (IOException e) {
                LOG.warn("Could not delete file: {}", zip, e);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#rollback()
     */
    @Override
    public void rollback() throws OnmsUpgradeException {
        Path zip = getBackupFile();
        unzipFile(zip, getRequisitionDir());
        try {
            Files.delete(zip);
        } catch (IOException e) {
            LOG.warn("Could not delete file: {}", zip, e);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#execute()
     */
    @Override
    public void execute() throws OnmsUpgradeException {
        DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) {
                return path.endsWith(".xml");
            }
        };

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(getRequisitionDir(), filter);) {
            for (Path req : stream) {
                log("Processing %s\n", req);

                String content = IOUtils.toString(Files.newInputStream(req), "UTF-8");
                String output = content.replaceAll(" non-ip-(snmp-primary|interfaces)=\"[^\"]+\"", "");
                if (content.length() != output.length()) {
                    log("  Updating and parsing the requisition\n", req);
                    IOUtils.write(output, Files.newOutputStream(req), "UTF-8");
                    Requisition requisition = JaxbUtils.unmarshal(Requisition.class, req, true);
                    if (requisition == null) {
                        throw new OnmsUpgradeException("Can't parse requisition " + req);
                    }
                }
            }
        } catch (Exception e) {
            throw new OnmsUpgradeException("Can't upgrade requisitions because " + e.getMessage(), e);
        }
    }

    /**
     * Gets the requisition directory.
     *
     * @return the requisition directory
     */
    private Path getRequisitionDir() {
        if (requisitionDir == null) {
            requisitionDir = ConfigFileConstants.getFilePathString().resolve("imports");
        }
        return requisitionDir;
    }

    /**
     * Gets the backup file.
     *
     * @return the backup file
     */
    private Path getBackupFile() {
        return getRequisitionDir().resolveSibling(getRequisitionDir().getFileName() + ZIP_EXT);
    }
}

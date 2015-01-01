/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.opennms.upgrade.api.AbstractOnmsUpgrade;
import org.opennms.upgrade.api.OnmsUpgradeException;

/**
 * The Class Jetty Config Migrator.
 *
 * <p>If HTTPS or AJP are enabled in opennms.properties, that requires a special version of jetty.xml on $OPENNMS_HOME/etc in order to work.</p>
 *
 * <p>Issues fixed:</p>
 * <ul>
 * <li>NMS-6629</li>
 * </ul>
 *
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public class JettyConfigMigratorOffline extends AbstractOnmsUpgrade {

    /**
     * Instantiates a new Jetty configuration migrator offline.
     *
     * @throws OnmsUpgradeException the OpenNMS upgrade exception
     */
    public JettyConfigMigratorOffline() throws OnmsUpgradeException {
        super();
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#getOrder()
     */
    @Override
    public int getOrder() {
        return 5;
    }

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#getDescription()
     */
    @Override
    public String getDescription() {
        return "Adds jetty.xml if necessary: NMS-6629";
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
    public void preExecute() throws OnmsUpgradeException {}

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#postExecute()
     */
    @Override
    public void postExecute() throws OnmsUpgradeException {}

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#rollback()
     */
    @Override
    public void rollback() throws OnmsUpgradeException {}

    /* (non-Javadoc)
     * @see org.opennms.upgrade.api.OnmsUpgrade#execute()
     */
    @Override
    public void execute() throws OnmsUpgradeException {
        String jettySSL = getMainProperties().getProperty("org.opennms.netmgt.jetty.https-port", null);
        String jettyAJP = getMainProperties().getProperty("org.opennms.netmgt.jetty.ajp-port", null);
        boolean sslWasFixed = false;
        boolean ajpWasFixed = false;
        try {
            log("SSL Enabled ? %s\n", jettySSL != null);
            log("AJP Enabled ? %s\n", jettyAJP != null);
            if (jettySSL != null || jettyAJP != null) {
                Path jettyXmlExample = getHomeDirectory().resolve("etc").resolve("examples").resolve("jetty.xml");
                Path jettyXml = getHomeDirectory().resolve("etc").resolve("jetty.xml");

                if (!Files.exists(jettyXml) && !Files.exists(jettyXmlExample)) {
                    throw new FileNotFoundException("The required file doesn't exist: " + jettyXmlExample);
                }

                if (!Files.exists(jettyXml)) {
                    log("Copying %s into %s\n", jettyXmlExample, jettyXml);
                    Files.copy(jettyXmlExample, jettyXml);
                }

                log("Creating %s\n", jettyXml);
                Path tempFile = jettyXml.resolveSibling("jetty.xml.tmp");

                try (BufferedWriter writer = Files.newBufferedWriter(tempFile, Charset.defaultCharset()); BufferedReader reader = Files.newBufferedReader(jettyXmlExample, Charset.defaultCharset());) {

                    boolean startSsl = false;
                    boolean startAjp = false;
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    if (startAjp) {
                        if (line.matches("^\\s+[<][!]--\\s*$")) {
                            continue;
                        }
                        if (line.matches("^\\s+--[>]\\s*$")) {
                            startAjp = false;
                            ajpWasFixed = true;
                            continue;
                        }
                    }
                    if (startSsl) {
                        if (line.matches("^\\s+[<][!]--\\s*$")) {
                            continue;
                        }
                        if (line.matches("^\\s+--[>]\\s*$")) {
                            startSsl = false;
                            sslWasFixed = true;
                            continue;
                        }
                    }
                        writer.write(line + "\n");
                    if (startAjp == false && line.contains("<!-- Add AJP support -->") && jettyAJP != null) {
                        startAjp = true;
                        log("Enabling AjpConnector\n");
                    }
                    if (startSsl == false && line.contains("<!-- Add HTTPS support -->") && jettySSL != null) {
                        startSsl = true;
                        log("Enabling SslSelectChannelConnector\n");
                    }
                }
                }
                Files.copy(tempFile, jettyXml);
                Files.delete(tempFile);

            } else {
                log("Neither SSL nor AJP are enabled.\n");
            }
        } catch (Exception e) {
            throw new OnmsUpgradeException("Can't fix Jetty configuration because " + e.getMessage(), e);
        }
        if (jettyAJP != null && !ajpWasFixed) {
            throw new OnmsUpgradeException("Can't enable APJ, please manually edit jetty.xml and uncomment the section where org.eclipse.jetty.ajp.Ajp13SocketConnector is defined.");
        }
        if (jettySSL != null && !sslWasFixed) {
            throw new OnmsUpgradeException("Can't enable SSL, please manually edit jetty.xml and uncomment the section where org.eclipse.jetty.server.ssl.SslSelectChannelConnector is defined.");
        }
    }

}

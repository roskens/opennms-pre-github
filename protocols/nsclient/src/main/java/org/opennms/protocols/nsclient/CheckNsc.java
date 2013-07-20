/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.protocols.nsclient;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * This is an example commandline tool to perform checks against NSClient
 * services using <code>NsclientManager</code>.
 *
 * @author <a href="mailto:matt.raykowski@gmail.com">Matt Raykowski</a>
 * @author <a href="http://www.opennms.org">OpenNMS</a>
 */
public class CheckNsc {

    /**
     * <p>
     * main
     * </p>
     * .
     *
     * @param args
     *            an array of {@link java.lang.String} objects.
     * @throws ParseException
     *             the parse exception
     */
    public static void main(String[] args) throws ParseException {

        Options options = new Options();
        options.addOption("port", true, "the port to connect to");
        options.addOption("password", true, "the password to use when connecting");
        options.addOption("warning", true, "treat the response as a warning if the level is above this value");
        options.addOption("critical", true, "treat the response as a critical error if the level is above this value");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);

        @SuppressWarnings("unchecked")
        List<String> arguments = cmd.getArgList();
        if (arguments.size() < 2) {
            usage(options, cmd);
            System.exit(1);
        }

        NsclientManager client = null;
        NsclientPacket response = null;
        NsclientCheckParams params = null;

        String host = arguments.remove(0);
        String command = arguments.remove(0);
        int warningLevel = 0;
        int criticalLevel = 0;
        int port = 1248;

        if (cmd.hasOption("warning")) {
            warningLevel = Integer.parseInt(cmd.getOptionValue("warning"));
        }
        if (cmd.hasOption("critical")) {
            criticalLevel = Integer.parseInt(cmd.getOptionValue("critical"));
        }
        if (cmd.hasOption("port")) {
            port = Integer.parseInt(cmd.getOptionValue("port"));
        }

        /* whatever's left gets merged into "arg1&arg2&arg3" */
        StringBuffer clientParams = new StringBuffer();
        if (!arguments.isEmpty()) {
            for (Iterator<String> i = arguments.iterator(); i.hasNext();) {
                clientParams.append(i.next());
                if (i.hasNext()) {
                    clientParams.append("&");
                }
            }
        }

        try {
            client = new NsclientManager(host, port);
        } catch (Throwable e) {
            usage(options, cmd, "An error occurred creating a new NsclientManager.", e);
        }

        if (cmd.hasOption("password")) {
            client.setPassword(cmd.getOptionValue("password"));
        }

        try {
            client.setTimeout(5000);
            client.init();
        } catch (Throwable e) {
            usage(options, cmd, "An error occurred initializing the NsclientManager.", e);
        }

        try {
            params = new NsclientCheckParams(warningLevel, criticalLevel, clientParams.toString());
        } catch (Throwable e) {
            usage(options, cmd, "An error occurred creating the parameter object.", e);
        }

        try {
            response = client.processCheckCommand(NsclientManager.convertStringToType(command), params);
        } catch (Throwable e) {
            usage(options, cmd, "An error occurred processing the command.", e);
        }

        if (response == null) {
            usage(options, cmd, "No response was returned.", null);
        } else {
            System.out.println("NsclientPlugin: " + command + ": "
                    + NsclientPacket.convertStateToString(response.getResultCode()) /*
                                                                                     * response
                                                                                     * .
                                                                                     * getResultCode
                                                                                     * (
                                                                                     * )
                                                                                     */
                    + " (" + response.getResponse() + ")");
        }
    }

    /**
     * Usage.
     *
     * @param options
     *            the options
     * @param cmd
     *            the cmd
     * @param error
     *            the error
     * @param e
     *            the e
     */
    private static void usage(Options options, CommandLine cmd, String error, Throwable e) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        if (error != null) {
            pw.println("An error occurred: " + error + "\n");
        }
        formatter.printHelp("usage: CheckNsc [options] host command [arguments]", options);

        if (e != null) {
            pw.println(e.getMessage());
            e.printStackTrace(pw);
        }

        pw.close();
    }

    /**
     * Usage.
     *
     * @param options
     *            the options
     * @param cmd
     *            the cmd
     */
    private static void usage(Options options, CommandLine cmd) {
        usage(options, cmd, null, null);
    }

}

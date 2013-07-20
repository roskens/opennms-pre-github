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

package org.opennms.features.topology.shell;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

/**
 * The Class BlueprintNamespaceShellCommand.
 */
@Command(scope = "onms", name = "listnamespaces", description = "Lists the available blueprint namespaces and their providers.")
public class BlueprintNamespaceShellCommand extends OsgiCommandSupport {

    /* (non-Javadoc)
     * @see org.apache.karaf.shell.console.AbstractAction#doExecute()
     */
    @Override
    protected Object doExecute() throws Exception {

        final Collection<ServiceReference<NamespaceHandler>> services = this.bundleContext.getServiceReferences(NamespaceHandler.class,
                                                                                                                null);
        for (final ServiceReference<NamespaceHandler> sr : services) {
            final Bundle bundle = sr.getBundle();
            final Object rawNamespaces = sr.getProperty("osgi.service.blueprint.namespace");

            final ArrayList<String> namespaces = new ArrayList<String>();
            if (rawNamespaces instanceof String) {
                namespaces.add((String) rawNamespaces);
            } else if (rawNamespaces instanceof Object[]) {
                for (final Object namespace : (Object[]) rawNamespaces) {
                    namespaces.add(namespace.toString());
                }
            } else if (rawNamespaces instanceof String[]) {
                for (final String namespace : (String[]) rawNamespaces) {
                    namespaces.add(namespace);
                }
            } else {
                System.err.println("Hmm, not sure how to interpret: " + rawNamespaces);
            }

            System.out.println(bundle.toString());
            for (final Object namespace : namespaces) {
                System.out.println("    " + namespace);
            }
            System.out.println();
        }
        return null;
    }
}

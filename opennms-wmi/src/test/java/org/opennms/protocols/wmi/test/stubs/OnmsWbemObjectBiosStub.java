/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.protocols.wmi.test.stubs;

import java.util.List;

import org.opennms.protocols.wmi.WmiException;
import org.opennms.protocols.wmi.wbem.OnmsWbemMethodSet;
import org.opennms.protocols.wmi.wbem.OnmsWbemObject;
import org.opennms.protocols.wmi.wbem.OnmsWbemObjectPath;
import org.opennms.protocols.wmi.wbem.OnmsWbemPropertySet;

/**
 * The Class OnmsWbemObjectBiosStub.
 */
public class OnmsWbemObjectBiosStub implements OnmsWbemObject {

    /** The props. */
    public OnmsWbemPropertySet props;

    /**
     * Instantiates a new onms wbem object bios stub.
     *
     * @param propset
     *            the propset
     */
    public OnmsWbemObjectBiosStub(OnmsWbemPropertySet propset) {
        props = propset;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemObject#wmiExecMethod(java.lang.String, java.util.List, java.util.List)
     */
    @Override
    public OnmsWbemObject wmiExecMethod(String methodName, List<?> params, List<?> namedValueSet) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemObject#wmiInstances()
     */
    @Override
    public List<String> wmiInstances() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemObject#wmiPut()
     */
    @Override
    public String wmiPut() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemObject#getWmiMethods()
     */
    @Override
    public OnmsWbemMethodSet getWmiMethods() throws WmiException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemObject#getWmiPath()
     */
    @Override
    public OnmsWbemObjectPath getWmiPath() throws WmiException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemObject#getWmiObjectText()
     */
    @Override
    public String getWmiObjectText() throws WmiException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemObject#getWmiProperties()
     */
    @Override
    public OnmsWbemPropertySet getWmiProperties() throws WmiException {
        return props;
    }
}

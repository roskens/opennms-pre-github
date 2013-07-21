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

import org.opennms.protocols.wmi.WmiException;
import org.opennms.protocols.wmi.wbem.OnmsWbemProperty;

/**
 * The Class OnmsWbemPropBiosStub.
 */
public class OnmsWbemPropBiosStub implements OnmsWbemProperty {

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemProperty#getWmiName()
     */
    @Override
    public String getWmiName() throws WmiException {
        return "ReleaseDate";
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemProperty#getWmiOrigin()
     */
    @Override
    public String getWmiOrigin() throws WmiException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemProperty#getWmiIsArray()
     */
    @Override
    public Boolean getWmiIsArray() throws WmiException {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemProperty#getWmiIsLocal()
     */
    @Override
    public Boolean getWmiIsLocal() throws WmiException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemProperty#getWmiValue()
     */
    @Override
    public Object getWmiValue() throws WmiException {
        return "2/12/2004 00:00:00";
    }

    /* (non-Javadoc)
     * @see org.opennms.protocols.wmi.wbem.OnmsWbemProperty#getWmiCIMType()
     */
    @Override
    public Integer getWmiCIMType() throws WmiException {
        return null;
    }
}

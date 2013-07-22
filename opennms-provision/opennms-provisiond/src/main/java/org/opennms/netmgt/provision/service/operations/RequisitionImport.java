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

package org.opennms.netmgt.provision.service.operations;

import javax.xml.bind.ValidationException;

import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class RequisitionImport.
 */
public class RequisitionImport {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(RequisitionImport.class);

    /** The m_requisition. */
    private Requisition m_requisition;

    /** The m_throwable. */
    private Throwable m_throwable;

    /**
     * Gets the requisition.
     *
     * @return the requisition
     */
    public Requisition getRequisition() {
        return m_requisition;
    }

    /**
     * Sets the requisition.
     *
     * @param requisition
     *            the new requisition
     */
    public void setRequisition(final Requisition requisition) {
        m_requisition = requisition;
        try {
            requisition.validate();
        } catch (final ValidationException e) {
            if (m_throwable == null) {
                m_throwable = e;
            } else {
                LOG.debug("Requisition {} did not validate, but we'll ignore the exception because we've previously aborted with: {}",
                          requisition, m_throwable, e);
            }
        }
    }

    /**
     * Gets the error.
     *
     * @return the error
     */
    public Throwable getError() {
        return m_throwable;
    }

    /**
     * Abort.
     *
     * @param t
     *            the t
     */
    public void abort(final Throwable t) {
        if (m_throwable == null) {
            m_throwable = t;
        } else {
            LOG.warn("Requisition {} has already been aborted, but we received another abort message.  Ignoring.",
                     m_requisition, t);
        }
    }

    /**
     * Checks if is aborted.
     *
     * @return true, if is aborted
     */
    public boolean isAborted() {
        if (m_throwable != null) {
            return true;
        }
        return false;
    }

}

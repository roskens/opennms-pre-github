/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.model;

/**
 * OpenNMS Trouble Ticket State Enumerations.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 * @version $Id: $
 */
public enum TroubleTicketState {

    /** The open. */
    OPEN,
 /** The create pending. */
 CREATE_PENDING,
 /** The create failed. */
 CREATE_FAILED,
 /** The update pending. */
 UPDATE_PENDING,
 /** The update failed. */
 UPDATE_FAILED,
 /** The closed. */
 CLOSED,
 /** The close pending. */
 CLOSE_PENDING,
 /** The close failed. */
 CLOSE_FAILED,
 /** The resolved. */
 RESOLVED,
 /** The resolve pending. */
 RESOLVE_PENDING,
 /** The resolve failed. */
 RESOLVE_FAILED,
 /** The cancelled. */
 CANCELLED,
 /** The cancel pending. */
 CANCEL_PENDING,
 /** The cancel failed. */
 CANCEL_FAILED
}

/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2011 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */

package org.opennms.gwt.web.ui.asset.client.tools.validation;

import org.opennms.gwt.web.ui.asset.client.AssetPageConstants;

import com.google.gwt.core.client.GWT;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 *         Validates given object to be castable to Integer.
 */
public class StringAsIntegerValidator implements Validator {
	private AssetPageConstants con = GWT.create(AssetPageConstants.class);

	/**
	 * Validates if the string representation of given object a valid integer.
	 * Validation will run against
	 * <code>Integer.parseInt(object.toString())</code> so make sure to use a
	 * proper object.
	 * 
	 * @param object
	 */
	@Override
	public String validate(Object object) {
		if (object.toString().equals("")) {
			return "";
		}

		try {
			Integer.parseInt(object.toString());
			return "";
		} catch (Exception e) {
			return con.stringNoValidInteger();
		}
	}
}

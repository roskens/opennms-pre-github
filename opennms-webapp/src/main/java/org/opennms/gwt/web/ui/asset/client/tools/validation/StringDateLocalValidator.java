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
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus
 *         Neumann</a></br> Validates if the string representation of given
 *         object is parseable to an {@link Date}. The expected format is
 *         "yyyy-MM-dd".
 */
public class StringDateLocalValidator implements Validator {
	private AssetPageConstants con = GWT.create(AssetPageConstants.class);

	private final DateTimeFormat m_formater = DateTimeFormat.getFormat("yyyy-MM-dd");

	private String m_dateString = "";

	/**
	 * Validates if the string representation of given object is parseable to an
	 * {@link Date}. The expected format is "yyyy-MM-dd". The given object will
	 * be casted by <code>(String) object</code>.
	 * 
	 * @param object
	 */
	@Override
	public String validate(Object object) {

		try {
			m_dateString = (String) object;
		} catch (Exception e) {
			// GWT.LOG("DATETIMEFORMATVALIDATOR: CAN'T CAST OBJECT: " + OBJECT +
			// " TO STRING");
			// validator can't cast given object to string.
			// it's a STRING-validator... so nothing happens
		}

		if (m_dateString.equals("")) {
			return "";
		}

		try {
			// GWT.log("DateTimeFormatValidator: m_DateSting: " + m_dateString);
			m_formater.parseStrict(m_dateString);
		} catch (Exception e) {
			// GWT.log("DateTimeFormatValidator: m_DateSting: " + m_dateString +
			// " can't be formated by m_formater; " + "yyyy-MM-dd");
			return con.stringNotADate();
		}
		return "";
	}
}

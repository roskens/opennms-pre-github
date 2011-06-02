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

package org.opennms.gwt.web.ui.asset.client.tools.fieldsets;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusHandler;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a> A
 *         FieldSet is a pair of a describing label/name and a value/input. It's
 *         intended to make data-input and data-maintenance pages easier. It's
 *         string based.
 */
public interface FieldSet extends FocusHandler, ChangeHandler {

	/**
	 * Clears the status changed from a {@link FieldSet}.
	 */
	public abstract void clearChanged();

	/**
	 * clears all error strings from a {@link FieldSet}.
	 */
	public abstract void clearErrors();

	/**
	 * clears all warning strings from a {@link FieldSet}.
	 */
	public abstract void clearWarnings();

	/**
	 * @return boolean enabled if writing/changes are allowed/active.
	 */
	public abstract Boolean getEnabled();

	/**
	 * Get the complete error string for the {@link FieldSet}.
	 * 
	 * @return String error
	 */
	public abstract String getError();

	/**
	 * Get the description/label text of the {@link FieldSet}.
	 * 
	 * @return String label
	 */
	public abstract String getLabel();

	/**
	 * @return actual value of {@link FieldSet}.
	 */
	public abstract String getValue();

	/**
	 * Get the complete warning string for the {@link FieldSet}.
	 * 
	 * @return String warning
	 */
	public abstract String getWarning();

	/**
	 * Set the {@link FieldSet} into write/write-protected mode.
	 * 
	 * @param enabled
	 *            to get write-mode disable to get write-protected mode
	 */
	public abstract void setEnabled(Boolean enabled);

	/**
	 * Set a error string to the {@link FieldSet}.
	 * 
	 * @param error
	 */
	public abstract void setError(String error);

	/**
	 * Sets a text into the description/label of the {@link FieldSet}.
	 * 
	 * @param label
	 */
	public abstract void setLabel(String label);

	/**
	 * Sets a value into the value/input of the {@link FieldSet}.
	 * 
	 * @param value
	 */
	public abstract void setValue(String value);

	/**
	 * Set a warning string to the {@link FieldSet}.
	 * 
	 * @param warning
	 */
	public abstract void setWarning(String warning);
}

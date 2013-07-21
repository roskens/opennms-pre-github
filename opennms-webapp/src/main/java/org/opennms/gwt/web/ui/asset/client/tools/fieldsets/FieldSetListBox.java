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

package org.opennms.gwt.web.ui.asset.client.tools.fieldsets;

import java.util.ArrayList;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.ListBox;

/**
 * The Class FieldSetListBox.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus
 *         Neumann</a><br/>
 *         {@link FieldSet} for displaying and selection values
 *         from a list.
 */
public class FieldSetListBox extends AbstractFieldSet implements FieldSet {

    /** The list box. */
    private ListBox listBox = new ListBox(false);

    /** The options. */
    private ArrayList<String> options;

    /**
     * Instantiates a new field set list box.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param helpText
     *            the help text
     */
    @UiConstructor
    public FieldSetListBox(String name, String value, String helpText) {
        super(name, helpText);
        init(value, null);
    }

    /**
     * Instantiates a new field set list box.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param helpText
     *            the help text
     * @param options
     *            the options
     */
    public FieldSetListBox(String name, String value, String helpText, ArrayList<String> options) {
        super(name, helpText);
        init(value, options);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#getValue()
     */
    @Override
    public String getValue() {
        return listBox.getItemText(listBox.getSelectedIndex());
    }

    /**
     * Inits the.
     *
     * @param value
     *            the value
     * @param options
     *            the options
     */
    private void init(String value, ArrayList<String> options) {
        inititalValue = value;

        this.options = options;

        if (options != null) {
            for (String string : options) {
                listBox.addItem(string);
            }
            if (options.contains(value)) {
                listBox.setSelectedIndex(options.indexOf(value));
            } else {
                listBox.addItem(value);
                listBox.setSelectedIndex(options.size());
            }
        }

        if (options == null) {
            listBox.addItem(value);
            listBox.setSelectedIndex(0);
        }

        listBox.setVisibleItemCount(1);
        listBox.addChangeHandler(this);
        listBox.setStyleName("listBox");

        listBox.setSize("300px", "18px");

        panel.add(listBox);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setEnabled(java.lang.Boolean)
     */
    @Override
    public void setEnabled(Boolean enabled) {
        listBox.setEnabled(enabled);
    }

    /**
     * Takes a ArraList of Strings as options. Options will be shown at the
     * list.
     *
     * @param options
     *            the new options
     */
    public void setOptions(ArrayList<String> options) {
        this.options = options;
        listBox.clear();
        for (String string : options) {
            listBox.addItem(string);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        if (options.contains(value)) {
            listBox.setSelectedIndex(options.indexOf(value));
        } else {
            listBox.addItem(value);
            listBox.getItemCount();
            listBox.setSelectedIndex(listBox.getItemCount() - 1);
        }
        inititalValue = value;
        validate(this.getValue());
    }
}

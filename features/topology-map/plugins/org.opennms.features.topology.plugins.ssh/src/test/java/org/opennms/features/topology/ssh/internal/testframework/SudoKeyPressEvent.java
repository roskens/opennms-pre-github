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

package org.opennms.features.topology.ssh.internal.testframework;

import com.google.gwt.event.dom.client.KeyPressEvent;

/**
 * The Class SudoKeyPressEvent.
 */
public class SudoKeyPressEvent extends KeyPressEvent {

    /** The char code. */
    private int charCode;

    /** The is ctrl down. */
    private boolean isCtrlDown;

    /** The is alt down. */
    private boolean isAltDown;

    /** The is shift down. */
    private boolean isShiftDown;

    /**
     * Instantiates a new sudo key press event.
     *
     * @param k
     *            the k
     * @param isCtrlDown
     *            the is ctrl down
     * @param isAltDown
     *            the is alt down
     * @param isShiftDown
     *            the is shift down
     */
    public SudoKeyPressEvent(int k, boolean isCtrlDown, boolean isAltDown, boolean isShiftDown) {
        charCode = k;
        this.isCtrlDown = isCtrlDown;
        this.isAltDown = isAltDown;
        this.isShiftDown = isShiftDown;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.KeyPressEvent#getUnicodeCharCode()
     */
    @Override
    public int getUnicodeCharCode() {
        return charCode;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.KeyEvent#isControlKeyDown()
     */
    @Override
    public boolean isControlKeyDown() {
        return isCtrlDown;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.KeyEvent#isAltKeyDown()
     */
    @Override
    public boolean isAltKeyDown() {
        return isAltDown;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.KeyEvent#isShiftKeyDown()
     */
    @Override
    public boolean isShiftKeyDown() {
        return isShiftDown;
    }
}

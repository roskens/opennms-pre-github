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

package org.opennms.core.utils;

/**
 * <p>
 * CommandBeanMockup class.
 * </p>
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 */
public class CommandBeanMockup {

    /** The script. */
    String script = "<script>foo</script>";

    /** The html table. */
    String htmlTable = "<table>";

    /** The number. */
    int number = 1;

    /** The cool. */
    Boolean cool = false;

    /**
     * Gets the script.
     *
     * @return the script
     */
    public String getScript() {
        return script;
    }

    /**
     * Sets the script.
     *
     * @param script
     *            the new script
     */
    public void setScript(String script) {
        this.script = script;
    }

    /**
     * Gets the html table.
     *
     * @return the html table
     */
    public String getHtmlTable() {
        return htmlTable;
    }

    /**
     * Sets the html table.
     *
     * @param htmlTable
     *            the new html table
     */
    public void setHtmlTable(String htmlTable) {
        this.htmlTable = htmlTable;
    }

    /**
     * Gets the number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the number.
     *
     * @param number
     *            the new number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Gets the cool.
     *
     * @return the cool
     */
    public Boolean getCool() {
        return cool;
    }

    /**
     * Sets the cool.
     *
     * @param cool
     *            the new cool
     */
    public void setCool(Boolean cool) {
        this.cool = cool;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CommandBean [script=" + script + ", htmlTable=" + htmlTable + ", number=" + number + ", cool=" + cool
                + "]";
    }
}

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
package org.opennms.mock.snmp.responder;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Variable;

/**
 * The Class Sleeper.
 */
public class Sleeper implements DynamicVariable {

    /** The m_instance. */
    private static Sleeper m_instance = null;

    /** The m_millis to sleep. */
    private long m_millisToSleep;

    /** The m_variable. */
    private Variable m_variable;

    /**
     * Instantiates a new sleeper.
     */
    private Sleeper() {
        resetWithVariable(new Integer32(0));
    }

    /**
     * Gets the single instance of Sleeper.
     *
     * @return single instance of Sleeper
     */
    public static synchronized Sleeper getInstance() {
        if (m_instance == null) {
            m_instance = new Sleeper();
        }
        return m_instance;
    }

    /**
     * Reset with variable.
     *
     * @param var
     *            the var
     */
    public void resetWithVariable(Variable var) {
        m_millisToSleep = 0;
        m_variable = var;
    }

    /**
     * Gets the sleep time.
     *
     * @return the sleep time
     */
    public long getSleepTime() {
        return m_millisToSleep;
    }

    /**
     * Sets the sleep time.
     *
     * @param millis
     *            the new sleep time
     */
    public void setSleepTime(long millis) {
        m_millisToSleep = millis;
    }

    /**
     * Gets the variable.
     *
     * @return the variable
     */
    public Variable getVariable() {
        return m_variable;
    }

    /**
     * Sets the variable.
     *
     * @param var
     *            the new variable
     */
    public void setVariable(Variable var) {
        m_variable = var;
    }

    /* (non-Javadoc)
     * @see org.opennms.mock.snmp.responder.DynamicVariable#getVariableForOID(java.lang.String)
     */
    @Override
    public Variable getVariableForOID(String oidStr) {
        sleep(m_millisToSleep);
        return m_variable;
    }

    /**
     * Sleep.
     *
     * @param millis
     *            the millis
     */
    private void sleep(long millis) {
        if (millis == 0)
            return;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

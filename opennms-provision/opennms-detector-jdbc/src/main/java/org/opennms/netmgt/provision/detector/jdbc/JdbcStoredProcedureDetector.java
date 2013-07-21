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

package org.opennms.netmgt.provision.detector.jdbc;

import org.opennms.netmgt.provision.detector.jdbc.request.JDBCRequest;
import org.opennms.netmgt.provision.detector.jdbc.response.JDBCResponse;
import org.opennms.netmgt.provision.support.RequestBuilder;
import org.opennms.netmgt.provision.support.ResponseValidator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class JdbcStoredProcedureDetector.
 */
@Component
/**
 * <p>JdbcStoredProcedureDetector class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
@Scope("prototype")
public class JdbcStoredProcedureDetector extends AbstractJdbcDetector {

    /** The Constant DEFAULT_STORED_PROCEDURE. */
    private static final String DEFAULT_STORED_PROCEDURE = "isRunning";

    /** The Constant DEFAULT_SCHEMA. */
    private static final String DEFAULT_SCHEMA = "test";

    /** The m_stored procedure. */
    private String m_storedProcedure;

    /** The m_schema. */
    private String m_schema = "test";

    /**
     * <p>
     * Constructor for JdbcStoredProcedureDetector.
     * </p>
     */
    protected JdbcStoredProcedureDetector() {
        super("JdbcStoredProcedureDetector", 3306);
        setSchema(DEFAULT_SCHEMA);
        setStoredProcedure(DEFAULT_STORED_PROCEDURE);
    }

    /** {@inheritDoc} */
    @Override
    protected void onInit() {
        expectBanner(resultSetNotNull());
        send(storedProcedure(createProcedureCall(getSchema(), getStoredProcedure())), isValidProcedureCall());
    }

    /**
     * Checks if is valid procedure call.
     *
     * @return the response validator
     */
    private static ResponseValidator<JDBCResponse> isValidProcedureCall() {
        return new ResponseValidator<JDBCResponse>() {

            @Override
            public boolean validate(JDBCResponse response) {
                return response.isValidProcedureCall();
            }

        };
    }

    /**
     * Stored procedure.
     *
     * @param storedProcedure
     *            the stored procedure
     * @return the request builder
     */
    private static RequestBuilder<JDBCRequest> storedProcedure(final String storedProcedure) {
        return new RequestBuilder<JDBCRequest>() {

            @Override
            public JDBCRequest getRequest() {
                JDBCRequest request = new JDBCRequest();
                request.setStoredProcedure(storedProcedure);
                return request;
            }

        };
    }

    /**
     * Creates the procedure call.
     *
     * @param schema
     *            the schema
     * @param procedure
     *            the procedure
     * @return the string
     */
    private String createProcedureCall(String schema, String procedure) {
        if (schema != null) {
            return String.format("%s.%s", schema, procedure);
        } else {
            return procedure;
        }
    }

    /**
     * <p>
     * setStoredProcedure
     * </p>
     * .
     *
     * @param storedProcedure
     *            a {@link java.lang.String} object.
     */
    public void setStoredProcedure(String storedProcedure) {
        m_storedProcedure = storedProcedure;
    }

    /**
     * <p>
     * getStoredProcedure
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStoredProcedure() {
        return m_storedProcedure;
    }

    /**
     * <p>
     * setSchema
     * </p>
     * .
     *
     * @param schema
     *            a {@link java.lang.String} object.
     */
    public void setSchema(String schema) {
        m_schema = schema;
    }

    /**
     * <p>
     * getSchema
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSchema() {
        return m_schema;
    }

}

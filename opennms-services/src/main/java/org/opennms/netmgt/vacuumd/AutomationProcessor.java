/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.vacuumd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opennms.core.utils.PropertiesUtils;
import org.opennms.core.utils.PropertiesUtils.SymbolTable;
import org.opennms.netmgt.config.VacuumdConfigFactory;
import org.opennms.netmgt.config.vacuumd.Action;
import org.opennms.netmgt.config.vacuumd.ActionEvent;
import org.opennms.netmgt.config.vacuumd.Assignment;
import org.opennms.netmgt.config.vacuumd.AutoEvent;
import org.opennms.netmgt.config.vacuumd.Automation;
import org.opennms.netmgt.config.vacuumd.Trigger;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.Parameter;
import org.opennms.netmgt.scheduler.ReadyRunnable;
import org.opennms.netmgt.scheduler.Schedule;
import org.opennms.netmgt.xml.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class used to process automations configured in
 * the vacuumd-configuration.xml file. Automations are
 * identified by a name and they reference
 * Triggers and Actions by name, as well. Autmations also
 * have an interval attribute that determines how often
 * they run.
 *
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 * @version $Id: $
 */
public class AutomationProcessor implements ReadyRunnable {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AutomationProcessor.class);

    /** The m_automation. */
    private final Automation m_automation;

    /** The m_trigger. */
    private final TriggerProcessor m_trigger;

    /** The m_action. */
    private final ActionProcessor m_action;

    /**
     * The m_auto event. @deprecated Associate {@link Automation} objects with
     * {@link ActionEvent} instances instead.
     */
    private final AutoEventProcessor m_autoEvent;

    /** The m_action event. */
    private final ActionEventProcessor m_actionEvent;

    /** The m_schedule. */
    private volatile Schedule m_schedule;

    /** The m_ready. */
    private volatile boolean m_ready = false;

    /**
     * The Class TriggerProcessor.
     */
    static class TriggerProcessor {

        /** The Constant LOG. */
        private static final Logger LOG = LoggerFactory.getLogger(TriggerProcessor.class);

        /** The m_trigger. */
        private final Trigger m_trigger;

        /**
         * Instantiates a new trigger processor.
         *
         * @param automationName
         *            the automation name
         * @param trigger
         *            the trigger
         */
        public TriggerProcessor(String automationName, Trigger trigger) {
            m_trigger = trigger;
        }

        /**
         * Gets the trigger.
         *
         * @return the trigger
         */
        public Trigger getTrigger() {
            return m_trigger;
        }

        /**
         * Checks for trigger.
         *
         * @return true, if successful
         */
        public boolean hasTrigger() {
            return m_trigger != null;
        }

        /**
         * Gets the trigger sql.
         *
         * @return the trigger sql
         */
        public String getTriggerSQL() {
            if (hasTrigger()) {
                return getTrigger().getStatement().getContent();
            } else {
                return null;
            }
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return getTrigger().getName();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return m_trigger == null ? "<No-Trigger>" : m_trigger.getName();
        }

        /**
         * Run trigger query.
         *
         * @return the result set
         * @throws SQLException
         *             the sQL exception
         */
        ResultSet runTriggerQuery() throws SQLException {
            try {
                if (!hasTrigger()) {
                    return null;
                }

                Connection conn = Transaction.getConnection(m_trigger.getDataSource());

                Statement triggerStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                  ResultSet.CONCUR_READ_ONLY);
                Transaction.register(triggerStatement);

                ResultSet triggerResultSet = triggerStatement.executeQuery(getTriggerSQL());
                Transaction.register(triggerResultSet);

                return triggerResultSet;
            } catch (SQLException e) {
                LOG.warn("Error executing trigger {}", getName(), e);
                throw e;
            }
        }

        /**
         * This method verifies that the number of rows in the result set of the
         * trigger
         * match the defined operation in the config. For example, if the user
         * has specified
         * that the trigger-rows = 5 and the operator ">", the automation will
         * only run
         * if the result rows is greater than 5.
         *
         * @param trigRowCount
         *            the trig row count
         * @param trigOp
         *            the trig op
         * @param resultRows
         *            the result rows
         * @return true, if successful
         */
        public boolean triggerRowCheck(int trigRowCount, String trigOp, int resultRows) {

            if (trigRowCount == 0 || trigOp == null) {
                LOG.debug("triggerRowCheck: trigger has no row-count restrictions: operator is: {}, row-count is: {}",
                          trigOp, trigRowCount);
                return true;
            }

            LOG.debug("triggerRowCheck: Verifying trigger resulting row count {} is {} {}", resultRows, trigOp,
                      trigRowCount);

            boolean runAction = false;
            if ("<".equals(trigOp)) {
                if (resultRows < trigRowCount)
                    runAction = true;

            } else if ("<=".equals(trigOp)) {
                if (resultRows <= trigRowCount)
                    runAction = true;

            } else if ("=".equals(trigOp)) {
                if (resultRows == trigRowCount)
                    runAction = true;

            } else if (">=".equals(trigOp)) {
                if (resultRows >= trigRowCount)
                    runAction = true;

            } else if (">".equals(trigOp)) {
                if (resultRows > trigRowCount)
                    runAction = true;

            }

            LOG.debug("Row count verification is: {}", runAction);

            return runAction;
        }

    }

    /**
     * The Class TriggerResults.
     */
    static class TriggerResults {

        /** The Constant LOG. */
        @SuppressWarnings("unused")
        private static final Logger LOG = LoggerFactory.getLogger(TriggerResults.class);

        /** The m_trigger. */
        private final TriggerProcessor m_trigger;

        /** The m_result set. */
        private final ResultSet m_resultSet;

        /** The m_successful. */
        private final boolean m_successful;

        /**
         * Instantiates a new trigger results.
         *
         * @param trigger
         *            the trigger
         * @param set
         *            the set
         * @param successful
         *            the successful
         */
        public TriggerResults(TriggerProcessor trigger, ResultSet set, boolean successful) {
            m_trigger = trigger;
            m_resultSet = set;
            m_successful = successful;
        }

        /**
         * Checks for trigger.
         *
         * @return true, if successful
         */
        public boolean hasTrigger() {
            return m_trigger.hasTrigger();
        }

        /**
         * Gets the result set.
         *
         * @return the result set
         */
        public ResultSet getResultSet() {
            return m_resultSet;
        }

        /**
         * Checks if is successful.
         *
         * @return true, if is successful
         */
        public boolean isSuccessful() {
            return m_successful;
        }

    }

    /**
     * The Class ActionProcessor.
     */
    static class ActionProcessor {

        /** The Constant LOG. */
        private static final Logger LOG = LoggerFactory.getLogger(ActionProcessor.class);

        /** The m_automation name. */
        private final String m_automationName;

        /** The m_action. */
        private final Action m_action;

        /**
         * Instantiates a new action processor.
         *
         * @param automationName
         *            the automation name
         * @param action
         *            the action
         */
        public ActionProcessor(String automationName, Action action) {
            m_automationName = automationName;
            m_action = action;
        }

        /**
         * Checks for action.
         *
         * @return true, if successful
         */
        public boolean hasAction() {
            return m_action != null;
        }

        /**
         * Gets the action.
         *
         * @return the action
         */
        public Action getAction() {
            return m_action;
        }

        /**
         * Gets the action sql.
         *
         * @return the action sql
         */
        String getActionSQL() {
            return getAction().getStatement().getContent();
        }

        /**
         * Creates the prepared statement.
         *
         * @return the prepared statement
         * @throws SQLException
         *             the sQL exception
         */
        PreparedStatement createPreparedStatement() throws SQLException {
            String actionJDBC = getActionSQL().replaceAll("\\$\\{\\w+\\}", "?");

            LOG.debug("createPrepareStatement: This action SQL: {}\nTurned into this: {}", getActionSQL(), actionJDBC);

            Connection conn = Transaction.getConnection(m_action.getDataSource());
            PreparedStatement stmt = conn.prepareStatement(actionJDBC);
            Transaction.register(stmt);
            return stmt;
        }

        /**
         * Returns an ArrayList containing the names of column defined
         * as tokens in the action statement defined in the config. If no
         * tokens are found, an empty list is returned.
         *
         * @return the action columns
         */
        public List<String> getActionColumns() {
            return getTokenizedColumns(getActionSQL());
        }

        /**
         * Gets the tokenized columns.
         *
         * @param targetString
         *            the target string
         * @return the tokenized columns
         */
        private List<String> getTokenizedColumns(String targetString) {
            // The \w represents a "word" charactor
            String expression = "\\$\\{(\\w+)\\}";
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(targetString);

            LOG.debug("getTokenizedColumns: processing string: {}", targetString);

            List<String> tokens = new ArrayList<String>();
            int count = 0;
            while (matcher.find()) {
                count++;
                LOG.debug("getTokenizedColumns: Token {}: {}", count, matcher.group(1));

                tokens.add(matcher.group(1));
            }
            return tokens;
        }

        /**
         * Assign statement parameters.
         *
         * @param stmt
         *            the stmt
         * @param rs
         *            the rs
         * @throws SQLException
         *             the sQL exception
         */
        void assignStatementParameters(PreparedStatement stmt, ResultSet rs) throws SQLException {
            List<String> actionColumns = getTokenizedColumns(getActionSQL());
            Iterator<String> it = actionColumns.iterator();
            String actionColumnName = null;
            int i = 0;
            while (it.hasNext()) {
                actionColumnName = (String) it.next();
                stmt.setObject(++i, rs.getObject(actionColumnName));
            }

        }

        /**
         * Counts the number of tokens in an Action Statement.
         *
         * @param targetString
         *            the target string
         * @return the token count
         */
        public int getTokenCount(String targetString) {
            // The \w represents a "word" charactor
            String expression = "(\\$\\{\\w+\\})";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(targetString);

            LOG.debug("getTokenCount: processing string: {}", targetString);

            int count = 0;
            while (matcher.find()) {
                count++;
                LOG.debug("getTokenCount: Token {}: {}", count, matcher.group(1));
            }
            return count;
        }

        /**
         * Execute.
         *
         * @return true, if successful
         * @throws SQLException
         *             the sQL exception
         */
        boolean execute() throws SQLException {
            // No trigger defined, just running the action.
            if (getTokenCount(getActionSQL()) != 0) {
                LOG.info("execute: not running action: {}.  Action contains tokens in an automation ({}) with no trigger.",
                         m_action.getName(), m_automationName);
                return false;
            } else {
                // Convert the sql to a PreparedStatement
                PreparedStatement actionStatement = createPreparedStatement();
                actionStatement.executeUpdate();
                return true;
            }
        }

        /**
         * Process trigger results.
         *
         * @param triggerResults
         *            the trigger results
         * @return true, if successful
         * @throws SQLException
         *             the sQL exception
         */
        boolean processTriggerResults(TriggerResults triggerResults) throws SQLException {
            ResultSet triggerResultSet = triggerResults.getResultSet();

            triggerResultSet.beforeFirst();

            PreparedStatement actionStatement = createPreparedStatement();

            // Loop through the select results
            while (triggerResultSet.next()) {
                // Convert the sql to a PreparedStatement
                assignStatementParameters(actionStatement, triggerResultSet);
                actionStatement.executeUpdate();
            }

            return true;
        }

        /**
         * Process action.
         *
         * @param triggerResults
         *            the trigger results
         * @return true, if successful
         * @throws SQLException
         *             the sQL exception
         */
        boolean processAction(TriggerResults triggerResults) throws SQLException {
            if (triggerResults.hasTrigger()) {
                return processTriggerResults(triggerResults);
            } else {
                return execute();
            }
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return m_action.getName();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return m_action.getName();
        }

        /**
         * Check for required columns.
         *
         * @param triggerResults
         *            the trigger results
         */
        public void checkForRequiredColumns(TriggerResults triggerResults) {
            ResultSet triggerResultSet = triggerResults.getResultSet();
            if (!resultSetHasRequiredActionColumns(triggerResultSet, getActionColumns())) {
                throw new AutomationException("Action " + this + " uses column not defined in trigger: "
                        + triggerResults);
            }
        }

        /**
         * Helper method that verifies tokens in a config defined action
         * are available in the ResultSet of the paired trigger.
         *
         * @param rs
         *            the rs
         * @param actionColumns
         *            TODO
         * @return true, if successful
         */
        public boolean resultSetHasRequiredActionColumns(ResultSet rs, Collection<String> actionColumns) {

            LOG.debug("resultSetHasRequiredActionColumns: Verifying required action columns in trigger ResultSet...");

            if (actionColumns.isEmpty()) {
                return true;
            }

            if (rs == null) {
                return false;
            }

            boolean verified = true;
            String actionColumnName = null;

            Iterator<String> it = actionColumns.iterator();

            while (it.hasNext()) {
                actionColumnName = (String) it.next();
                try {
                    if (rs.findColumn(actionColumnName) > 0) {
                    }
                } catch (SQLException e) {
                    LOG.warn("resultSetHasRequiredActionColumns: Trigger ResultSet does NOT have required action columns.  Missing: {}",
                             actionColumnName);
                    LOG.warn(e.getMessage());
                    verified = false;
                }
            }

            return verified;
        }

    }

    /**
     * The Class AutoEventProcessor.
     *
     * @deprecated Use {@link ActionEventProcessor} instead.
     */
    static class AutoEventProcessor {

        /** The Constant LOG. */
        private static final Logger LOG = LoggerFactory.getLogger(ActionProcessor.class);

        /** The m_automation name. */
        private final String m_automationName;

        /** The m_auto event. */
        private final AutoEvent m_autoEvent;

        /**
         * Instantiates a new auto event processor.
         *
         * @param automationName
         *            the automation name
         * @param autoEvent
         *            the auto event
         * @deprecated Use {@link ActionEventProcessor} instead.
         */
        public AutoEventProcessor(String automationName, AutoEvent autoEvent) {
            m_automationName = automationName;
            m_autoEvent = autoEvent;
        }

        /**
         * Checks for event.
         *
         * @return true, if successful
         */
        public boolean hasEvent() {
            return m_autoEvent != null;
        }

        /**
         * Gets the auto event.
         *
         * @return the auto event
         */
        public AutoEvent getAutoEvent() {
            return m_autoEvent;
        }

        /**
         * Gets the uei.
         *
         * @return the uei
         */
        String getUei() {
            if (hasEvent()) {
                return getAutoEvent().getUei().getContent();
            } else {
                return null;
            }
        }

        /**
         * Send.
         */
        void send() {

            if (hasEvent()) {
                // create and send event
                LOG.debug("AutoEventProcessor: Sending auto-event {} for automation {}", getUei(), m_automationName);

                EventBuilder bldr = new EventBuilder(getUei(), "Automation");
                sendEvent(bldr.getEvent());
            } else {
                LOG.debug("AutoEventProcessor: No auto-event for automation {}", m_automationName);
            }
        }

        /**
         * Send event.
         *
         * @param event
         *            the event
         */
        private void sendEvent(Event event) {
            Vacuumd.getSingleton().getEventManager().sendNow(event);
        }

    }

    /**
     * The Class SQLExceptionHolder.
     */
    static class SQLExceptionHolder extends RuntimeException {

        /** The Constant LOG. */
        @SuppressWarnings("unused")
        private static final Logger LOG = LoggerFactory.getLogger(SQLExceptionHolder.class);

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 2479066089399740468L;

        /** The m_ex. */
        private final SQLException m_ex;

        /**
         * Instantiates a new sQL exception holder.
         *
         * @param ex
         *            the ex
         */
        public SQLExceptionHolder(SQLException ex) {
            m_ex = ex;
        }

        /**
         * Rethrow.
         *
         * @throws SQLException
         *             the sQL exception
         */
        public void rethrow() throws SQLException {
            if (m_ex != null) {
                throw m_ex;
            }
        }
    }

    /**
     * The Class ResultSetSymbolTable.
     */
    static class ResultSetSymbolTable implements PropertiesUtils.SymbolTable {

        /** The m_rs. */
        private final ResultSet m_rs;

        /**
         * Instantiates a new result set symbol table.
         *
         * @param rs
         *            the rs
         */
        public ResultSetSymbolTable(ResultSet rs) {
            m_rs = rs;
        }

        /* (non-Javadoc)
         * @see org.opennms.core.utils.PropertiesUtils.SymbolTable#getSymbolValue(java.lang.String)
         */
        @Override
        public String getSymbolValue(String symbol) {
            try {
                return m_rs.getString(symbol);
            } catch (SQLException e) {
                throw new SQLExceptionHolder(e);
            }
        }

    }

    /**
     * The Class InvalidSymbolTable.
     */
    static class InvalidSymbolTable implements PropertiesUtils.SymbolTable {

        /* (non-Javadoc)
         * @see org.opennms.core.utils.PropertiesUtils.SymbolTable#getSymbolValue(java.lang.String)
         */
        @Override
        public String getSymbolValue(String symbol) {
            throw new IllegalArgumentException("token " + symbol + " is not allowed for " + this
                    + " when no trigger is being processed");
        }

    }

    /**
     * The Class EventAssignment.
     */
    static class EventAssignment {

        /** The Constant LOG. */
        @SuppressWarnings("unused")
        private static final Logger LOG = LoggerFactory.getLogger(EventAssignment.class);

        /** The Constant s_pattern. */
        static final Pattern s_pattern = Pattern.compile("\\$\\{(\\w+)\\}");

        /** The m_assignment. */
        private final Assignment m_assignment;

        /**
         * Instantiates a new event assignment.
         *
         * @param assignment
         *            the assignment
         */
        public EventAssignment(Assignment assignment) {
            m_assignment = assignment;
        }

        /**
         * Assign.
         *
         * @param bldr
         *            the bldr
         * @param symbols
         *            the symbols
         */
        public void assign(EventBuilder bldr, PropertiesUtils.SymbolTable symbols) {

            String val = PropertiesUtils.substitute(m_assignment.getValue(), symbols);

            if (m_assignment.getValue().equals(val) && s_pattern.matcher(val).matches()) {
                // no substitution was made the value was a token pattern so
                // skip it
                return;
            }

            if ("field".equals(m_assignment.getType())) {
                bldr.setField(m_assignment.getName(), val);
            } else {
                bldr.addParam(m_assignment.getName(), val);
            }
        }

    }

    /**
     * The Class ActionEventProcessor.
     */
    static class ActionEventProcessor {

        /** The Constant LOG. */
        private static final Logger LOG = LoggerFactory.getLogger(ActionEventProcessor.class);

        /** The m_automation name. */
        private final String m_automationName;

        /** The m_action event. */
        private final ActionEvent m_actionEvent;

        /** The m_assignments. */
        private final List<EventAssignment> m_assignments;

        /**
         * Instantiates a new action event processor.
         *
         * @param automationName
         *            the automation name
         * @param actionEvent
         *            the action event
         */
        public ActionEventProcessor(String automationName, ActionEvent actionEvent) {
            m_automationName = automationName;
            m_actionEvent = actionEvent;

            if (actionEvent != null) {

                m_assignments = new ArrayList<EventAssignment>(actionEvent.getAssignmentCount());
                for (Assignment assignment : actionEvent.getAssignment()) {
                    m_assignments.add(new EventAssignment(assignment));
                }

            } else {
                m_assignments = null;
            }

        }

        /**
         * Checks for event.
         *
         * @return true, if successful
         */
        public boolean hasEvent() {
            return m_actionEvent != null;
        }

        /**
         * Send.
         */
        void send() {

            if (hasEvent()) {
                // the uei will be set by the event assignments
                EventBuilder bldr = new EventBuilder(null, "Automation");
                buildEvent(bldr, new InvalidSymbolTable());
                LOG.debug("ActionEventProcessor: Sending action-event {} for automation {}", bldr.getEvent().getUei(),
                          m_automationName);
                sendEvent(bldr.getEvent());

            } else {
                LOG.debug("ActionEventProcessor: No action-event for automation {}", m_automationName);
            }
        }

        /**
         * Builds the event.
         *
         * @param bldr
         *            the bldr
         * @param symbols
         *            the symbols
         */
        private void buildEvent(EventBuilder bldr, SymbolTable symbols) {
            for (EventAssignment assignment : m_assignments) {
                assignment.assign(bldr, symbols);
            }
        }

        /**
         * Send event.
         *
         * @param event
         *            the event
         */
        private void sendEvent(Event event) {
            Vacuumd.getSingleton().getEventManager().sendNow(event);
        }

        /**
         * Process trigger results.
         *
         * @param triggerResults
         *            the trigger results
         * @throws SQLException
         *             the sQL exception
         */
        void processTriggerResults(TriggerResults triggerResults) throws SQLException {
            if (!hasEvent()) {
                LOG.debug("processTriggerResults: No action-event for automation {}", m_automationName);
                return;
            }

            ResultSet triggerResultSet = triggerResults.getResultSet();

            triggerResultSet.beforeFirst();

            // Loop through the select results
            while (triggerResultSet.next()) {
                // the uei will be set by the event assignments
                EventBuilder bldr = new EventBuilder(null, "Automation");
                ResultSetSymbolTable symbols = new ResultSetSymbolTable(triggerResultSet);

                try {
                    if (m_actionEvent.isAddAllParms() && resultHasColumn(triggerResultSet, "eventParms")) {
                        bldr.setParms(Parameter.decode(triggerResultSet.getString("eventParms")));
                    }
                    buildEvent(bldr, symbols);
                } catch (SQLExceptionHolder holder) {
                    holder.rethrow();
                }
                LOG.debug("processTriggerResults: Sending action-event {} for automation {}", bldr.getEvent().getUei(),
                          m_automationName);
                sendEvent(bldr.getEvent());
            }

        }

        /**
         * Result has column.
         *
         * @param resultSet
         *            the result set
         * @param columnName
         *            the column name
         * @return true, if successful
         */
        private boolean resultHasColumn(ResultSet resultSet, String columnName) {
            try {
                if (resultSet.findColumn(columnName) > 0) {
                    return true;
                }
            } catch (SQLException e) {
            }
            return false;
        }

        /**
         * For each result.
         *
         * @return true, if successful
         */
        public boolean forEachResult() {
            return m_actionEvent == null ? false : m_actionEvent.getForEachResult();
        }

        /**
         * Process action event.
         *
         * @param triggerResults
         *            the trigger results
         * @throws SQLException
         *             the sQL exception
         */
        void processActionEvent(TriggerResults triggerResults) throws SQLException {
            if (triggerResults.hasTrigger() && forEachResult()) {
                processTriggerResults(triggerResults);
            } else {
                send();
            }
        }

    }

    /**
     * Public constructor.
     *
     * @param automation
     *            a {@link org.opennms.netmgt.config.vacuumd.Automation} object.
     */
    @SuppressWarnings("deprecation")
    public AutomationProcessor(Automation automation) {
        m_ready = true;
        m_automation = automation;
        m_trigger = new TriggerProcessor(m_automation.getName(),
                                         VacuumdConfigFactory.getInstance().getTrigger(m_automation.getTriggerName()));
        m_action = new ActionProcessor(m_automation.getName(),
                                       VacuumdConfigFactory.getInstance().getAction(m_automation.getActionName()));
        m_autoEvent = new AutoEventProcessor(
                                             m_automation.getName(),
                                             VacuumdConfigFactory.getInstance().getAutoEvent(m_automation.getAutoEventName()));
        m_actionEvent = new ActionEventProcessor(
                                                 m_automation.getName(),
                                                 VacuumdConfigFactory.getInstance().getActionEvent(m_automation.getActionEvent()));
    }

    /**
     * <p>
     * getAction
     * </p>
     * .
     *
     * @return a
     *         {@link org.opennms.netmgt.vacuumd.AutomationProcessor.ActionProcessor}
     *         object.
     */
    public ActionProcessor getAction() {
        return m_action;
    }

    /**
     * <p>
     * getTrigger
     * </p>
     * .
     *
     * @return a
     *         {@link org.opennms.netmgt.vacuumd.AutomationProcessor.TriggerProcessor}
     *         object.
     */
    public TriggerProcessor getTrigger() {
        return m_trigger;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    /**
     * <p>
     * run
     * </p>
     * .
     */
    @Override
    public void run() {

        Date startDate = new Date();
        LOG.debug("Start Scheduled automation {}", this);

        if (getAutomation() != null) {
            setReady(false);
            try {
                runAutomation();
            } catch (SQLException e) {
                LOG.warn("Error running automation: {}, {}", getAutomation().getName(), e.getMessage());
            } finally {
                setReady(true);
            }
        }

        LOG.debug("run: Finished automation {}, started at {}", m_automation.getName(), startDate);

    }

    /**
     * Called by the run method to execute the sql statements
     * of triggers and actions defined for an automation. An
     * automation may have 0 or 1 trigger and must have 1 action.
     * If the automation doesn't have a trigger than the action
     * must not contain any tokens.
     *
     * @return a boolean.
     * @throws SQLException
     *             the sQL exception
     */
    public boolean runAutomation() throws SQLException {
        LOG.debug("runAutomation: {} running...", m_automation.getName());

        if (hasTrigger()) {
            LOG.debug("runAutomation: {} trigger statement is: {}", m_automation.getName(), m_trigger.getTriggerSQL());
        }

        LOG.debug("runAutomation: {} action statement is: {}", m_automation.getName(), m_action.getActionSQL());

        LOG.debug("runAutomation: Executing trigger: {}", m_automation.getTriggerName());

        Transaction.begin();
        try {
            LOG.debug("runAutomation: Processing automation: {}", m_automation.getName());

            TriggerResults results = processTrigger();

            boolean success = false;
            if (results.isSuccessful()) {
                success = processAction(results);
            }

            return success;

        } catch (Throwable e) {
            Transaction.rollbackOnly();
            LOG.warn("runAutomation: Could not execute automation: {}", m_automation.getName(), e);
            return false;
        } finally {

            LOG.debug("runAutomation: Ending processing of automation: {}", m_automation.getName());

            Transaction.end();
        }

    }

    /**
     * Process action.
     *
     * @param triggerResults
     *            the trigger results
     * @return true, if successful
     * @throws SQLException
     *             the sQL exception
     */
    private boolean processAction(TriggerResults triggerResults) throws SQLException {
        LOG.debug("runAutomation: running action(s)/actionEvent(s) for : {}", m_automation.getName());

        // Verfiy the trigger ResultSet returned the required number of rows and
        // the required columns for the action statement
        m_action.checkForRequiredColumns(triggerResults);

        if (m_action.processAction(triggerResults)) {
            m_actionEvent.processActionEvent(triggerResults);
            m_autoEvent.send();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Process trigger.
     *
     * @return the trigger results
     * @throws SQLException
     *             the sQL exception
     */
    private TriggerResults processTrigger() throws SQLException {

        if (m_trigger.hasTrigger()) {
            // get a scrollable ResultSet so that we can count the rows and move
            // back to the
            // beginning for processing.

            ResultSet triggerResultSet = m_trigger.runTriggerQuery();

            TriggerResults triggerResults = new TriggerResults(m_trigger, triggerResultSet,
                                                               verifyRowCount(triggerResultSet));

            return triggerResults;

        } else {
            return new TriggerResults(m_trigger, null, true);
        }
    }

    /**
     * <p>
     * verifyRowCount
     * </p>
     * .
     *
     * @param triggerResultSet
     *            a {@link java.sql.ResultSet} object.
     * @return a boolean.
     * @throws SQLException
     *             the sQL exception
     */
    protected boolean verifyRowCount(ResultSet triggerResultSet) throws SQLException {
        if (!m_trigger.hasTrigger()) {
            return true;
        }

        int resultRows;
        boolean validRows = true;
        // determine if number of rows required by the trigger row-count and
        // operator were
        // met by the trigger query, if so we'll run the action
        resultRows = countRows(triggerResultSet);

        int triggerRowCount = m_trigger.getTrigger().getRowCount();
        String triggerOperator = m_trigger.getTrigger().getOperator();

        LOG.debug("verifyRowCount: Verifying trigger result: {} is {} than {}", resultRows,
                  (triggerOperator == null ? "<null>" : triggerOperator), triggerRowCount);

        if (!m_trigger.triggerRowCheck(triggerRowCount, triggerOperator, resultRows))
            validRows = false;

        return validRows;
    }

    /**
     * Method used to count the rows in a ResultSet. This probably requires
     * that your ResultSet is scrollable.
     *
     * @param rs
     *            a {@link java.sql.ResultSet} object.
     * @return a int.
     * @throws SQLException
     *             the sQL exception
     */
    public int countRows(ResultSet rs) throws SQLException {
        if (rs == null) {
            return 0;
        }

        int rows = 0;
        while (rs.next())
            rows++;
        rs.beforeFirst();
        return rows;
    }

    /**
     * Simple helper method to determine if the targetString contains
     * any '${token}'s.
     *
     * @param targetString
     *            a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean containsTokens(String targetString) {
        return m_action.getTokenCount(targetString) > 0;
    }

    /**
     * <p>
     * getAutomation
     * </p>
     * .
     *
     * @return Returns the automation.
     */
    public Automation getAutomation() {
        return m_automation;
    }

    /**
     * <p>
     * isReady
     * </p>
     * .
     *
     * @return a boolean.
     */
    @Override
    public boolean isReady() {
        return m_ready;
    }

    /**
     * <p>
     * getSchedule
     * </p>
     * .
     *
     * @return Returns the schedule.
     */
    public Schedule getSchedule() {
        return m_schedule;
    }

    /**
     * <p>
     * setSchedule
     * </p>
     * .
     *
     * @param schedule
     *            The schedule to set.
     */
    public void setSchedule(Schedule schedule) {
        m_schedule = schedule;
    }

    /**
     * Checks for trigger.
     *
     * @return true, if successful
     */
    private boolean hasTrigger() {
        return m_trigger.hasTrigger();
    }

    /**
     * <p>
     * setReady
     * </p>
     * .
     *
     * @param ready
     *            a boolean.
     */
    public void setReady(boolean ready) {
        m_ready = ready;
    }

}

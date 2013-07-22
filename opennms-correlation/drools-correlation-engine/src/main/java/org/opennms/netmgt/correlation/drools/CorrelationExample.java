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

package org.opennms.netmgt.correlation.drools;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.audit.WorkingMemoryFileLogger;
import org.drools.compiler.PackageBuilder;

/**
 * <p>
 * CorrelationExample class.
 * </p>
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @version $Id: $
 */
public class CorrelationExample {

    /**
     * <p>
     * main
     * </p>
     * .
     *
     * @param args
     *            an array of {@link java.lang.String} objects.
     * @throws Exception
     *             the exception
     */
    public static void main(final String[] args) throws Exception {

        final PackageBuilder builder = new PackageBuilder();
        builder.addPackageFromDrl(new InputStreamReader(
                                                        CorrelationExample.class.getResourceAsStream("CorrelationExample.drl"),
                                                        "UTF-8"));

        final RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage(builder.getPackage());

        final WorkingMemory workingMemory = ruleBase.newStatefulSession();

        final WorkingMemoryFileLogger logger = new WorkingMemoryFileLogger(workingMemory);
        logger.setFileName("log/correlation");

        final InputStream in = CorrelationExample.class.getResourceAsStream("simulation");
        try {
            final Simulation simulation = new Simulation();
            System.out.println("Loading Simulation");
            simulation.load(in);
            System.out.println("Executing Simulation");
            simulation.simulate(workingMemory);

        } finally {
            if (in != null) {
                in.close();
            }
        }

        logger.writeToDisk();
    }

    /**
     * Sleep.
     *
     * @param delay
     *            the delay
     */
    private static void sleep(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }

    }

    /**
     * The Class Simulation.
     */
    public static class Simulation {

        /**
         * The Class SimItem.
         */
        public static class SimItem {

            /** The m_delay. */
            final int m_delay;

            /** The m_event. */
            final EventBean m_event;

            /**
             * Instantiates a new sim item.
             *
             * @param delay
             *            the delay
             * @param event
             *            the event
             */
            public SimItem(final int delay, final EventBean event) {
                m_delay = delay;
                m_event = event;
            }

            /**
             * Simulate.
             *
             * @param memory
             *            the memory
             */
            public void simulate(final WorkingMemory memory) {
                sleep(m_delay);
                System.out.println("Start simulation of " + this);
                memory.insert(m_event);
                memory.fireAllRules();
                System.out.println("End simulation of " + this);
            }

        }

        /** The m_nodes. */
        final Map<Integer, Node> m_nodes = new HashMap<Integer, Node>();

        /** The m_event sequence. */
        final List<SimItem> m_eventSequence = new LinkedList<SimItem>();

        /**
         * Load.
         *
         * @param in
         *            the in
         */
        public void load(final InputStream in) {

            final Scanner scanner = new Scanner(in);
            while (scanner.hasNext()) {
                final String lineType = scanner.next();
                if ("#".equals(lineType)) {
                    scanner.nextLine();
                } else if ("node".equals(lineType)) {
                    /*
                     * expect line to be
                     * node <nodeLabel> <nodeid> (<parentnodeid>?)
                     * Note: parent nodes need to be defined before their
                     * children
                     * If the parentnodeid is missing then we assume that it has
                     * no parent
                     */
                    final String nodeLabel = scanner.next();
                    final Integer nodeId = scanner.nextInt();
                    assert m_nodes.get(nodeId) == null : "Already have a node with id " + nodeId;

                    Integer parentId = null;
                    if (scanner.hasNextInt()) {
                        parentId = scanner.nextInt();
                    }

                    assert (parentId == null || m_nodes.containsKey(parentId)) : "Reference to parentId " + parentId
                            + " that is not yet defined";

                    final Node parent = (parentId == null ? null : m_nodes.get(parentId));
                    final Node node = new Node(nodeId, nodeLabel, parent);
                    m_nodes.put(nodeId, node);

                } else if ("event".equals(lineType)) {
                    /*
                     * expect line to be
                     * event delay uei nodeid
                     */
                    final int delay = scanner.nextInt();
                    final String uei = scanner.next();
                    final Integer nodeId = scanner.nextInt();

                    assert m_nodes.containsKey(nodeId) : "Invalid nodeId " + nodeId;

                    final EventBean e = new EventBean(uei, m_nodes.get(nodeId));
                    final SimItem item = new SimItem(delay, e);
                    m_eventSequence.add(item);

                }

            }
        }

        /**
         * Simulate.
         *
         * @param memory
         *            the memory
         */
        public void simulate(final WorkingMemory memory) {
            for (final SimItem item : m_eventSequence) {
                item.simulate(memory);
                System.out.println("Memory Size = " + getObjectCount(memory));
            }
        }
    }

    /**
     * The Class Outage.
     */
    public static class Outage {

        /** The m_problem. */
        private EventBean m_problem;

        /** The m_resolution. */
        private EventBean m_resolution;

        /** The m_cause. */
        private Node m_cause;

        /** The m_node. */
        private Node m_node;

        /**
         * Instantiates a new outage.
         *
         * @param node
         *            the node
         * @param problem
         *            the problem
         */
        public Outage(final Node node, final EventBean problem) {
            m_node = node;
            m_problem = problem;
        }

        /**
         * Gets the node.
         *
         * @return the node
         */
        public Node getNode() {
            return m_node;
        }

        /**
         * Gets the problem.
         *
         * @return the problem
         */
        public EventBean getProblem() {
            return m_problem;
        }

        /**
         * Gets the resolution.
         *
         * @return the resolution
         */
        public EventBean getResolution() {
            return m_resolution;
        }

        /**
         * Sets the resolution.
         *
         * @param resolution
         *            the new resolution
         */
        public void setResolution(final EventBean resolution) {
            m_resolution = resolution;
        }

        /**
         * Gets the cause.
         *
         * @return the cause
         */
        public Node getCause() {
            return m_cause;
        }

        /**
         * Sets the cause.
         *
         * @param cause
         *            the new cause
         */
        public void setCause(final Node cause) {
            m_cause = cause;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return new ToStringBuilder(this).append("problem", m_problem).append("cause", m_cause).append("resolution",
                                                                                                          m_resolution).toString();
        }

    }

    /**
     * The Class Node.
     */
    public static class Node {

        /** The m_id. */
        private Integer m_id;

        /** The m_parent. */
        private Node m_parent;

        /** The m_label. */
        private String m_label;

        /**
         * Instantiates a new node.
         *
         * @param id
         *            the id
         * @param label
         *            the label
         * @param parent
         *            the parent
         */
        public Node(final Integer id, final String label, final Node parent) {
            m_id = id;
            m_label = label;
            m_parent = parent;
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        public Integer getId() {
            return m_id;
        }

        /**
         * Gets the parent.
         *
         * @return the parent
         */
        public Node getParent() {
            return m_parent;
        }

        /**
         * Gets the label.
         *
         * @return the label
         */
        public String getLabel() {
            return m_label;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return new ToStringBuilder(this).append("id", m_id).append("label", m_label).toString();
        }
    }

    /**
     * The Class EventBean.
     */
    public static class EventBean {

        /** The m_uei. */
        private String m_uei;

        /** The m_node. */
        private Node m_node;

        /**
         * Instantiates a new event bean.
         *
         * @param uei
         *            the uei
         * @param node
         *            the node
         */
        public EventBean(final String uei, final Node node) {
            m_uei = uei;
            m_node = node;
        }

        /**
         * Gets the uei.
         *
         * @return the uei
         */
        public String getUei() {
            return m_uei;
        }

        /**
         * Gets the node.
         *
         * @return the node
         */
        public Node getNode() {
            return m_node;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return new ToStringBuilder(this).append("uei", m_uei).append("node", m_node).toString();
        }
    }

    /**
     * The Class PossibleCause.
     */
    public static class PossibleCause {

        /** The m_node. */
        private Node m_node;

        /** The m_outage. */
        private Outage m_outage;

        /** The m_verified. */
        private boolean m_verified;

        /**
         * Instantiates a new possible cause.
         *
         * @param node
         *            the node
         * @param outage
         *            the outage
         */
        public PossibleCause(final Node node, final Outage outage) {
            this(node, outage, false);
        }

        /**
         * Instantiates a new possible cause.
         *
         * @param node
         *            the node
         * @param outage
         *            the outage
         * @param verified
         *            the verified
         */
        public PossibleCause(final Node node, final Outage outage, final boolean verified) {
            m_node = node;
            m_outage = outage;
            m_verified = verified;
        }

        /**
         * Gets the node.
         *
         * @return the node
         */
        public Node getNode() {
            return m_node;
        }

        /**
         * Gets the outage.
         *
         * @return the outage
         */
        public Outage getOutage() {
            return m_outage;
        }

        /**
         * Checks if is verified.
         *
         * @return true, if is verified
         */
        public boolean isVerified() {
            return m_verified;
        }

        /**
         * Sets the verified.
         *
         * @param verified
         *            the new verified
         */
        public void setVerified(final boolean verified) {
            m_verified = verified;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return new ToStringBuilder(this).append("node", m_node).append("outage", m_outage).toString();
        }
    }

    /**
     * <p>
     * getObjectCount
     * </p>
     * .
     *
     * @param memory
     *            a {@link org.drools.WorkingMemory} object.
     * @return a int.
     */
    public static int getObjectCount(final WorkingMemory memory) {
        int count = 0;
        for (final Iterator<?> it = memory.iterateObjects(); it.hasNext(); it.next()) {
            count++;
        }
        return count;
    }

}

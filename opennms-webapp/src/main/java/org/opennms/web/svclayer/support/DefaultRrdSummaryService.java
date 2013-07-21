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

package org.opennms.web.svclayer.support;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.config.attrsummary.Attribute;
import org.opennms.netmgt.config.attrsummary.Resource;
import org.opennms.netmgt.config.attrsummary.Summary;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.ResourceDao;
import org.opennms.netmgt.dao.api.RrdDao;
import org.opennms.netmgt.dao.support.FilterWalker;
import org.opennms.netmgt.dao.support.NodeSnmpResourceType;
import org.opennms.netmgt.filter.FilterDao;
import org.opennms.netmgt.model.AbstractEntityVisitor;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.RrdGraphAttribute;
import org.opennms.web.svclayer.RrdSummaryService;
import org.opennms.web.svclayer.SummarySpecification;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * <p>
 * DefaultRrdSummaryService class.
 * </p>
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public class DefaultRrdSummaryService implements RrdSummaryService, InitializingBean {

    /**
     * The Class SummaryBuilder.
     */
    static class SummaryBuilder {

        /** The m_root. */
        private SummaryHolder m_root;

        /** The m_current resource. */
        private ResourceHolder m_currentResource;

        /** The m_curr attr. */
        private Attribute m_currAttr;

        /**
         * The Interface ResourceParent.
         */
        interface ResourceParent {

            /**
             * Checks if is root.
             *
             * @return true, if is root
             */
            boolean isRoot();

            /**
             * Adds the resource.
             *
             * @param resource
             *            the resource
             */
            void addResource(Resource resource);

            /**
             * Commit.
             */
            void commit();
        }

        /**
         * The Class SummaryHolder.
         */
        class SummaryHolder implements ResourceParent {

            /** The m_summary. */
            Summary m_summary = new Summary();

            /* (non-Javadoc)
             * @see org.opennms.web.svclayer.support.DefaultRrdSummaryService.SummaryBuilder.ResourceParent#addResource(org.opennms.netmgt.config.attrsummary.Resource)
             */
            @Override
            public void addResource(final Resource resource) {
                m_summary.addResource(resource);
            }

            /* (non-Javadoc)
             * @see org.opennms.web.svclayer.support.DefaultRrdSummaryService.SummaryBuilder.ResourceParent#commit()
             */
            @Override
            public void commit() {

            }

            /**
             * Gets the summary.
             *
             * @return the summary
             */
            public Summary getSummary() {
                return m_summary;
            }

            /* (non-Javadoc)
             * @see org.opennms.web.svclayer.support.DefaultRrdSummaryService.SummaryBuilder.ResourceParent#isRoot()
             */
            @Override
            public boolean isRoot() {
                return true;
            }

            /* (non-Javadoc)
             * @see java.lang.Object#toString()
             */
            @Override
            public String toString() {
                return "[root]";
            }

        }

        /**
         * The Class ResourceHolder.
         */
        class ResourceHolder implements ResourceParent {

            /** The m_parent. */
            ResourceParent m_parent;

            /** The m_commited. */
            boolean m_commited = false;

            /** The m_resource. */
            Resource m_resource;

            /**
             * Instantiates a new resource holder.
             *
             * @param parent
             *            the parent
             * @param name
             *            the name
             */
            ResourceHolder(final ResourceParent parent, final String name) {
                Assert.notNull(parent, "parent must not be null");
                m_parent = parent;
                m_resource = new Resource();
                m_resource.setName(name);
            }

            /**
             * Gets the parent.
             *
             * @return the parent
             */
            public ResourceParent getParent() {
                return m_parent;
            }

            /**
             * Checks if is commited.
             *
             * @return true, if is commited
             */
            public boolean isCommited() {
                return m_commited;
            }

            /* (non-Javadoc)
             * @see org.opennms.web.svclayer.support.DefaultRrdSummaryService.SummaryBuilder.ResourceParent#commit()
             */
            @Override
            public void commit() {
                if (isCommited()) {
                    return;
                }
                if (m_parent != null) {
                    m_parent.commit();
                }
                addSelf();
                m_commited = true;
            }

            /* (non-Javadoc)
             * @see org.opennms.web.svclayer.support.DefaultRrdSummaryService.SummaryBuilder.ResourceParent#addResource(org.opennms.netmgt.config.attrsummary.Resource)
             */
            @Override
            public void addResource(final Resource resource) {
                m_resource.addResource(resource);
            }

            /**
             * Adds the attribute.
             *
             * @param name
             *            the name
             * @return the attribute
             */
            protected Attribute addAttribute(final String name) {
                Attribute attr = new Attribute();
                attr.setName(name);
                m_resource.addAttribute(attr);
                commit();
                return attr;
            }

            /**
             * Adds the self.
             */
            protected void addSelf() {
                if (getParent() == null) {
                    m_root.addResource(m_resource);
                } else {
                    getParent().addResource(m_resource);
                }
            }

            /* (non-Javadoc)
             * @see java.lang.Object#toString()
             */
            @Override
            public String toString() {
                return (getParent() == null ? "[root]" : getParent().toString()) + ".[" + m_resource.getName() + "]";
            }

            /* (non-Javadoc)
             * @see org.opennms.web.svclayer.support.DefaultRrdSummaryService.SummaryBuilder.ResourceParent#isRoot()
             */
            @Override
            public boolean isRoot() {
                return false;
            }

        }

        /**
         * Instantiates a new summary builder.
         */
        SummaryBuilder() {
            m_root = new SummaryHolder();
        }

        /**
         * Gets the summary.
         *
         * @return the summary
         */
        Summary getSummary() {
            return m_root.getSummary();
        }

        /**
         * Adds the attribute.
         *
         * @param name
         *            the name
         */
        public void addAttribute(final String name) {
            Assert.state(m_currentResource != null, "addResource must be called before calling addAttribute");
            m_currAttr = m_currentResource.addAttribute(name);
        }

        /**
         * Sets the min.
         *
         * @param min
         *            the new min
         */
        public void setMin(final double min) {
            checkForCurrAttr();
            m_currAttr.setMin(min);

        }

        /**
         * Check for curr attr.
         */
        private void checkForCurrAttr() {
            Assert.state(m_currAttr != null, "addAttribute must be called before calling setMin,setMax or setAverage");
        }

        /**
         * Sets the average.
         *
         * @param avg
         *            the new average
         */
        public void setAverage(final double avg) {
            checkForCurrAttr();
            m_currAttr.setAverage(avg);
        }

        /**
         * Sets the max.
         *
         * @param max
         *            the new max
         */
        public void setMax(final double max) {
            checkForCurrAttr();
            m_currAttr.setMax(max);
        }

        /**
         * Push resource.
         *
         * @param label
         *            the label
         */
        public void pushResource(final String label) {
            ResourceParent parent = (m_currentResource == null ? m_root : m_currentResource);
            m_currentResource = new ResourceHolder(parent, label);
        }

        /**
         * Pop resource.
         */
        public void popResource() {
            Assert.state(m_currentResource != null, "you must push a resource before you can pop one");
            if (m_currentResource.getParent().isRoot()) {
                m_currentResource = null;
            } else {
                m_currentResource = (ResourceHolder) m_currentResource.getParent();
            }
        }
    }

    /** The m_filter dao. */
    public FilterDao m_filterDao;

    /** The m_resource dao. */
    public ResourceDao m_resourceDao;

    /** The m_rrd dao. */
    public RrdDao m_rrdDao;

    /** The m_node dao. */
    public NodeDao m_nodeDao;

    /** The m_stats. */
    public Stats m_stats = new Stats();

    /**
     * The Class OpStats.
     */
    static class OpStats {

        /** The m_name. */
        private String m_name;

        /** The m_count. */
        private int m_count = 0;

        /** The m_total. */
        private long m_total = 0;

        /** The m_last started. */
        private long m_lastStarted = -1;

        /**
         * Instantiates a new op stats.
         *
         * @param n
         *            the n
         */
        OpStats(final String n) {
            m_name = n;
        }

        /**
         * Begin.
         */
        void begin() {
            m_count++;
            m_lastStarted = System.nanoTime();
        }

        /**
         * End.
         */
        void end() {
            long ended = System.nanoTime();
            Assert.state(m_lastStarted >= 0, "must call begin before calling end");
            m_total += (ended - m_lastStarted);
            m_lastStarted = -1;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            double total = (double) m_total;
            return String.format("stats: %s: count=%d, totalTime=%f ms ( %f us/call )", m_name, m_count,
                                 total / 1000000.0, total / (m_count * 1000.0));
        }

    }

    /**
     * The Class Stats.
     */
    static class Stats {

        /** The map. */
        Map<String, OpStats> map = new LinkedHashMap<String, OpStats>();

        /**
         * Begin.
         *
         * @param operation
         *            the operation
         */
        public void begin(final String operation) {
            if (!map.containsKey(operation)) {
                map.put(operation, new OpStats(operation));
            }
            map.get(operation).begin();
        }

        /**
         * End.
         *
         * @param operation
         *            the operation
         */
        public void end(final String operation) {
            map.get(operation).end();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder bldr = new StringBuilder(map.size() * 50);
            for (OpStats opStat : map.values()) {
                bldr.append(opStat);
                bldr.append('\n');
            }
            return bldr.toString();
        }

    }

    /**
     * <p>
     * getSummary
     * </p>
     * .
     *
     * @param filterRule
     *            a {@link java.lang.String} object.
     * @param startTime
     *            a long.
     * @param endTime
     *            a long.
     * @param attributeSieve
     *            a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.config.attrsummary.Summary} object.
     */
    public final Summary getSummary(final String filterRule, final long startTime, final long endTime,
            final String attributeSieve) {
        m_stats.begin("getSummary");
        try {
            final SummaryBuilder bldr = new SummaryBuilder();

            FilterWalker walker = new FilterWalker();
            walker.setFilterDao(m_filterDao);
            walker.setNodeDao(m_nodeDao);
            walker.setFilter(filterRule);
            walker.setVisitor(new AbstractEntityVisitor() {
                @Override
                public void visitNode(final OnmsNode node) {

                    OnmsResource nodeResource = getResourceForNode(node);

                    bldr.pushResource(node.getLabel());

                    for (OnmsResource child : getChildResources1(nodeResource)) {
                        if (child.getResourceType() instanceof NodeSnmpResourceType) {
                            addAttributes(getResourceGraphAttributes(child));
                        }
                    }

                    for (OnmsResource child : getChildResources2(nodeResource)) {
                        if (!(child.getResourceType() instanceof NodeSnmpResourceType)) {
                            addResource(child);
                        }
                    }

                    bldr.popResource();
                }

                private Collection<RrdGraphAttribute> getResourceGraphAttributes(final OnmsResource child) {
                    String op = "getResourceGraphAttributes-" + child.getResourceType().getName();
                    m_stats.begin(op);
                    try {
                        return child.getRrdGraphAttributes().values();
                    } finally {
                        m_stats.end(op);
                    }
                }

                private List<OnmsResource> getChildResources1(final OnmsResource nodeResource) {
                    m_stats.begin("getChildResources1");
                    try {
                        return nodeResource.getChildResources();
                    } finally {
                        m_stats.end("getChildResources1");
                    }
                }

                private List<OnmsResource> getChildResources2(final OnmsResource nodeResource) {
                    m_stats.begin("getChildResources2");
                    try {
                        return nodeResource.getChildResources();
                    } finally {
                        m_stats.end("getChildResources2");
                    }
                }

                private OnmsResource getResourceForNode(final OnmsNode node) {
                    m_stats.begin("getResourceForNode");
                    try {
                        return m_resourceDao.getResourceForNode(node);
                    } finally {
                        m_stats.end("getResourceForNode");
                    }
                }

                private void addResource(final OnmsResource resource) {
                    addResource(resource, resource.getLabel());
                }

                private void addResource(final OnmsResource resource, final String label) {
                    Collection<RrdGraphAttribute> attrs = getResourceGraphAttributes(resource);
                    if (attrs.size() > 0) {
                        bldr.pushResource(label);
                        addAttributes(attrs);
                        bldr.popResource();
                    }
                }

                private void addAttributes(final Collection<RrdGraphAttribute> attrs) {
                    m_stats.begin("addAttributes");
                    try {
                        for (RrdGraphAttribute attr : attrs) {
                            if (attr.getName().matches(attributeSieve)) {
                                bldr.addAttribute(attr.getName());
                                double[] values = getValues(attr);
                                bldr.setMin(values[0]);
                                bldr.setAverage(values[1]);
                                bldr.setMax(values[2]);
                            }
                        }
                    } finally {
                        m_stats.end("addAttributes");
                    }
                }

                private double[] getValues(final RrdGraphAttribute attr) {
                    m_stats.begin("getValues");
                    try {
                        return m_rrdDao.getPrintValues(attr, "AVERAGE", startTime * 1000, endTime * 1000, "MIN",
                                                       "AVERAGE", "MAX");
                    } finally {
                        m_stats.end("getValues");
                    }
                }

            });
            walker.walk();

            return bldr.getSummary();
        } finally {
            m_stats.end("getSummary");
        }
    }

    /**
     * <p>
     * afterPropertiesSet
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public final void afterPropertiesSet() throws Exception {
        Assert.state(m_filterDao != null, "filterDao property must be set");
        Assert.state(m_resourceDao != null, "resourceDao property must be set");
        Assert.state(m_rrdDao != null, "rrdDao property must be set");
        Assert.state(m_nodeDao != null, "nodeDao property must be set");
    }

    /**
     * <p>
     * setFilterDao
     * </p>
     * .
     *
     * @param filterDao
     *            a {@link org.opennms.netmgt.filter.FilterDao} object.
     */
    public final void setFilterDao(final FilterDao filterDao) {
        m_filterDao = filterDao;
    }

    /**
     * <p>
     * setResourceDao
     * </p>
     * .
     *
     * @param resourceDao
     *            a {@link org.opennms.netmgt.dao.api.ResourceDao} object.
     */
    public final void setResourceDao(final ResourceDao resourceDao) {
        m_resourceDao = resourceDao;
    }

    /**
     * <p>
     * setRrdDao
     * </p>
     * .
     *
     * @param rrdDao
     *            a {@link org.opennms.netmgt.dao.api.RrdDao} object.
     */
    public final void setRrdDao(final RrdDao rrdDao) {
        m_rrdDao = rrdDao;
    }

    /**
     * Gets the node dao.
     *
     * @return the nodeDao
     */
    public final NodeDao getNodeDao() {
        return m_nodeDao;
    }

    /**
     * Sets the node dao.
     *
     * @param nodeDao
     *            the nodeDao to set
     */
    public final void setNodeDao(final NodeDao nodeDao) {
        this.m_nodeDao = nodeDao;
    }

    /** {@inheritDoc} */
    @Override
    public final Summary getSummary(final SummarySpecification spec) {
        return getSummary(spec.getFilterRule(), spec.getStartTime(), spec.getEndTime(), spec.getAttributeSieve());
    }

}

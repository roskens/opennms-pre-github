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

package org.opennms.netmgt.ncs.persistence;

import java.util.List;

import org.opennms.netmgt.dao.hibernate.AbstractDaoHibernate;
import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
import org.springframework.dao.DataAccessException;

/**
 * The Class NCSComponentDao.
 */
public class NCSComponentDao extends AbstractDaoHibernate<NCSComponent, Long> implements NCSComponentRepository {

    /**
     * Instantiates a new nCS component dao.
     */
    public NCSComponentDao() {
        super(NCSComponent.class);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findByType(java.lang.String)
     */
    @Override
    public List<NCSComponent> findByType(String type) {
        return find("from NCSComponent as ncs where ncs.type = ?", type);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findByTypeAndForeignIdentity(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public NCSComponent findByTypeAndForeignIdentity(String type, String foreignSource, String foreignId) {
        return findUnique("from NCSComponent as ncs where ncs.type = ? and ncs.foreignSource = ? and ncs.foreignId = ?",
                          type, foreignSource, foreignId);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findComponentsThatDependOn(org.opennms.netmgt.model.ncs.NCSComponent)
     */
    @Override
    public List<NCSComponent> findComponentsThatDependOn(NCSComponent component) {
        return find("from NCSComponent as ncs where ? in elements(ncs.subcomponents)", component);
    }

    /**
     * Find components with child.
     *
     * @param component
     *            the component
     * @return the list
     */
    public List<NCSComponent> findComponentsWithChild(final NCSComponent component) {
        return find("from NCSComponent as ncs where ? in elements(ncs.parentcomponents)", component);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findComponentsWithAttribute(java.lang.String, java.lang.String)
     */
    @Override
    public List<NCSComponent> findComponentsWithAttribute(String attrKey, String attrValue) {
        return find("from NCSComponent as ncs where ncs.attributes[?] = ?", attrKey, attrValue);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findComponentsByNodeId(int)
     */
    @Override
    public List<NCSComponent> findComponentsByNodeId(int nodeid) {
        return find("select distinct ncs from NCSComponent as ncs, OnmsNode as n left join fetch ncs.attributes where ncs.nodeIdentification.foreignSource = n.foreignSource and ncs.nodeIdentification.foreignId = n.foreignId and n.id = ?",
                    nodeid);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.hibernate.AbstractDaoHibernate#save(java.lang.Object)
     */
    @Override
    public void save(final NCSComponent entity) throws DataAccessException {
        validateEntity(entity);
        super.save(entity);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.dao.hibernate.AbstractDaoHibernate#saveOrUpdate(java.lang.Object)
     */
    @Override
    public void saveOrUpdate(final NCSComponent entity) throws DataAccessException {
        validateEntity(entity);
        super.saveOrUpdate(entity);

    }

    // enforcing no colons seems to be not very feasible in The Real World
    /**
     * Validate entity.
     *
     * @param entity
     *            the entity
     * @throws DataAccessException
     *             the data access exception
     */
    private void validateEntity(final NCSComponent entity) throws DataAccessException {
        /*
         * if (entity.getForeignId().contains(":")) {
         * throw new
         * InvalidForeignIdException("Foreign ID cannot contain a colon!");
         * }
         * if (entity.getForeignSource().contains(":")) {
         * throw new InvalidForeignSourceException(entity.getForeignSource(),
         * "Foreign Source cannot contain a colon!");
         * }
         * for (final NCSComponent component : entity.getSubcomponents()) {
         * validateEntity(component);
         * }
         */
    }

    /**
     * The Class InvalidForeignIdException.
     */
    public static final class InvalidForeignIdException extends DataAccessException {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -762719422321681955L;

        /**
         * Instantiates a new invalid foreign id exception.
         *
         * @param msg
         *            the msg
         */
        public InvalidForeignIdException(final String msg) {
            super("Invalid Foreign ID: " + msg);
        }

        /**
         * Instantiates a new invalid foreign id exception.
         *
         * @param foreignId
         *            the foreign id
         * @param msg
         *            the msg
         */
        public InvalidForeignIdException(final String foreignId, final String msg) {
            super("Invalid Foreign ID (" + foreignId + "): " + msg);
        }
    }

    /**
     * The Class InvalidForeignSourceException.
     */
    public static final class InvalidForeignSourceException extends DataAccessException {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -4762525172339952143L;

        /**
         * Instantiates a new invalid foreign source exception.
         *
         * @param msg
         *            the msg
         */
        public InvalidForeignSourceException(final String msg) {
            super("Invalid Foreign Source: " + msg);
        }

        /**
         * Instantiates a new invalid foreign source exception.
         *
         * @param foreignSource
         *            the foreign source
         * @param msg
         *            the msg
         */
        public InvalidForeignSourceException(final String foreignSource, final String msg) {
            super("Invalid Foreign Source (" + foreignSource + "): " + msg);
        }
    }
}

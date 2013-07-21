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
package org.opennms.features.topology.plugins.ncs;

import java.util.List;

import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;

/**
 * The Class MockNCSComponentRepository.
 */
public class MockNCSComponentRepository implements NCSComponentRepository {

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#lock()
     */
    @Override
    public void lock() {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#initialize(java.lang.Object)
     */
    @Override
    public void initialize(Object obj) {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#flush()
     */
    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#clear()
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#countAll()
     */
    @Override
    public int countAll() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#delete(org.opennms.netmgt.model.ncs.NCSComponent)
     */
    @Override
    public void delete(NCSComponent component) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findAll()
     */
    @Override
    public List<NCSComponent> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findMatching(org.opennms.netmgt.model.OnmsCriteria)
     */
    @Override
    public List<NCSComponent> findMatching(OnmsCriteria criteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#countMatching(org.opennms.netmgt.model.OnmsCriteria)
     */
    @Override
    public int countMatching(OnmsCriteria onmsCrit) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#get(java.lang.Long)
     */
    @Override
    public NCSComponent get(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#load(java.lang.Long)
     */
    @Override
    public NCSComponent load(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#save(org.opennms.netmgt.model.ncs.NCSComponent)
     */
    @Override
    public void save(NCSComponent component) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#saveOrUpdate(org.opennms.netmgt.model.ncs.NCSComponent)
     */
    @Override
    public void saveOrUpdate(NCSComponent component) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#update(org.opennms.netmgt.model.ncs.NCSComponent)
     */
    @Override
    public void update(NCSComponent component) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findByType(java.lang.String)
     */
    @Override
    public List<NCSComponent> findByType(String type) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findByTypeAndForeignIdentity(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public NCSComponent findByTypeAndForeignIdentity(String type, String foreignSource, String foreignId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findComponentsThatDependOn(org.opennms.netmgt.model.ncs.NCSComponent)
     */
    @Override
    public List<NCSComponent> findComponentsThatDependOn(NCSComponent component) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findComponentsWithAttribute(java.lang.String, java.lang.String)
     */
    @Override
    public List<NCSComponent> findComponentsWithAttribute(String attrKey, String attrValue) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ncs.NCSComponentRepository#findComponentsByNodeId(int)
     */
    @Override
    public List<NCSComponent> findComponentsByNodeId(int nodeid) {
        // TODO Auto-generated method stub
        return null;
    }

}

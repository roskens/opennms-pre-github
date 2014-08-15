/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opennms.netmgt.collection.support;

import org.opennms.netmgt.collection.api.Resource;

/**
 *
 * should there be some kind of attribute/properties map on this?
 * <p>
 * @author roskens
 */
public abstract class AbstractResource implements Resource {
    private final Resource m_parent;
    private final String m_type;
    private final String m_name;

    public AbstractResource(final Resource parent, final String type, final String name) {
        m_parent = parent;
        m_type = type;
        m_name = name;
    }

    @Override
    public Resource getParent() {
        return m_parent;
    }

    @Override
    public String getType() {
        return m_type;
    }

    @Override
    public String getName() {
        return m_name;
    }

    @Override
    public String resolvePath() {
        StringBuilder s = new StringBuilder();
        if (m_parent != null) {
            s.append(m_parent.resolvePath()).append(".");
        }
        s.append(m_type).append("[").append(m_name).append("]");

        return s.toString();
    }
}

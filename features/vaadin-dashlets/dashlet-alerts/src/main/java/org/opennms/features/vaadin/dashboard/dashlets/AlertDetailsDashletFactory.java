package org.opennms.features.vaadin.dashboard.dashlets;

import org.opennms.features.vaadin.dashboard.model.AbstractDashletFactory;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.dao.NodeDao;

public class AlertDetailsDashletFactory extends AbstractDashletFactory {
    private AlarmDao m_alarmDao;
    private NodeDao m_nodeDao;

    public AlertDetailsDashletFactory(AlarmDao alarmDao, NodeDao nodeDao) {
        super("Alert Details");

        m_alarmDao = alarmDao;
        m_nodeDao = nodeDao;
    }

    public Dashlet newDashletInstance(DashletSpec dashletSpec) {
        return new AlertDetailsDashlet(dashletSpec, m_alarmDao, m_nodeDao);
    }

}

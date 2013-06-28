package org.opennms.features.vaadin.dashboard.dashlets;

import org.opennms.features.vaadin.dashboard.model.AbstractDashletFactory;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.dao.NodeDao;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AlertsDashletFactory extends AbstractDashletFactory {
    private AlarmDao m_alarmDao;
    private NodeDao m_nodeDao;

    public AlertsDashletFactory(AlarmDao alarmDao, NodeDao nodeDao) {
        super("Alerts");

        m_alarmDao = alarmDao;
        m_nodeDao = nodeDao;
    }

    public Dashlet newDashletInstance(DashletSpec dashletSpec) {
        return new AlertsDashlet(dashletSpec, m_alarmDao, m_nodeDao);
    }
}

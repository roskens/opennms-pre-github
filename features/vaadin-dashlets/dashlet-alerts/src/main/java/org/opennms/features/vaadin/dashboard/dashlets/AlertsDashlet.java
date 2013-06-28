package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.core.criteria.Fetch;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsSeverity;

import java.util.Calendar;
import java.util.List;

public class AlertsDashlet extends VerticalLayout implements Dashlet {
    private AlarmDao m_alarmDao;
    private NodeDao m_nodeDao;
    private DashletSpec m_dashletSpec;

    int tries = 0;

    public AlertsDashlet(DashletSpec dashletSpec, AlarmDao alarmDao, NodeDao nodeDao) {
        m_dashletSpec = dashletSpec;
        m_alarmDao = alarmDao;
        m_nodeDao = nodeDao;
        setCaption(getName());
        setWidth("100%");
        //setSizeFull();

    }

    private boolean updateAlarms() {
        final CriteriaBuilder alarmCb = new CriteriaBuilder(OnmsAlarm.class);

        int minimumSeverity = 4;
        int boostSeverity = 6;
        int alarmsPerPage = 12;

        try {
            alarmsPerPage = Math.max(1, Integer.parseInt(m_dashletSpec.getParameters().get("alarmsPerPage")));
            minimumSeverity = Math.min(7, Math.max(1, Integer.parseInt(m_dashletSpec.getParameters().get("minimumSeverity"))));
            boostSeverity = Math.min(7, Math.max(1, Integer.parseInt(m_dashletSpec.getParameters().get("boostSeverity"))));
        } catch (NumberFormatException numberFormatException) {
        }

        alarmCb.fetch("firstEvent", Fetch.FetchType.EAGER);
        alarmCb.fetch("lastEvent", Fetch.FetchType.EAGER);

        alarmCb.isNull("alarmAckUser");
        alarmCb.ge("severity", OnmsSeverity.get(minimumSeverity));

        alarmCb.orderBy("lastEventTime").desc();
        alarmCb.limit(alarmsPerPage);

        alarmCb.distinct();

        List<OnmsAlarm> alarms = m_alarmDao.findMatching(alarmCb.toCriteria());
        removeAllComponents();

        boolean boosted = false;

        if (alarms.size() == 0) {
            Label label = new Label("No alarms found!");
            label.addStyleName("node-font");
            addComponent(label);
        } else {
            for (OnmsAlarm onmsAlarm : alarms) {
                OnmsNode onmsNode = null;

                if (onmsAlarm.getNodeId() != null) {
                    CriteriaBuilder nodeCb = new CriteriaBuilder(OnmsNode.class);
                    nodeCb.eq("id", onmsAlarm.getNodeId());

                    List<OnmsNode> nodes = m_nodeDao.findMatching(nodeCb.toCriteria());

                    if (nodes.size() == 1) {
                        onmsNode = nodes.get(0);
                    }
                }
                addComponent(createAlarmComponent(onmsAlarm, onmsNode));

                if (onmsAlarm.getSeverity().isGreaterThanOrEqual(OnmsSeverity.get(boostSeverity))) {
                    boosted = true;
                }
            }
        }
        return boosted;
    }

    public String getHumanReadableFormat(long secondsAll) {
        long seconds = secondsAll;
        long minutes = 0;
        long hours = 0;

        if (seconds / 60 > 0) {
            long rest = seconds % 60;
            minutes = seconds / 60;
            seconds = rest;
        }

        if (minutes / 60 > 0) {
            long rest = minutes % 60;
            hours = minutes / 60;
            minutes = rest;
        }

        String output = "";

        if (hours > 0) {
            output = hours + "h, " + minutes + "m, " + seconds + "s";
        } else {
            if (minutes > 0) {
                output = minutes + "m, " + seconds + "s";
            } else {
                output = seconds + "s";
            }
        }

        return output + " ago";
    }

    public Component createAlarmComponent(OnmsAlarm onmsAlarm, OnmsNode onmsNode) {

        Calendar calendar = Calendar.getInstance();

        String ago = getHumanReadableFormat((calendar.getTimeInMillis() / 1000) - (onmsAlarm.getLastEventTime().getTime() / 1000));

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.addStyleName("node-label-black");
        horizontalLayout.addStyleName(onmsAlarm.getSeverity().name().toLowerCase());

        Label labelAgo = new Label();
        labelAgo.setSizeUndefined();
        labelAgo.addStyleName("node-font");
        labelAgo.setValue(ago);

        Label labelId = new Label();
        labelId.setSizeUndefined();
        labelId.addStyleName("node-font-black");
        if (onmsNode != null) {
            labelId.setValue(onmsNode.getLabel() + " (" + onmsNode.getNodeId() + ")");
        } else {
            labelId.setValue("-");
        }

        Label labelUei = new Label();
        labelUei.setSizeUndefined();
        labelUei.addStyleName("node-font-black");
        labelUei.setValue(onmsAlarm.getUei());

        horizontalLayout.addComponent(labelAgo);
        horizontalLayout.addComponent(labelId);
        horizontalLayout.addComponent(labelUei);

        horizontalLayout.setExpandRatio(labelAgo, 1.0f);
        horizontalLayout.setExpandRatio(labelId, 2.0f);
        horizontalLayout.setExpandRatio(labelUei, 4.0f);

        return horizontalLayout;
    }

    public String getName() {
        return "Alerts";
    }

    @Override
    public boolean isBoosted() {
        return updateAlarms();
    }
}

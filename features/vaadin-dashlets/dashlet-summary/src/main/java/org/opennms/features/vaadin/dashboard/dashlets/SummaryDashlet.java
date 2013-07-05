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
package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsSeverity;

/**
 * This class implements a {@link Dashlet} for testing purposes.
 *
 * @author Christian Pape
 */
public class SummaryDashlet extends VerticalLayout implements Dashlet {
    /**
     * The {@link AlarmDao} used
     */
    private AlarmDao m_alarmDao;
    /**
     * The {@link DashletSpec} for this instance
     */
    private DashletSpec m_dashletSpec;

    /**
     * Timeslot to use
     */
    private long timeslot = 3600;

    /**
     * Trend identifiers
     */
    private static int TREND_NORTH = 4;
    private static int TREND_NORTHEAST = 3;
    private static int TREND_EAST = 2;
    private static int TREND_SOUTHEAST = 1;
    private static int TREND_SOUTH = 0;

    /**
     * Constructor for instantiating new objects.
     *
     * @param dashletSpec the {@link DashletSpec} to be used
     */
    public SummaryDashlet(DashletSpec dashletSpec, AlarmDao alarmDao) {
        m_dashletSpec = dashletSpec;
        m_alarmDao = alarmDao;
        setCaption(getName());
        setWidth("100%");

        update();
    }

    /**
     * Returns a human-readable {@link String} representation of a timestamp in the past.
     *
     * @param secondsAll the timestamp to be used
     * @return a human-readable representation
     */
    public String getHumanReadableFormat(long secondsAll) {
        long seconds = secondsAll;
        long minutes = 0;
        long hours = 0;
        long days = 0;

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

        if (hours / 24 > 0) {
            long rest = hours % 24;
            days = hours / 24;
            hours = rest;
        }

        String output = "";

        if (days > 0) {
            output += days + "d";
        }
        if (hours > 0) {
            output += hours + "h";
        }
        if (minutes > 0) {
            output += minutes + "m";
        }
        if (seconds > 0) {
            output += seconds + "s";
        }

        return output;
    }

    /**
     * Computes the trend for acknowledged and not acknowledged alarms
     *
     * @param ack    number of acknowledged alarms
     * @param notAck number of unacknowledged alarms
     * @return the trend value
     */
    private int computeTrend(int ack, int notAck) {
        if (ack == notAck) {
            return TREND_EAST;
        } else {
            if (notAck == 0) {
                return TREND_NORTH;
            } else {
                if (ack == 0) {
                    return TREND_NORTH;
                } else {
                    double ratio = (double) ack / (double) notAck;

                    if (ratio < 0.5) {
                        return TREND_SOUTH;
                    } else {
                        if (ratio < 1) {
                            return TREND_SOUTHEAST;
                        } else {
                            if (ratio > 2) {
                                return TREND_NORTH;
                            } else {
                                return TREND_NORTHEAST;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Updates the data and checks whether this dashlet is boosted.
     *
     * @return true, if boosted, false otherwise
     */
    private boolean update() {
        timeslot = 3600;

        try {
            timeslot = Math.max(1, Integer.parseInt(m_dashletSpec.getParameters().get("timeslot")));
        } catch (NumberFormatException numberFormatException) {
            /**
             * Just ignore
             */
        }

        removeAllComponents();

        injectStyles();

        int overallSum = 0;
        int severitySum = 0;

        for (OnmsSeverity onmsSeverity : OnmsSeverity.values()) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setSpacing(true);
            horizontalLayout.addStyleName("summary");
            horizontalLayout.addStyleName(onmsSeverity.name().toLowerCase());

            int acknowledged = countBySeverity(true, timeslot, onmsSeverity);
            int notAcknowledged = countBySeverity(false, timeslot, onmsSeverity);

            Label labelSeverity = new Label(onmsSeverity.getLabel());
            labelSeverity.addStyleName("summary-font");
            Label labelAcknowledge = new Label(String.valueOf(acknowledged));
            labelAcknowledge.addStyleName("summary-font");
            Label labelNotAcknowledged = new Label(String.valueOf(notAcknowledged));
            labelNotAcknowledged.addStyleName("summary-font");

            horizontalLayout.addComponent(labelSeverity);
            horizontalLayout.addComponent(labelAcknowledge);
            horizontalLayout.addComponent(labelNotAcknowledged);

            int status = computeTrend(acknowledged, notAcknowledged);

            severitySum += onmsSeverity.getId();
            overallSum += onmsSeverity.getId() * status;

            Image image = new Image(null, new ThemeResource("img/a" + status + ".png"));
            image.setWidth(32, Unit.PIXELS);
            horizontalLayout.addComponent(image);

            horizontalLayout.setExpandRatio(labelSeverity, 3.0f);
            horizontalLayout.setExpandRatio(labelAcknowledge, 1.0f);
            horizontalLayout.setExpandRatio(labelNotAcknowledged, 1.0f);

            horizontalLayout.setWidth(375, Unit.PIXELS);
            addComponent(horizontalLayout);
        }

        int globalTrend = (int) Math.max(0, Math.min(4, Math.round(((double) overallSum) / ((double) severitySum))));

        Image image = new Image(null, new ThemeResource("img/a" + globalTrend + ".png"));
        image.setWidth(256, Unit.PIXELS);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.addStyleName("summary");
        verticalLayout.addStyleName("global");
        verticalLayout.setSizeFull();

        Label label = new Label("Global trend (" + getHumanReadableFormat(timeslot) + ")");
        label.addStyleName("summary-font");
        label.setSizeUndefined();

        verticalLayout.addComponent(label);
        verticalLayout.addComponent(image);

        verticalLayout.setWidth(375, Unit.PIXELS);

        verticalLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

        verticalLayout.setExpandRatio(label, 1.0f);

        addComponent(verticalLayout, 0);

        return (globalTrend > 2);
    }

    /**
     * Injects CSS styles in the current page
     */
    private void injectStyles() {
        Page.getCurrent().getStyles().add(".summary.cleared { background: #000000; border-left: 15px solid #858585; }");
        Page.getCurrent().getStyles().add(".summary.normal { background: #000000; border-left: 15px solid #336600; }");
        Page.getCurrent().getStyles().add(".summary.indeterminate {  background: #000000; border-left: 15px solid #999; }");
        Page.getCurrent().getStyles().add(".summary.warning { background: #000000; border-left: 15px solid #FFCC00; }");
        Page.getCurrent().getStyles().add(".summary.minor { background: #000000;  border-left: 15px solid #FF9900; }");
        Page.getCurrent().getStyles().add(".summary.major { background: #000000; border-left: 15px solid #FF3300; }");
        Page.getCurrent().getStyles().add(".summary.critical { background: #000000; border-left: 15px solid #CC0000; }");
        Page.getCurrent().getStyles().add(".summary.global { background: #000000; border-left: 15px solid #000000; }");
        Page.getCurrent().getStyles().add(".summary { padding: 5px 5px; margin: 1px; }");
        Page.getCurrent().getStyles().add(".summary-font { font-size: 24px; line-height: normal; text-align: right; }");
    }

    /**
     * Searches for alarms with the given criterias and returns the number found.
     *
     * @param acknowledged search for acknowledged or unacknowledged alarms
     * @param age          the age of the alarms
     * @param onmsSeverity the {@link OnmsSeverity} to search for
     * @return number of alarms found
     */
    public int countBySeverity(boolean acknowledged, long age, OnmsSeverity onmsSeverity) {
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsAlarm.class);

        if (acknowledged) {
            criteriaBuilder.isNotNull("alarmAckUser");
        } else {
            criteriaBuilder.isNull("alarmAckUser");
        }

        criteriaBuilder.eq("severity", onmsSeverity);

        criteriaBuilder.sql("EXTRACT(EPOCH FROM CURRENT_TIMESTAMP - lastEventTime) < " + age);

        return m_alarmDao.countMatching(criteriaBuilder.toCriteria());
    }

    @Override
    public String getName() {
        return "Summary";
    }

    @Override
    public boolean isBoosted() {
        return update();
    }

}

package org.opennms.features.vaadin.dashboard.model;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
@XmlRootElement
public class DashletSpec {

    private int duration = 5;
    private int priority = 5;
    private int boostDuration = 5;
    private int boostPriority = 5;
    private String dashlet = "Undefined";
    private Map<String, String> parameters = new TreeMap<String, String>();

    public DashletSpec() {
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBoostDuration() {
        return boostDuration;
    }

    public void setBoostDuration(int boostDuration) {
        this.boostDuration = boostDuration;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getBoostPriority() {
        return boostPriority;
    }

    public void setBoostPriority(int boostPriority) {
        this.boostPriority = boostPriority;
    }

    public void setDashletName(String dashletName) {
        this.dashlet = dashletName;
    }

    public String getDashletName() {
        return dashlet;
    }

    @XmlElementWrapper(name = "parameters")
    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public DashletSpec clone() {
        DashletSpec dashletSpec = new DashletSpec();
        dashletSpec.setPriority(getPriority());
        dashletSpec.setDuration(getDuration());
        dashletSpec.setBoostPriority(getBoostPriority());
        dashletSpec.setBoostDuration(getBoostDuration());
        dashletSpec.setDashletName(getDashletName());
        return dashletSpec;
    }
}

package org.opennms.features.vaadin.dashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class Wallboard {
    private List<DashletSpec> dashletSpecs = new LinkedList<DashletSpec>();
    private String title;

    public Wallboard(Wallboard wallboard) {
        this.title = wallboard.getTitle();
        for (DashletSpec dashletSpec : wallboard.getDashletSpecs()) {
            this.dashletSpecs.add(dashletSpec.clone());
        }
    }

    public Wallboard() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlAttribute(name = "title")
    public String getTitle() {
        return title;
    }

    @XmlElement(name = "dashlet")
    @XmlElementWrapper(name = "dashlets")
    public List<DashletSpec> getDashletSpecs() {
        return dashletSpecs;
    }
}

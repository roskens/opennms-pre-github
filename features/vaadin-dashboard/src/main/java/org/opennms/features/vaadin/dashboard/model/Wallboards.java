package org.opennms.features.vaadin.dashboard.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Wallboards {
    private List<Wallboard> wallboards = new ArrayList<Wallboard>();

    public Wallboards() {
    }

    @XmlElement(name = "wallboard")
    public List<Wallboard> getWallboards() {
        return wallboards;
    }
}

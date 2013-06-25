package org.opennms.features.vaadin.dashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class SandwichBoard {
    private List<SandwichSpec> sandwiches = new LinkedList<SandwichSpec>();
    private String title;

    public SandwichBoard(SandwichBoard sandwichBoard) {
        this.title = sandwichBoard.getTitle();
        for (SandwichSpec sandwichSpec : sandwichBoard.getSandwiches()) {
            this.sandwiches.add(sandwichSpec.clone());
        }
    }

    public SandwichBoard() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlAttribute(name="title")
    public String getTitle() {
        return title;
    }

    @XmlElement(name = "dashletDefinition")
    @XmlElementWrapper(name = "dashletDefinitions")
    public List<SandwichSpec> getSandwiches() {
        return sandwiches;
    }
}

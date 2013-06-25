package org.opennms.features.vaadin.dashboard.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class SandwichBoards {
    private List<SandwichBoard> sandwichBoardList = new ArrayList<SandwichBoard>();

    public SandwichBoards() {
    }

    @XmlElement(name = "boardDefinition")
    public List<SandwichBoard> getSandwichBoards() {
        return sandwichBoardList;
    }
}

package org.opennms.features.vaadin.nodebrowser.search;

import java.util.List;
import org.opennms.features.vaadin.nodebrowser.model.Criteria;

public interface NodeSearch {
    public void search(List<Criteria> criterias);
}

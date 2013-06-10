package org.opennms.features.vaadin.nodebrowser.search;

import com.vaadin.data.Container;
import org.opennms.features.vaadin.nodebrowser.model.Criteria;

import java.util.List;

public interface SearchStrategy {
    public void search(List<Criteria> criterias);

    public Container getContainer();
}

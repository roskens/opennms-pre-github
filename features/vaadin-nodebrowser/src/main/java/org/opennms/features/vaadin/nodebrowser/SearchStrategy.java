package org.opennms.features.vaadin.nodebrowser;
import com.vaadin.data.Container;
import java.util.List;

public interface SearchStrategy {
    public void search(List<SearchCriteria> searchCriterias);
    public Container getContainer();
    public void refresh();
}

package org.opennms.features.vaadin.nodebrowser.columns;

import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;

public class NodeStatusColumnGenerator implements Table.ColumnGenerator {
    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        Property prop = source.getItem(itemId).getItemProperty(columnId);

        if (prop.getType().equals(Integer.class)) {
            if (prop.getValue().equals(0)) {
                return new Embedded("Normal", new ExternalResource("images/bgNormal.png"));
            }
            if (prop.getValue().equals(1)) {
                return new Embedded("Warning", new ExternalResource("images/bgWarning.png"));
            }
            if (prop.getValue().equals(2)) {
                return new Embedded("Critical", new ExternalResource("images/bgCritical.png"));
            }
        }
        return new Embedded("Unknown", new ExternalResource("images/bgCleared.png"));
    }
}

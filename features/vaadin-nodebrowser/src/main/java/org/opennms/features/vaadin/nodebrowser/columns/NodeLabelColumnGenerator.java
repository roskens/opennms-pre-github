package org.opennms.features.vaadin.nodebrowser.columns;

import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;

public class NodeLabelColumnGenerator implements Table.ColumnGenerator {
    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        Property prop = source.getItem(itemId).getItemProperty(columnId);

        if (prop.getType().equals(String.class)) {
            Link link = new Link(String.valueOf(prop.getValue()), new ExternalResource("element/node.jsp?node=" + itemId));
            link.setTargetName("_top");
            return link;
        }
        return null;
    }
}

package org.opennms.vaadin.applicationstack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mvrueden
 */
public class ApplicationStack {
    private static final int MAX_WIDTH = 1000;

    private final Map<String, ApplicationLayer> layers = new HashMap<String, ApplicationLayer>();

    private String label;

    public ApplicationStack(String label) {
        this.label = label;
    }

    public ApplicationStack addLayer(ApplicationLayer layer) {
        layers.put(layer.getLabel(), layer);
        return this;
    }

    public ApplicationStack removeLayer(ApplicationLayer layer) {
        layers.remove(layer.getLabel());
        return this;
    }

    public int getRowCount() {
        int rowCount = 0;
        for (ApplicationLayer eachLayer : layers.values()) {
            rowCount = Math.max(rowCount, eachLayer.getRow() + eachLayer.getHeight());
        }
        return rowCount;
    }

    public int getColumnCount() {
        int columnCount = 0;
        for (ApplicationLayer eachLayer : layers.values()) {
            columnCount = Math.max(columnCount, eachLayer.getColumn() + eachLayer.getWidth());
        }
        return columnCount;
    }

    public int computeWidth() {
        return MAX_WIDTH;
    }

    public int computeColumnWidth() {
        return MAX_WIDTH / getColumnCount();
    }

    public int computeRowHeight() {
        return 150;
    }

    public String getLabel() {
        return label;
    }

    public Iterable<ApplicationLayer> getLayers() {
        return new ArrayList<ApplicationLayer>(layers.values());
    }

    public ApplicationStack registerNode(final String layerId, NodeDummy node) {
        layers.get(layerId).registerNode(node);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void refreshLayers() {
        Iterable<ApplicationLayer> tmpLayers = getLayers();
        this.layers.clear();
        for (ApplicationLayer eachLayer : tmpLayers) {
            addLayer(eachLayer);
        }
    }
}

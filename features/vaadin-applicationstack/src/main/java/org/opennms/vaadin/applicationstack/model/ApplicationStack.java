package org.opennms.vaadin.applicationstack.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author mvrueden
 */
@XmlRootElement
public class ApplicationStack {
    private static final int MAX_WIDTH = 1000;

    private List<ApplicationLayer> layers = new ArrayList<ApplicationLayer>();

    private String label;

    public ApplicationStack() {
        
    }
    
    public ApplicationStack(String label) {
        this.label = label;
    }

    public ApplicationStack addLayer(ApplicationLayer layer) {
        layers.add(layer);
        return this;
    }

    public ApplicationStack removeLayer(ApplicationLayer layer) {
        layers.remove(layer);
        return this;
    }

    public int getRowCount() {
        int rowCount = 0;
        for (ApplicationLayer eachLayer : layers) {
            rowCount = Math.max(rowCount, eachLayer.getRow() + eachLayer.getHeight());
        }
        return rowCount;
    }

    public int getColumnCount() {
        int columnCount = 0;
        for (ApplicationLayer eachLayer : layers) {
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

    public List<ApplicationLayer> getLayers() {
        return layers;
    }

    // TODO remove or change method to criteria and layer (layerId must go)
    public ApplicationStack registerNode(final String layerId, NodeDummy node) {
//        layers.get(lget(layerId).registerNode(node);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLayers(List<ApplicationLayer> layers) {
        this.layers = layers;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}

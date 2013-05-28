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
public class ApplicationLayer {

    private int width;
    private int height;
    private int column;
    private int row;
    private String label;
//    private final List<NodeDummy> nodes = new ArrayList<NodeDummy>();
    private List<Criteria> criterias = new ArrayList<Criteria>();

    public ApplicationLayer() {
        
    }
    
    public ApplicationLayer(String label, int row, int column, int width, int height) {
        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
        this.label = label;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLabel() {
        return label;
    }

    public Coordinates getCoordinates() {
        return new Coordinates(column, row, column + width - 1, row + height - 1);
    }

    // TODO remove this method
    protected ApplicationLayer registerNode(NodeDummy node) {
//        nodes.add(node);
        return this;
    }

    // TODO implement computeGood
    public float computeGood() {
        int good = 0;
        return 0;
//        int sum = getSum();
//        for (NodeDummy eachNode : nodes) {
//            good += eachNode.getGood();
//        }
//        return good == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) good;
    }

    // TODO implement computeDeath
    public float computeDeath() {
        int death = 0;
        return 0;
//        int sum = getSum();
//        for (NodeDummy eachNode : nodes) {
//            death += eachNode.getDeath();
//        }
//        return death == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) death;
    }

    // TODO implement computeProblems
    public float computeProblems() {
        int problem = 0;
        return 0;
//        int sum = getSum();
//        for (NodeDummy eachNode : nodes) {
//            problem += eachNode.getProblems();
//        }
//        return problem == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) problem;
    }

    public int getSum() {
        int sum = 0;
//        for (NodeDummy eachNode : nodes) {
//            sum += eachNode.getSum();
//        }
        return sum;
    }

    public void setLabel(String label) {
        this.label = label;
    }

//    public Iterable<NodeDummy> getNodes() {
//        return new ArrayList<NodeDummy>(nodes);
//    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }

    public ApplicationLayer addCriteria(Criteria criteria) {
        criterias.add(criteria);
        return this;
    }

    public void removeCriteria(Criteria criteria) {
        criterias.remove(criteria);
    }

    public void removeAllCriterias() {
        criterias.clear();
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setWidth(int width) {
        this.width = width;
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

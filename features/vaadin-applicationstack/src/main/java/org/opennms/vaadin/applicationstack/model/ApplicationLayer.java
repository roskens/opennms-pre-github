package org.opennms.vaadin.applicationstack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mvrueden
 */
public class ApplicationLayer {

    private int width;
    private int height;
    private int xPos;
    private int yPos;
    private String label;
    private final List<NodeDummy> nodes = new ArrayList<NodeDummy>();
    private List<Criteria> criterias = new ArrayList<Criteria>();

    public ApplicationLayer(String label, int row, int column, int width, int height) {
        this.yPos = row;
        this.xPos = column;
        this.width = width;
        this.height = height;
        this.label = label;
    }

    public int getRow() {
        return yPos;
    }

    public int getColumn() {
        return xPos;
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
        return new Coordinates(xPos, yPos, xPos + width - 1, yPos + height - 1);
    }

    protected ApplicationLayer registerNode(NodeDummy node) {
        nodes.add(node);
        return this;
    }

    public float computeGood() {
        int good = 0;
        int sum = getSum();
        for (NodeDummy eachNode : nodes) {
            good += eachNode.getGood();
        }
        return good == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) good;
    }

    public float computeDeath() {
        int death = 0;
        int sum = getSum();
        for (NodeDummy eachNode : nodes) {
            death += eachNode.getDeath();
        }
        return death == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) death;
    }

    public float computeProblems() {
        int problem = 0;
        int sum = getSum();
        for (NodeDummy eachNode : nodes) {
            problem += eachNode.getProblems();
        }
        return problem == 0 || sum == 0 ? 0 : (float) 100 / (float) sum * (float) problem;
    }

    public int getSum() {
        int sum = 0;
        for (NodeDummy eachNode : nodes) {
            sum += eachNode.getSum();
        }
        return sum;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Iterable<NodeDummy> getNodes() {
        return new ArrayList<NodeDummy>(nodes);
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }

    public void addCriteria(Criteria criteria) {
        criterias.add(criteria);
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
}

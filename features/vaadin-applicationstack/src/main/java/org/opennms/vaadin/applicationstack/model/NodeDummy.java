package org.opennms.vaadin.applicationstack.model;

/**
 *
 * @author mvrueden
 */
public class NodeDummy {
    private String label;
    
    private int good;
    private int death;
    private int problems;
    
    public NodeDummy(String label, int good, int death, int problems) {
        this.label = label;
        this.good = good;
        this.death = death;
        this.problems = problems;
    }

    public int getDeath() {
        return death;
    }

    public int getGood() {
        return good;
    }

    public String getLabel() {
        return label;
    }

    public int getProblems() {
        return problems;
    }
    
    public int getSum() {
        return good + death + problems;
    }
}

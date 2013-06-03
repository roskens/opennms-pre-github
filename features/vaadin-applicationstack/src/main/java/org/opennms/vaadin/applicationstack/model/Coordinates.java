package org.opennms.vaadin.applicationstack.model;

/**
 *
 * @author mvrueden
 */
public class Coordinates {

    public final int column1;
    public final int column2;
    public final int row1;
    public final int row2;
    
    protected Coordinates(int column1, int row1, int column2, int row2) {
        this.column1 = column1;
        this.row1 = row1;
        this.column2 = column2;
        this.row2 = row2;
    }
}

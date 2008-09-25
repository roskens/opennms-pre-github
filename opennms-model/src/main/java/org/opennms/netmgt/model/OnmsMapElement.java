package org.opennms.netmgt.model;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "element")
public class OnmsMapElement implements Serializable {
    public static final String MAP_TYPE = "M";
    public static final String NODE_TYPE = "N";
    public static final String defaultNodeIcon = "unspecified";
    public static final String defaultMapIcon = "map";

    private int id;

    private int mapId;

    protected String type;

    private String label;

    private String iconName;

    private int x;

    private int y;

    protected OnmsMapElement() {
        // blank
    }

    public OnmsMapElement(OnmsMapElement e) {
        this(e.mapId, e.id, e.type, e.label, e.iconName, e.x, e.y);
    }

    public OnmsMapElement(int mapId, int id, String type, String label,
            String iconName, int x, int y) {
        this.mapId = mapId;
        this.id = id;
        setType(type);
        this.label = label;
        setIconName(iconName);
        this.x = x;
        this.y = y;
    }

    @Id
    @Column(name="elementId")
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "mapId")
    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    @Column(name = "elementType")
    public String getType() {
        return type;
    }

    // TODO the original code throws an exception. I just don't set a value if its invalid.
    public void setType(String type) {
        if (type.equals(MAP_TYPE) || type.equals(NODE_TYPE))
            this.type = type;
    }

    @Column(name = "elementLabel")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "elementIcon")
    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        if(iconName==null){
    		iconName=defaultNodeIcon;
    	}
        this.iconName = iconName;
    }

    @Column(name = "elementX")
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Column(name = "elementY")
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

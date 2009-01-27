package org.opennms.netmgt.model.inventory;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlID;
import javax.persistence.*;

@XmlRootElement(name = "inventoryCategory")
@Entity
@Table(name = "inventorycategory")
public class OnmsInventoryCategory {

    @Id
    @Column(name="id")
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    @XmlTransient
    private int id;

    @Column(name = "categoryname")
    private String categoryName;

    protected OnmsInventoryCategory() {
        // blank
    }

    public OnmsInventoryCategory(OnmsInventoryCategory cat) {
        this(cat.categoryName, cat.id);
    }

    public OnmsInventoryCategory(String categoryName, int  id) {
        this.categoryName = categoryName;
        this.id = id;        
    }

    public OnmsInventoryCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    @XmlID
    @Transient
    public String getCategoryId() {
        return Integer.toString(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

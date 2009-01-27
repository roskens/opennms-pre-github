package org.opennms.netmgt.model.inventory;

import org.opennms.netmgt.model.OnmsNode;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlID;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;
import java.util.LinkedHashSet;

@XmlRootElement(name = "inventoryAsset")
@Entity
@Table(name = "inventoryasset")
public class OnmsInventoryAsset {

    @Id
    @Column(name="id")
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    @XmlTransient
    private int id;

    @XmlTransient
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="category")
    private OnmsInventoryCategory category;

    @Column(name = "assetName")
    private String assetName;

    @Column(name = "assetSource")
    private String assetSource;

    @XmlTransient
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="ownerNode")
    private OnmsNode ownerNode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateAdded")
    private Date dateAdded;

    @XmlTransient
    @OneToMany(mappedBy="inventoryAsset")
    @org.hibernate.annotations.Cascade( {
        org.hibernate.annotations.CascadeType.ALL,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    private Set<OnmsInventoryAssetProperty> properties = new LinkedHashSet<OnmsInventoryAssetProperty>();

    public OnmsInventoryAsset() {
        // do nothing.
    }
    
    public OnmsInventoryAsset(String assetName, OnmsInventoryCategory category, OnmsNode ownerNode) {
        this.assetName = assetName;
        this.category = category;
        this.ownerNode = ownerNode;
        this.dateAdded = new Date();
        this.assetSource = "Invd";
    }
    
    public int getId() {
        return id;
    }

    @XmlID
    @Transient
    public String getAssetId() {
        return Integer.toString(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public OnmsInventoryCategory getCategory() {
        return category;
    }

    public void setCategory(OnmsInventoryCategory category) {
        this.category = category;
    }

    public String getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
    }

    public OnmsNode getOwnerNode() {
        return ownerNode;
    }

    public void setOwnerNode(OnmsNode ownerNode) {
        this.ownerNode = ownerNode;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Set<OnmsInventoryAssetProperty> getProperties() {
        return properties;
    }

    public void setProperties(Set<OnmsInventoryAssetProperty> properties) {
        this.properties = properties;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
}

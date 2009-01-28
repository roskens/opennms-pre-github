package org.opennms.netmgt.model.inventory;

import org.opennms.netmgt.model.OnmsNode;
import org.springframework.core.style.ToStringCreator;

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

    /**
     * Default constructor.
     */
    public OnmsInventoryAsset() {
        // do nothing.
    }

    /**
     * Constructor.
     *
     * This constructor defaults dateAdded to the current time and
     * assetSource to "Invd."
     *
     * @param category The inventory category this asset belongs to.
     * @param assetName The name of this asset.
     * @param ownerNode The node this asset belongs to.
     */
    public OnmsInventoryAsset(OnmsInventoryCategory category,
                              String assetName,
                              OnmsNode ownerNode) {
        this.assetName = assetName;
        this.category = category;
        this.ownerNode = ownerNode;
        this.dateAdded = new Date();
        this.assetSource = "Invd";
    }

    /**
     * Constructor.
     *
     * This constructor requires all members except asset properties.
     * No member values are defaulted.
     *
     * @param category The inventory category this asset belongs to.
     * @param assetName The name of this asset.
     * @param assetSource The source of this asset, e.g "Invd" or "User"
     * @param ownerNode The node this asset belongs to.
     * @param dateAdded The date that this asset was added or changed.
     */     
    public OnmsInventoryAsset(OnmsInventoryCategory category,
                              String assetName,
                              String assetSource,
                              OnmsNode ownerNode,
                              Date dateAdded) {
        this.category = category;
        this.assetName = assetName;
        this.assetSource = assetSource;
        this.ownerNode = ownerNode;
        this.dateAdded = dateAdded;

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

    public boolean addCategory(OnmsInventoryAssetProperty prop) {
        return getProperties().add(prop);
    }

    public boolean removeCategory(OnmsInventoryAssetProperty prop) {
        return getProperties().remove(prop);
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String toString() {
        return new ToStringCreator(this)
            .append("id", getId())
            .append("name", getAssetName())
            .toString();
    }
}

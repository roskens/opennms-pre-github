package org.opennms.netmgt.model.inventory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "inventoryAssetProperty")
@Entity
@Table(name = "inventoryassetproperty")
public class OnmsInventoryAssetProperty {
    @Id
    @Column(name="id")
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    @XmlTransient
    private int id;

    @XmlTransient
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="inventoryAsset")
    private OnmsInventoryAsset inventoryAsset;

    @Column(name = "assetKey")
    private String assetKey;

    @Column(name = "assetValue")
    private String assetValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateAdded")
    private Date dateAdded;

    public OnmsInventoryAssetProperty() {
        // do nothing.
    }

    public OnmsInventoryAssetProperty(OnmsInventoryAsset inventoryAsset,
                                      String assetKey,
                                      String assetValue) {
        this.inventoryAsset = inventoryAsset;
        this.assetKey = assetKey;
        this.assetValue = assetValue;
        this.dateAdded = new Date();
    }

    public OnmsInventoryAssetProperty(OnmsInventoryAsset inventoryAsset,
                                      String assetKey,
                                      String assetValue,
                                      Date dateAdded) {
        this.inventoryAsset = inventoryAsset;
        this.assetKey = assetKey;
        this.assetValue = assetValue;
        this.dateAdded = dateAdded;
    }
    
    public int getId() {
        return id;
    }

    @XmlID
    @Transient
    public String getAssetPropertyId() {
        return Integer.toString(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public OnmsInventoryAsset getInventoryAsset() {
        return inventoryAsset;
    }

    public void setInventoryAsset(OnmsInventoryAsset inventoryAsset) {
        this.inventoryAsset = inventoryAsset;
    }

    public String getAssetKey() {
        return assetKey;
    }

    public void setAssetKey(String assetKey) {
        this.assetKey = assetKey;
    }

    public String getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(String assetValue) {
        this.assetValue = assetValue;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}

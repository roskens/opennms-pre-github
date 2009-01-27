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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import java.util.Date;

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

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id")
    @XmlIDREF
    private OnmsInventoryAsset asset;

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

    public OnmsInventoryAsset getAsset() {
        return asset;
    }

    public void setAsset(OnmsInventoryAsset asset) {
        this.asset = asset;
    }
}

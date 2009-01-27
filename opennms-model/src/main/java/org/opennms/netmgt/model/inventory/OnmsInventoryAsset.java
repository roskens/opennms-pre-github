package org.opennms.netmgt.model.inventory;

import org.opennms.netmgt.model.OnmsNode;

import java.util.Date;

public class OnmsInventoryAsset {
    private int id;
    private OnmsInventoryCategory categoryOnms;
    private String assetSource;
    private String assetKey;
    private String assetValue;
    private OnmsNode ownerNode;
    private Date dateAdded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OnmsInventoryCategory getCategory() {
        return categoryOnms;
    }

    public void setCategory(OnmsInventoryCategory categoryOnms) {
        this.categoryOnms = categoryOnms;
    }

    public String getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
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
}

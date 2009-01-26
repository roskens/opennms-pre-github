package org.opennms.netmgt.model.inventory;

import org.opennms.netmgt.model.OnmsNode;

public class InventoryAsset {
    private int id;
    private InventoryCategory category;
    private String assetSource;
    private String assetKey;
    private String assetValue;
    private OnmsNode ownerNode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InventoryCategory getCategory() {
        return category;
    }

    public void setCategory(InventoryCategory category) {
        this.category = category;
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
}

package com.uwaterloo.fydp.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Bin implements Serializable {
    private int id;
    private int binSystemId;
    private String name;
    private String description;
    private String imageUrl;
    private String purchaseUrl;
    private double massPerItem;
    private double totalMass;
    private double currentMass;
    private Timestamp createdTimestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBinSystemId() {
        return binSystemId;
    }

    public void setBinSystemId(int binSystemId) {
        this.binSystemId = binSystemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(String purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public double getMassPerItem() {
        return massPerItem;
    }

    public void setMassPerItem(double massPerItem) {
        this.massPerItem = massPerItem;
    }

    public double getTotalMass() {
        return totalMass;
    }

    public void setTotalMass(double totalMass) {
        this.totalMass = totalMass;
    }

    public double getCurrentMass() {
        return currentMass;
    }

    public void setCurrentMass(double currentMass) {
        this.currentMass = currentMass;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}

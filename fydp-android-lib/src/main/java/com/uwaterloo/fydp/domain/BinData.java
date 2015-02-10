package com.uwaterloo.fydp.domain;

import java.sql.Timestamp;

public class BinData {

    private int id;
    private int binSystemId;
    private int binId;
    private double data;
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

    public int getBinId() {
        return binId;
    }

    public void setBinId(int binId) {
        this.binId = binId;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}

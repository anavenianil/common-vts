package com.affluencesystems.insurancetelematics.common.Models;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class DataModel {

    private int id;
    public int icon;
    public String name;

    // Constructor.
    public DataModel(int id, int icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

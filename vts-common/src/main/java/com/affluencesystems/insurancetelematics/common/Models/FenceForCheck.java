package com.affluencesystems.insurancetelematics.common.Models;

public class FenceForCheck {

    private String id, name;
    private boolean isSelected;

    public FenceForCheck(String id, String name, boolean isSelected) {
        this.name = name;
        this.id = id;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

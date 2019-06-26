package com.affluencesystems.insurancetelematics.common.Models;

import java.util.ArrayList;

public class ConfigSinglePacket {

    private ArrayList<String> geoFenceIds;
    private String parameterName, parameterType, parameterValue;

    public ConfigSinglePacket(ArrayList<String> geoFenceIds, String parameterName, String parameterType, String parameterValue) {
        this.geoFenceIds = geoFenceIds;
        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
    }

    public ArrayList<String> getGeoFenceIds() {
        return geoFenceIds;
    }

    public void setGeoFenceIds(ArrayList<String> geoFenceIds) {
        this.geoFenceIds = geoFenceIds;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}

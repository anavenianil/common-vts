package com.affluencesystems.insurancetelematics.common.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class GeoFenceResponse {
    private String imeiNumber, fenceId, fenceName;
    private ArrayList<RadiusLatLng> radiusLatAndLong;
    private ArrayList<LatLng> customLatAndLong;
    private String latLongUpdateDate;
    private String organizationId;

    public GeoFenceResponse(String imeiNumber, String fenceName, ArrayList<RadiusLatLng> radiusLatAndLong, ArrayList<LatLng> customLatAndLong, String latLongUpdateDate, String organizationId) {
        this.imeiNumber = imeiNumber;
        this.fenceName = fenceName;
        this.radiusLatAndLong = radiusLatAndLong;
        this.customLatAndLong = customLatAndLong;
        this.latLongUpdateDate = latLongUpdateDate;
        this.organizationId = organizationId;
    }

    public GeoFenceResponse(String fenceId, String fenceName, String imeiNumber, ArrayList<RadiusLatLng> radiusLatAndLong, ArrayList<LatLng> customLatAndLong, String latLongUpdateDate, String organizationId) {
        this.fenceId = fenceId;
        this.fenceName = fenceName;
        this.imeiNumber = imeiNumber;
        this.radiusLatAndLong = radiusLatAndLong;
        this.customLatAndLong = customLatAndLong;
        this.latLongUpdateDate = latLongUpdateDate;
        this.organizationId = organizationId;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public ArrayList<RadiusLatLng> getRadiusLatAndLong() {
        return radiusLatAndLong;
    }

    public void setRadiusLatAndLong(ArrayList<RadiusLatLng> radiusLatAndLong) {
        this.radiusLatAndLong = radiusLatAndLong;
    }

    public ArrayList<LatLng> getCustomLatAndLong() {
        return customLatAndLong;
    }

    public void setCustomLatAndLong(ArrayList<LatLng> customLatAndLong) {
        this.customLatAndLong = customLatAndLong;
    }

    public String getLatLongUpdateDate() {
        return latLongUpdateDate;
    }

    public void setLatLongUpdateDate(String latLongUpdateDate) {
        this.latLongUpdateDate = latLongUpdateDate;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getFenceId() {
        return fenceId;
    }

    public void setFenceId(String fenceId) {
        this.fenceId = fenceId;
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }
}

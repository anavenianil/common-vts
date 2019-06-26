package com.affluencesystems.insurancetelematics.common.Models;

import java.util.ArrayList;

public class DeviceConfig {

    private String imeiNumber, configType, overSpeedLimitThreshold, deviceName, harshBrakingThreshold, harshAcclerationThreshold, rashTurningThreshold, endDate, organizationId;
    private ArrayList<String> tokenId, fenceId;

    public DeviceConfig(String imeiNumber, String configType, String overSpeedLimitThreshold, String deviceName, String harshBrakingThreshold, String harshAcclerationThreshold, String rashTurningThreshold, String endDate, ArrayList<String> tokenId, String organizationId, ArrayList<String> fenceId) {
        this.imeiNumber = imeiNumber;
        this.configType = configType;
        this.overSpeedLimitThreshold = overSpeedLimitThreshold;
        this.deviceName = deviceName;
        this.harshBrakingThreshold = harshBrakingThreshold;
        this.harshAcclerationThreshold = harshAcclerationThreshold;
        this.rashTurningThreshold = rashTurningThreshold;
        this.endDate = endDate;
        this.tokenId = tokenId;
        this.organizationId = organizationId;
        this.fenceId = fenceId;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getOverSpeedLimitThreshold() {
        return overSpeedLimitThreshold;
    }

    public void setOverSpeedLimitThreshold(String overSpeedLimitThreshold) {
        this.overSpeedLimitThreshold = overSpeedLimitThreshold;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getHarshBrakingThreshold() {
        return harshBrakingThreshold;
    }

    public void setHarshBrakingThreshold(String harshBrakingThreshold) {
        this.harshBrakingThreshold = harshBrakingThreshold;
    }

    public String getHarshAcclerationThreshold() {
        return harshAcclerationThreshold;
    }

    public void setHarshAcclerationThreshold(String harshAcclerationThreshold) {
        this.harshAcclerationThreshold = harshAcclerationThreshold;
    }

    public String getRashTurningThreshold() {
        return rashTurningThreshold;
    }

    public void setRashTurningThreshold(String rashTurningThreshold) {
        this.rashTurningThreshold = rashTurningThreshold;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getTokenId() {
        return tokenId;
    }

    public void setTokenId(ArrayList<String> tokenId) {
        this.tokenId = tokenId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public ArrayList<String> getFenceId() {
        return fenceId;
    }

    public void setFenceId(ArrayList<String> fenceId) {
        this.fenceId = fenceId;
    }
}

package com.affluencesystems.insurancetelematics.common.Models;

public class Notifications {
   private String imeiNumber, typeOfAlert, dateTime,sourceIp,sourceMobileNumber,scale,latitude,latituduDirection,longitude,longDirection,speed,organizationId;

    public Notifications(String imeiNumber, String typeOfAlert, String dateTime, String sourceIp, String sourceMobileNumber, String scale, String latitude, String latituduDirection, String longitude, String longDirection, String speed, String organizationId) {
        this.imeiNumber = imeiNumber;
        this.typeOfAlert = typeOfAlert;
        this.dateTime = dateTime;
        this.sourceIp = sourceIp;
        this.sourceMobileNumber = sourceMobileNumber;
        this.scale = scale;
        this.latitude = latitude;
        this.latituduDirection = latituduDirection;
        this.longitude = longitude;
        this.longDirection = longDirection;
        this.speed = speed;
        this.organizationId = organizationId;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getTypeOfAlert() {
        return typeOfAlert;
    }

    public void setTypeOfAlert(String typeOfAlert) {
        this.typeOfAlert = typeOfAlert;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getSourceMobileNumber() {
        return sourceMobileNumber;
    }

    public void setSourceMobileNumber(String sourceMobileNumber) {
        this.sourceMobileNumber = sourceMobileNumber;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatituduDirection() {
        return latituduDirection;
    }

    public void setLatituduDirection(String latituduDirection) {
        this.latituduDirection = latituduDirection;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongDirection() {
        return longDirection;
    }

    public void setLongDirection(String longDirection) {
        this.longDirection = longDirection;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}

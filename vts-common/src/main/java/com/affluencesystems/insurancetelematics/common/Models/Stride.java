package com.affluencesystems.insurancetelematics.common.Models;

import java.util.ArrayList;

public class Stride {

    private String primaryKey, latitude, longitude, averageCanSpeed, /*heading,*/ startDate, endDate, startLatitude, startlongitude, millage, travelledTime, distanceSinceStrideON;

    public Stride(String primaryKey, String latitude, String longitude, String averageCanSpeed, String startDate, String endDate, String startLatitude, String startlongitude, String millage, String travelledTime, String distanceSinceStrideON) {
        this.primaryKey = primaryKey;
        this.latitude = latitude;
        this.longitude = longitude;
        this.averageCanSpeed = averageCanSpeed;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startLatitude = startLatitude;
        this.startlongitude = startlongitude;
        this.millage = millage;
        this.travelledTime = travelledTime;
        this.distanceSinceStrideON = distanceSinceStrideON;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAverageCanSpeed() {
        return averageCanSpeed;
    }

    public void setAverageCanSpeed(String averageCanSpeed) {
        this.averageCanSpeed = averageCanSpeed;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartlongitude() {
        return startlongitude;
    }

    public void setStartlongitude(String startlongitude) {
        this.startlongitude = startlongitude;
    }

    public String getMillage() {
        return millage;
    }

    public void setMillage(String millage) {
        this.millage = millage;
    }

    public String getTravelledTime() {
        return travelledTime;
    }

    public void setTravelledTime(String travelledTime) {
        this.travelledTime = travelledTime;
    }

    public String getDistanceSinceStrideON() {
        return distanceSinceStrideON;
    }

    public void setDistanceSinceStrideON(String distanceSinceStrideON) {
        this.distanceSinceStrideON = distanceSinceStrideON;
    }
}

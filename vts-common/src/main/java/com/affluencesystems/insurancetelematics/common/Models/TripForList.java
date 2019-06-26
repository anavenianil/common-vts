package com.affluencesystems.insurancetelematics.common.Models;

import java.util.ArrayList;

public class TripForList {

    private String tripName, tripStartDate, tripEndDate, imeiNumber, tripStatus, driverSlNo, distanceTravel, fuelConsumed, timeDuration;
    private ArrayList<TripPacket> latlong;

    public TripForList(String tripName, String tripStartDate, String tripEndDate, String imeiNumber, String tripStatus, String driverSlNo, String distanceTravel, String fuelConsumed, String timeDuration, ArrayList<TripPacket> latlong) {
        this.tripName = tripName;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.imeiNumber = imeiNumber;
        this.tripStatus = tripStatus;
        this.driverSlNo = driverSlNo;
        this.distanceTravel = distanceTravel;
        this.fuelConsumed = fuelConsumed;
        this.timeDuration =timeDuration;
        this.latlong = latlong;
    }

    public String getDistanceTravel() {
        return distanceTravel;
    }

    public void setDistanceTravel(String distanceTravel) {
        this.distanceTravel = distanceTravel;
    }

    public String getFuelConsumed() {
        return fuelConsumed;
    }

    public void setFuelConsumed(String fuelConsumed) {
        this.fuelConsumed = fuelConsumed;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(String tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public String getTripEndDate() {
        return tripEndDate;
    }

    public void setTripEndDate(String tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getDriverSlNo() {
        return driverSlNo;
    }

    public void setDriverSlNo(String driverSlNo) {
        this.driverSlNo = driverSlNo;
    }

    public ArrayList<TripPacket> getLatlong() {
        return latlong;
    }

    public void setLatlong(ArrayList<TripPacket> latlong) {
        this.latlong = latlong;
    }
}

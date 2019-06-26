package com.affluencesystems.insurancetelematics.common.Models;

public class TravelDetails {

    private String drivenTime, distanceTravel, costOfDrive, fuelConsumend, dateTime;

    public TravelDetails(String drivenTime, String distanceTravel, String costOfDrive, String fuelConsumend, String dateTime) {
        this.drivenTime = drivenTime;
        this.distanceTravel = distanceTravel;
        this.costOfDrive = costOfDrive;
        this.fuelConsumend = fuelConsumend;
        this.dateTime = dateTime;
    }

    public String getDrivenTime() {
        return drivenTime;
    }

    public void setDrivenTime(String drivenTime) {
        this.drivenTime = drivenTime;
    }

    public String getDistanceTravel() {
        return distanceTravel;
    }

    public void setDistanceTravel(String distanceTravel) {
        this.distanceTravel = distanceTravel;
    }

    public String getCostOfDrive() {
        return costOfDrive;
    }

    public void setCostOfDrive(String costOfDrive) {
        this.costOfDrive = costOfDrive;
    }

    public String getFuelConsumend() {
        return fuelConsumend;
    }

    public void setFuelConsumend(String fuelConsumend) {
        this.fuelConsumend = fuelConsumend;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

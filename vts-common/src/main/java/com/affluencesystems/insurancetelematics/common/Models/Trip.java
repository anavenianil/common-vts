package com.affluencesystems.insurancetelematics.common.Models;

public class Trip {
    private String tripName, tripStartDate, tripEndDate, tripStatus, imeiNumber/*, driverSlNo, latlong*/;

    public Trip(String tripName, String tripStartDate, String tripEndDate, String tripStatus, String imeiNumber/*, String latlong*/) {
        this.tripName = tripName;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.tripStatus = tripStatus;
        this.imeiNumber = imeiNumber;
//        this.latlong = latlong;
    }

  /*  public Trip(String tripName, String tripStartDate, String tripEndDate, String tripStatus, String imeiNumber, String driverSlNo) {
        this.tripName = tripName;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.tripStatus = tripStatus;
        this.imeiNumber = imeiNumber;
        this.driverSlNo = driverSlNo;
    }*/

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

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

//    public String getDriverSlNo() {
//        return driverSlNo;
//    }
//
//    public void setDriverSlNo(String driverSlNo) {
//        this.driverSlNo = driverSlNo;
//    }
}

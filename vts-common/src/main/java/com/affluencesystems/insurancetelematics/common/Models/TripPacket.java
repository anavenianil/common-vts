package com.affluencesystems.insurancetelematics.common.Models;

public class TripPacket {

    private String latitude, longitude, speed, engineRpm, ignition;

    public TripPacket(String latitude, String longitude, String speed, String engineRpm, String ignition) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.engineRpm = engineRpm;
        this.ignition = ignition;
    }

    public String getIgnition() {
        return ignition;
    }

    public void setIgnition(String ignition) {
        this.ignition = ignition;
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

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getEngineRpm() {
        return engineRpm;
    }

    public void setEngineRpm(String engineRpm) {
        this.engineRpm = engineRpm;
    }
}

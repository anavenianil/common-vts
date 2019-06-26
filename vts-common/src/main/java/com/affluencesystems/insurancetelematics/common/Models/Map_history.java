/*****************************************************************************
 * Copyright (C) 2018, Affluence Infosystems Pvt Ltd.
 *  All Rights Reserved.
 *
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of Affluence Infosystems,
 * the contents of this file may not be disclosed to third parties, copied
 * or duplicated in any form, in whole or in part, without the prior
 * written permission of Affluence Infosystems.
 *
 *
 * File Name:  Map_history.java
 *
 * Description: This file used  to set and get data for map history.
 *
 * Routines in this file:
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      Shiva        1.0         31/10/2018
 *    Description: This file used  to set and get data for map history.

 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.Models;

import com.google.gson.annotations.SerializedName;

public class Map_history {
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("speed")
    String speed;
    @SerializedName("heading")
    String heading;
    @SerializedName("accelerometer")
    String accelerometer;

    @SerializedName("fuelConsumption")
    float fuelConsumption;

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

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getAccelerometer() {
        return accelerometer;
    }

    public float getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(float fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public void setAccelerometer(String accelerometer) {
        this.accelerometer = accelerometer;
    }

}

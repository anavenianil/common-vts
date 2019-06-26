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
 * File Name:  CheckActivityStatus.java
 *
 * Description: This file is used to test if the activity is ruuning or not if the activty is running values set to true other wise  set to false.
 *
 * Routines in this file:
 *     isSpeedManagementVisible()
 *     SpeedManagementResumed()
 *     SpeedManagementPaused()
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      Shiva        1.0         20/10/2018
 *    Description:     boolean values are set to true  if the activity is running.

 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.Utils;

import android.app.Application;

public class CheckActivityStatus extends Application {

    public static boolean speedManagementVisible;
    public static boolean fuelManagementVisible;
    public static boolean homeScreenVisible;
    public static boolean lightFragmentVisible;
    public static boolean travelPathVisible;
    public static boolean geoFencingVisisble;
    public static boolean carHealthVisisble;
    public static boolean live_trackingVisible;


    //Speed Management
    public static boolean isSpeedManagementVisible() {
        return speedManagementVisible;
    }

    public static void SpeedManagementResumed() {
        speedManagementVisible = true;
    }

    public static void SpeedManagementPaused() {
        speedManagementVisible = false;
    }

    //fuel Management
    public static boolean isFuelManagementVisible() {
        return fuelManagementVisible;
    }

    public static void FuelManagementResumed() {
        fuelManagementVisible = true;
    }

    public static void FuelManagementPaused() {
        fuelManagementVisible = false;
    }

    //vehicle Management
    public static boolean isCarHealthVisible() {
        return carHealthVisisble;
    }

    public static void CarHealthResumed() {
        carHealthVisisble = true;
    }

    public static void CarHealthPaused() {
        carHealthVisisble = false;
    }


    //home Screen
    public static boolean isHomeScreenVisible() {
        return homeScreenVisible;
    }

    public static void HomeScreenVisibleResumed() {
        homeScreenVisible = true;
    }

    public static void HomeScreenVisiblePaused() {
        homeScreenVisible = false;
    }


    //travel path
    public static boolean isTravelPathScreenVisible() {
        return travelPathVisible;
    }

    public static void TravelPathScreenVisibleResumed() {
        travelPathVisible = true;
    }

    public static void TravelPathScreenVisiblePaused() {
        travelPathVisible = false;
    }


    //light fragment
    public static boolean isLive_trackingVisible() {
        return lightFragmentVisible;
    }

    public static void live_trackingResumed() {
        lightFragmentVisible = true;
    }

    public static void live_trackingPaused() {
        lightFragmentVisible = false;
    }


    //geo fencing
    public static boolean isGeoFencingVisisble() {
        return geoFencingVisisble;
    }

    public static void geofencingResumed() {
        geoFencingVisisble = true;
    }

    public static void geofencingPaused() {
        geoFencingVisisble = false;
    }


}

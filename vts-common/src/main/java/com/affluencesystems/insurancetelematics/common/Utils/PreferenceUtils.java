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
 * File Name:  PreferenceUtils.java
 *
 * Description: this class used for to store the values using shared preference.
 *
 * Routines in this file:
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      Shiva        1.0         31/10/2018
 * Description: this class used for to store the values using shared preference.


 ****************************************************************************/

package com.affluencesystems.insurancetelematics.common.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class PreferenceUtils {

    //user data
    public static final String FISTRNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String USER_NAME = "userName";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String STREAT_TOWN_NAME = "address";
    public static final String GENDER = "gender";
    public static final String EMAIL = "email";
    public static final String VEHICLE_ID = "vehilceId";
    public static final String BRAND = "brand";
    public static final String COMPANY = "manufacturedCompany";
    public static final String VEHICLE_MODEL = "vehicleModel";
    public static final String VEHICLE_MODEL_NUMBER = "vehicleModelNumber";
    public static final String VEHICLE_MODEL_ID = "vehicleModelId";
    public static final String COUNTRY = "country";
    public static final String STATE = "state";
    public static final String PIN = "pin";
    public static final String CITY = "city";
    public static final String IMEI_NUMBER = "imei_number";
    public static final String IMEI_NUMBER_TEST_DRIVE = "IMEI_NUMBER_TEST_DRIVE";
    public static final String VEHICLE_NUMBER = "VEHICLE_NUMBER";
    public static final String SECURED_PASSWORD = "securedPassword";
    public static final String PASSWORD = "password";
    public static final String USER_SIGNUP_TIME = "UserSignUpTime";
    public static final String DEVICE_ID = "deviceId";
    public static final String DEVICE_SERIAL_NUMBER = "DEVICE_SERIAL_NUMBER";
    public static final String FUEL_TYPE = "fuelType";
    public static final String VEHICLE_COMPANY = "VEHICLE_COMPANY";
    public static final String VEHICLE_BRAND = "VEHICLE_BRAND";
    public static final String VEHICLE_VARIANT = "VEHICLE_VARIANT";
    public static final String PRICE = "price";
    public static final String VENDOR_ID = "VENDOR_ID";
    public static final String PERSON_ID = "PERSON_ID";
    public static final String CAR_PIC = "car_image";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String ODO_METER_READING = "ODO_METER_READING";
    public static final String LAST_CAR_SERVICE_DATE = "LAST_CAR_SERVICE_DATE";
    public static final String CAR_PURCHASE_DATE = "CAR_PURCHASE_DATE";
    public static final String CAR_MANUFACTURE_DATE= "CAR_MANUFACTURE_DATE";
    public static final String USER_REGI_DATE = "USER_REGI_DATE";
    public static final String FIREBASE_TOKEN_ID = "FIREBASE_TOKEN_ID";
    public static final String RESPONSE_TRIP= "RESPONSE_TRIP";
    public static final String DEVICE_ACT_DATE = "DEVICE_ACT_DATE";
    public static final String DEVICE_RENEWAL_DATE = "DEVICE_RENEWAL_DATE";
    public static final String DEVICE_STATUS = "DEVICE_STATUS";
    public static final String VEHICLE_COLOR = "VEHICLE_COLOR";
    public static final String VEHICLE_TYPE = "VEHICLE_TYPE";
    public static final String INSURANCE_NUMBER = "INSURANCE_NUMBER";
    public static final String INSURANCE_EX_DATE = "INSURANCE_EX_DATE";
    public static final String INSURANCE_COMPANY = "INSURANCE_COMPANY";
    public static final String INSURANCE_IMAGE = "INSURANCE_IMAGE";
    public static final String INSURANCE_PREMIUM = "INSURANCE_PREMIUM";
    public static final String TRIP_NAME = "TRIP_NAME";
    public static final String Enable_Pattern_Visibity = "Enable_Pattern_Visibity";
    public static final String Enable_FingerPrint_Visibity = "Enable_FingerPrint_Visibity";

    public static final String VEHICLE_CODE = "VEHICLE_CODE";
    public static final String ZOOM_LEVEL = "ZOOM_LEVEL";

    public static final String LIVE_TRACKING = "LIVE_TRACKING";
    public static final String TRIP_LIST = "TRIP_LIST";
    public static final String STRIDE_LIST = "STRIDE_LIST";
    public static final String VISIBLE = "VISIBLE";
    public static final String GONE = "GONE";
    public static final String GEO_FENCE = "GEO_FENCE";
    public static final String SERVICES = "SERVICES";
    public static final String DOCUMENTS = "DOCUMENTS";
    public static final String NOTIFICATIONS = "NOTIFICATIONS";
    public static final String NAV_MY_CAR = "NAV_MY_CAR";
    public static final String ADD_VEHICLE = "ADD_VEHICLE";
    public static final String UPDATE_VEHICLE = "UPDATE_VEHICLE";
    public static final String CHANGE_VEHICLE = "CHANGE_VEHICLE";
    public static final String FEEDBACK = "FEEDBACK";
    public static final String OTP = "OTP";
    public static final String PERSON_PRIVILEGES = "PERSON_PRIVILEGES";

    public static final String Enable_FingerPrint = "Enable_FingerPrint";
    public static final String Enable_Pattern = "Enable_Pattern";

    public static final String STANDARD = "STANDARD";
    public static final String DARK  = "DARK";
    public static final String NIGHT = "NIGHT";
    public static final String RETRO = "RETRO";
    public static final String SILVER = "SILVER";
    public static final String TERRAIN = "TERRAIN";
    public static final String SATELLITE = "SATELLITE";
    public static final String HYBRID = "HYBRID";

    public static final String IS_Map_Styles_Enable = "IS_Map_Styles_Enable";
    public static final String Is_2D_3D_Enable = "Is_2D_3D_Enable";
    public static final String ZOOM_CONTROLS = "ZOOM_CONTROLS";
    public static final String NAVIGATION_CONTROL = "NAVIGATION_CONTROL";

    public static final String RC_DOCUMENT = "RC_DOCUMENT";
    public static final String POLLUTION_DOCUMENT = "POLLUTION_DOCUMENT";
    public static final String LICENCE_DOCUMENT = "LICENCE_DOCUMENT";
    public static final String INSURANCE_DOCUMENT = "INSURANCE_DOCUMENT";


    public static final String PREFERENCES_NAME = "vts";
    public static final String API_KEY = "apikey";
    public static final String ONBOARD_STATUS = "onboard";
    public static final String USER_TYPE = "user_type";
    public static final String MAX_MILLAGE = "max_millage";
    public static final String LOGIN_STATUS = "login_status";

    public static final String IS_START = "is_start";
    public static final String TRIP_START_DATE = "TRIP_START_DATE";


    public static final String UPDATED_LATITUDE = "updated_lattitude";
    public static final String UPDATED_LONGITUDE = "updated_longitude";


    /*
    *           For Driver App values
    * */
    public static final String VEHICLESTATUS = "VEHICLESTATUS";
    public static final String DRIVERSER_NO = "DRIVERSER_NO";
    public static final String VehicleNumber = "VehicleNumber";
    public static final String DRIVER_IMAGE = "DRIVER_IMAGE";
    public static final String IS_VEHICLE_ASSIGNED = "IS_VEHICLE_ASSIGNED";
    public static final String OrganizationId = "OrganizationId";
    public static final String VEHICLEALLOCATEDATETIME = "VEHICLEALLOCATEDATETIME";
    public static final String VEHICLEDEALLOCATEDATETIME = "VEHICLEDEALLOCATEDATETIME";
    public static final String VEHICLEDALLOCATEDBYEMAIL = "VEHICLEDALLOCATEDBYEMAIL";
    public static final String VEHICLEDELOCATEDBYEMAIL = "VEHICLEDELOCATEDBYEMAIL";
    public static final String VEHICLEALLOCATEDBYEMOBILE = "VEHICLEALLOCATEDBYEMOBILE";
    public static final String VEHICLEDELOCATEDBYMOBILE = "VEHICLEDELOCATEDBYMOBILE";
    public static final String PARK_MOVE_VEHICLE = "PARK_MOVE_VEHICLE";
    public static final String DOCUMENT_WALLET = "DOCUMENT_WALLET";
    public static final String REPORT_PROBLEM = "REPORT_PROBLEM";
    public static final String Settings = "Settings";
    public static final String TOken_ID = "TOken_ID";
    public static final String TYPEOFPROOF = "TYPEOFPROOF";
    public static final String IDPROOFNUMBER = "IDPROOFNUMBER";
    public static final String DRIVING_LICENECE_NUMBER = "DRIVING_LICENECE_NUMBER";
    public static final String LICENCEEXPIREDATE = "LICENCEEXPIREDATE";
    public static final String DRIVERADDBY = "DRIVERADDBY";
    public static final String DRIVERVERIFYSTATUS = "DRIVERVERIFYSTATUS";
    public static final String DRIVINGWORKINGSTATUS = "DRIVINGWORKINGSTATUS";
    public static final String DRIVERVERIFIED_BY = "DRIVERVERIFIED_BY";
    public static final String IDPROOFIMAGE = "IDPROOFIMAGE";
    public static final String DRIVING_LICENECE_IMAGE = "DRIVING_LICENECE_IMAGE";

    public static final String IS_TRIP_LIST ="IS_TRIP_LIST";
    public static final String IS_STRIDE_LIST ="IS_STRIDE_LIST";


    public static SharedPreferences.Editor edit;
    private static SharedPreferences preferences;


    public PreferenceUtils(Context context) {

        preferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
        //  preferences = PreferenceManager.getDefaultSharedPreferences(context);
        edit = preferences.edit();
    }

    public void saveString(String strKey, String strValue) {
        edit.putString(strKey, strValue);
        edit.apply();
        edit.commit();
    }

    public void saveInt(String strKey, int value) {
        edit.putInt(strKey, value);
        edit.apply();
        edit.commit();
    }

    public void saveLong(String strKey, Long value) {
        edit.putLong(strKey, value);
        edit.apply();
        edit.commit();
    }

    public void saveFloat(String strKey, float value) {
        edit.putFloat(strKey, value);
        edit.apply();
        edit.commit();
    }

    public void saveDouble(String strKey, String value) {
        edit.putString(strKey, value);
        edit.apply();
        edit.commit();
    }

    public void saveBoolean(String strKey, boolean value) {
        edit.putBoolean(strKey, value);
        edit.apply();
        edit.commit();
    }

    public void removeFromPreference(String strKey) {
        edit.remove(strKey);
    }

    public String getStringFromPreference(String strKey, String defaultValue) {
        return preferences.getString(strKey, defaultValue);
    }

    public boolean getbooleanFromPreference(String strKey) {
        return preferences.getBoolean(strKey, false);
    }

    public boolean getBooleanFromPreferenceForSideBar(String strKey) {
        return preferences.getBoolean(strKey, true);
    }

    public int getIntFromPreference(String strKey, int defaultValue) {
        return preferences.getInt(strKey, defaultValue);
    }

    public long getLongFromPreference(String strKey) {
        return preferences.getLong(strKey, 0);
    }

    public float getFloatFromPreference(String strKey, float defaultValue) {
        return preferences.getFloat(strKey, defaultValue);
    }

    public double getDoubleFromPreference(String strKey, double defaultValue) {
        return Double.parseDouble(preferences.getString(strKey, "" + defaultValue));
    }


    public void logOut() {
        edit.clear();
        edit.commit();

    }

}

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
 * File Name:  DZ_URL.java
 *
 * Description: This file is important in this all service URL's are delclared
 *
 * Routines in this file:
 *             Sign_up()
 *             SignIn()
 *             getBrand() etc.
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      anil        1.0         31/10/2018
 *    Description:    This file is important in this all service URL's are delclared

 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.ApiUtils;

public interface DZ_URL {

/*//    String BASE_URL= "http://192.168.0.127:1245/";                         // for live service data
//    String BASE_URL= "http://1.22.125.203:9090/";                         // for live service data
//    String BASE_URL= "http://113.193.180.242:9090/";                         // for live service data
//    String BASE_URL= "http://192.168.0.164:9090/";                         // for live service data
//    String BASE_URL= "http://intellitrack.in:9191/";                         // for live service data
    String BASE_URL= "http://intellitrack.in:3344/";                         // for live service data
//    String BASE_URL_LIVE= "http://intellitrack.in:9191/";                         // for live service data
    String BASE_URL_LIVE= "http://intellitrack.in:3344/";                         // for live service data
//    String BASE_URL= "http://1.22.125.203:1234/";                         // for live service data
//    String BASE_URL = "http://192.168.0.164:9090/";                         // for device id existence
    String CONFIG_BASE = "http://192.168.0.121:1234/";

    String LIVE_SERVICE = "deviceCapturing/getLiveData/{IMEI}";

//    String CHECK_IS_IN_GEOFENCING = "geofencedata/{organizationId}/{imeinumber}";
    String CHECK_IS_IN_GEOFENCING = "individualgeofence/getgeofencebasedonpersonid/{personId}";

//    String SET_GEOFENCING = "setgeofencing";
    String SET_GEOFENCING = "geofence/setgeofencing";

    String PERSON_GEO_MAP= "individualgeofence/mappersonandfencedata";

    String DELETE_GEO_FENCE = "geofence/removegeofencedatasingle/{fenceId}";

    String UPDATE_FENCING = "geofence/updategeofence";

    String GET_CONFIG = "deviceCapturing/getDeviceconfig/{imeiNumber}";

    String UPDATE_CONFIG = "deviceCapturing/getSetParameters";

//    String SAVE_FEEDBACK_SERVICE = "userfeedback";
    String SAVE_FEEDBACK_SERVICE = "feedback/userfeedback";

    String SAVE_TRIP = "vehicletrips/settripdata";

    String UPDATE_TRIP = "vehicletrips/updatetrip";

//    String GET_NOTIFICATION_SERVICE = "getalertsdatabyimei/{imeiNumber}";
    String GET_NOTIFICATION_SERVICE = "vehiclealerts/getalertsdatabyimei/{imeiNumber}";

    String GET_NOTIFICATION_TYPE = "typeofalerts/getallalertstype";

    String GET_STRIDES = "stridedata/getstridelistbyimeinumber/{imeiNumber}/{startdate}/{enddate}";

    String GET_TRIPS = "vehicletrips/gettriplistbyimeinumber/{imeiNumber}/{startdate}/{enddate}";

    String TEST_LIVE_SERVICE = "deviceCapturing/getNormalData/{IMEI}";


    *//*
    *       Driver API's
    * *//*

    String GET_NOTIFICATION_SERVICE_DRIVER = "getalertsbydriverslno/{driverslno}";

    String GET_STRIDES_DRIVER = "getstridelistbydriverslno/{driverslno}/{startdate}/{enddate}";

    String GET_TRIPS_DRIVER = "gettriplistbydriverslno/{driverslno}/{startdate}/{enddate}";

    //String CHECK_IS_IN_GEOFENCING_DRIVER = "geofencedata/{organizationId}/{imeinumber}";

    String CHECK_IS_IN_GEOFENCING_DRIVER = "geofence/getfencedatabasedonimei/{imeinumber}";
    String GET_CACHE_CONFIG = "deviceCapturing/getSetParametersByImei/{imeiNumber}";*/



    //    String BASE_URL= "http://192.168.0.127:1245/";                         // for live service data
//    String BASE_URL= "http://1.22.125.203:9090/";                         // for live service data
//    String BASE_URL= "http://113.193.180.242:9090/";                         // for live service data
//    String BASE_URL= "http://192.168.0.164:9090/";                         // for live service data
//    String BASE_URL= "http://intellitrack.in:9191/";                         // for live service data
    String BASE_URL= "http://intellitrack.in:3344/";                         // for live service data
    //    String BASE_URL_LIVE= "http://intellitrack.in:9191/";                         // for live service data
    String BASE_URL_LIVE= "http://intellitrack.in:3344/";                         // for live service data
    //    String BASE_URL= "http://1.22.125.203:1234/";                         // for live service data
//    String BASE_URL = "http://192.168.0.164:9090/";                         // for device id existence
//    String CONFIG_BASE = "http://192.168.0.121:1234/";
    String CONFIG_BASE = "http://intellitrack.in:1234/";
    String CONFIG_BASE1 = "http://192.168.0.121:1245/";

    String LIVE_SERVICE = "deviceCapturing/getLiveData/{IMEI}";
//    String LIVE_SERVICE = "deviceCapturing/getLiveTrackingDetailsForSingle/{IMEI}";

    //    String CHECK_IS_IN_GEOFENCING = "geofencedata/{organizationId}/{imeinumber}";
    String CHECK_IS_IN_GEOFENCING = "individualgeofence/getgeofencebasedonpersonid/{personId}";

    String STRIDE_DATA = "stridedata/getstridedata/demo";

    String DOWNLOAD_FILE = "stridedata/getstridedocument";

    //    String SET_GEOFENCING = "setgeofencing";
    String SET_GEOFENCING = "geofence/setgeofencing";

    String PERSON_GEO_MAP= "individualgeofence/mappersonandfencedata";

    String DELETE_GEO_FENCE = "geofence/removegeofencedatasingle/{fenceId}";

    String UPDATE_FENCING = "geofence/updategeofence";

    String GET_CONFIG = "deviceCapturing/getDeviceconfig/{imeiNumber}";

    String GET_CACHE_CONFIG = "deviceCapturing/getSetParametersByImei/{imeiNumber}";

    String UPDATE_CONFIG = "deviceCapturing/getSetParameters";

    //    String SAVE_FEEDBACK_SERVICE = "userfeedback";
    String SAVE_FEEDBACK_SERVICE = "feedback/userfeedback";

    String SAVE_TRIP = "vehicletrips/settripdata";

    String UPDATE_TRIP = "vehicletrips/updatetrip";

    //    String GET_NOTIFICATION_SERVICE = "getalertsdatabyimei/{imeiNumber}";
    String GET_NOTIFICATION_SERVICE = "vehiclealerts/getalertsdatabyimei/{imeiNumber}";

    String GET_NOTIFICATION_TYPE = "typeofalerts/getallalertstype";

    String GET_STRIDES = "stridedata/getstridelistbyimeinumber/{imeiNumber}/{startdate}/{enddate}";

    String GET_STRIDES_GPS_DATA = "stridedata/getstridedocument/{primaryKey}";

    String DOWNLOAD_STRIDES_GPS_DATA = "stridedata/getstridedocumentsfile/{primarykey}";

    String GET_STRIDES_FOR_TRIP = "vehicletrips/getstridelistintrip/{imeinumber}/{startdate}/{enddate}";

    String GET_TRIPS = "vehicletrips/gettriplistbyimeinumber/{imeiNumber}/{startdate}/{enddate}";

    String TEST_LIVE_SERVICE = "deviceCapturing/getNormalData/{IMEI}";

    String GET_LAT_LNG_DATA_= "stridedata/getstridedata/demo";

    String CHECK_DOWNLOAD = "stridedata/getDocumentsFile";

    String CHECK_BYTE = "bytecheck";


    /*
     *       Driver API's
     * */

    String GET_NOTIFICATION_SERVICE_DRIVER = "getalertsbydriverslno/{driverslno}";

    String GET_STRIDES_DRIVER = "getstridelistbydriverslno/{driverslno}/{startdate}/{enddate}";

    //String GET_TRIPS_DRIVER = "gettriplistbydriverslno/{driverslno}/{startdate}/{enddate}";
     String GET_TRIPS_DRIVER = "vehicletrips/gettriplistofdriverslno/{driverslno}/{startdate}/{enddate}";

    String CHECK_IS_IN_GEOFENCING_DRIVER = "geofencedata/{organizationId}/{imeinumber}";


}

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
 * File Name:  API_class.java
 *
 * Description: This file is important in this all service methods are delclared
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
 *    Description:    This file is important in this all service methods are delclared

 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.ApiUtils;

import com.affluencesystems.insurancetelematics.common.Models.ConfigPacket;
import com.affluencesystems.insurancetelematics.common.Models.DeviceConfig;
import com.affluencesystems.insurancetelematics.common.Models.DownloadFile;
import com.affluencesystems.insurancetelematics.common.Models.Feedback;
import com.affluencesystems.insurancetelematics.common.Models.GeoFenceResponse;
import com.affluencesystems.insurancetelematics.common.Models.GeoResponse;
import com.affluencesystems.insurancetelematics.common.Models.NotificationModel;
import com.affluencesystems.insurancetelematics.common.Models.NotificationType;
import com.affluencesystems.insurancetelematics.common.Models.PersonGeoFenceMap;
import com.affluencesystems.insurancetelematics.common.Models.Stride;
import com.affluencesystems.insurancetelematics.common.Models.StrideGpsData;
import com.affluencesystems.insurancetelematics.common.Models.Trip;
import com.affluencesystems.insurancetelematics.common.Models.TripForList;
import com.affluencesystems.insurancetelematics.common.Models.Try;
import com.google.gson.JsonElement;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API_class {

    @GET(DZ_URL.LIVE_SERVICE)                                                                                   // static data imei number added....
    Call<JsonElement> getVehicleAllDetailsBasedOnDeviceId(@Path("IMEI") String IMEI);

    @GET(DZ_URL.CHECK_IS_IN_GEOFENCING)
    Call<ArrayList<GeoResponse>> checkIsInGeoFencing(/*@Path("organizationId") String organizationId, */@Path("personId") String personId);

    @GET(DZ_URL.STRIDE_DATA)
    Call<JsonElement> getStrideData();

    @GET(DZ_URL.DOWNLOAD_FILE)
    Call<JsonElement> downloadFile();

    @POST(DZ_URL.SET_GEOFENCING)
    Call<GeoResponse> postGeoFencing(@Body GeoResponse geoFenceResponse);

    @POST(DZ_URL.PERSON_GEO_MAP)
    Call<PersonGeoFenceMap> personGeoMap(@Body PersonGeoFenceMap personGeoFenceMap);

    @DELETE(DZ_URL.DELETE_GEO_FENCE)
    Call<JsonElement> deleteGeoFence(@Path("fenceId") String fenceId);

    @PUT(DZ_URL.UPDATE_FENCING)
    Call<GeoResponse> updateGeoFencing(@Body GeoResponse geoResponse);

    @POST(DZ_URL.SAVE_FEEDBACK_SERVICE)
    Call<JsonElement> saveFeedbackDetails(@Body Feedback feedback);

    @POST(DZ_URL.SAVE_TRIP)
    Call<JsonElement> saveTripDetails(@Body Trip trip);

    @PUT(DZ_URL.UPDATE_TRIP)
    Call<JsonElement> updateTrip(@Body Trip trip);

    @GET(DZ_URL.GET_CONFIG)
    Call<DeviceConfig> getConfig(@Path("imeiNumber") String imeiNumber);

    @GET(DZ_URL.GET_CACHE_CONFIG)
    Call<ConfigPacket> getCacheConfig(@Path("imeiNumber") String imeiNumber);

    @POST(DZ_URL.UPDATE_CONFIG)
    Call<DeviceConfig> updateConfig(@Body ConfigPacket configPacket);

    @GET(DZ_URL.GET_NOTIFICATION_SERVICE)
    Call<NotificationModel> getNotificationForPersonal(@Path("imeiNumber") String imeiNumber, @Query("page") int page, @Query("size") int size);

    @GET(DZ_URL.GET_NOTIFICATION_TYPE)
    Call<ArrayList<NotificationType>> getNotificationType();

    @GET(DZ_URL.GET_STRIDES)
    Call<JsonElement> getStrides(@Path("imeiNumber") String imeiNumber, @Path("startdate") String startdate, @Path("enddate") String enddate);

    @GET(DZ_URL.GET_STRIDES)
    Call<ArrayList<Stride>> getStridesNew(@Path("imeiNumber") String imeiNumber, @Path("startdate") String startdate, @Path("enddate") String enddate);

    @GET(DZ_URL.GET_STRIDES_GPS_DATA)
    Call<StrideGpsData> getStrideGpsData(@Path("primaryKey") String primaryKey);

    @GET(DZ_URL.DOWNLOAD_STRIDES_GPS_DATA)
    Call<StrideGpsData> downloadStrideGpsData(@Path("primarykey") String primarykey);

    @GET(DZ_URL.GET_STRIDES_FOR_TRIP)
    Call<ArrayList<Stride>> getStrideForTrip(@Path("imeinumber") String imeinumber, @Path("startdate") String startdate, @Path("enddate") String enddate);

    @GET(DZ_URL.GET_TRIPS)
    Call<ArrayList<TripForList>> getTrips(@Path("imeiNumber") String imeiNumber, @Path("startdate") String startdate, @Path("enddate") String enddate);

    @GET(DZ_URL.TEST_LIVE_SERVICE)                                                 // static data imei number added....
    Call<JsonElement> testLiveService(@Path("IMEI") String IMEI);

    @GET(DZ_URL.GET_LAT_LNG_DATA_)                                                 // static data imei number added....
    Call<JsonElement> getRecords();

    @POST(DZ_URL.CHECK_BYTE)                                                 // static data imei number added....
    Call<JsonElement> getRecords1(@Body Try trya);

    @GET(DZ_URL.CHECK_DOWNLOAD)                                                 // static data imei number added....
    Call<JsonElement> download();


    /*
    *       Driver API's
    * */


    @GET(DZ_URL.GET_NOTIFICATION_SERVICE_DRIVER)
    Call <NotificationModel> getNotificationForDriver(@Path("driverslno") String driverslno);

    @GET(DZ_URL.GET_STRIDES_DRIVER)
    Call<ArrayList<Stride>> getStridesForDriver(@Path("driverslno") String driverslno, @Path("startdate") String startdate, @Path("enddate") String enddate);

    @GET(DZ_URL.GET_TRIPS_DRIVER)
    Call<ArrayList<TripForList>> getTripsForDriver(@Path("driverslno") String driverslno, @Path("startdate") String startdate, @Path("enddate") String enddate);

    @GET(DZ_URL.CHECK_IS_IN_GEOFENCING_DRIVER)
    Call<ArrayList<GeoFenceResponse>> checkIsInGeoFencingForDriver(@Path("organizationId") String organizationId, @Path("imeinumber") String imeinumber);

}

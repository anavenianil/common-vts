package com.affluencesystems.insurancetelematics.common.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.getDistanceInMeters;

public class LiveService2 extends Service {

    private String TAG = "LiveService2";
    private static /*final */ long DELAY = 60000;
    private Handler handler;
    private PreferenceUtils preferenceUtils;
    private Intent intent;
    private int counter = 0;
    private String oldLatitude = "0", oldLongitude = "0";
    private JSONArray gpsData;
    private Handler dataHandler;
    private int position = 0;
    private String speed = "0", rpm = "", battery = "0", travelTime = "0", distanceStride = "0", fuel = "0", /*averageFuelConsumedForStride,*/ /*averageFuelPerLiter,*/
            millage;

    /*
     *       call runnable for every DELAY(3 sec) to get current latlng.
     * */
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (isConnected())
                    getVehicleDetailsService();
                Log.d(TAG, "counter --> " + counter);
                counter++;
            } catch (Exception e) {
                Log.e("LIve service", e.getMessage());
            }
           /* if (i == latlongs.size() - 1) {
                stopRepeatingTask();
            } else {
                handler.postDelayed(mStatusChecker, DELAY);
            }*/
            handler.postDelayed(mStatusChecker, DELAY);
        }
    };

    Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String latitude = "0.0", longitude = "0.0";
                try {
                    if(gpsData.length() < position) {
                        JSONObject object = gpsData.getJSONObject(position);
                        latitude = object.getString("latitude");
                        longitude = object.getString("longitude");
                        position = position + 3;
                    } else {
                        Log.d(TAG,"Array crossed.... for server hit");
                        dataHandler.removeCallbacks(dataRunnable);
                        dataHandler.removeCallbacksAndMessages(dataRunnable);
                        handler.removeCallbacks(mStatusChecker);
                        handler.post(mStatusChecker);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Exception === > " + e.getMessage());
//                    dataHandler.removeCallbacks(dataRunnable);
//                    dataHandler.removeCallbacksAndMessages(dataRunnable);
//                    handler.removeCallbacks(mStatusChecker);
//                    handler.post(mStatusChecker);
                }

                double distance = getDistanceInMeters(Double.parseDouble(oldLatitude), Double.parseDouble(oldLongitude), Double.parseDouble(latitude), Double.parseDouble(longitude));

                intent = new Intent(LiveConstants.LIVE_DATA_KEY);
                if (distance >= 1) {
                    oldLatitude = latitude;
                    oldLongitude = longitude;
                    intent.putExtra(LiveConstants.LIVE_LATITUDE, latitude);
                    intent.putExtra(LiveConstants.LIVE_LONGITUDE, longitude);
                    /*intent.putExtra(LiveConstants.LIVE_SPEED, speed);
                    intent.putExtra(LiveConstants.LIVE_RPM, rpm);
                    intent.putExtra(LiveConstants.LIVE_BATTERY, battery);

                    intent.putExtra(LiveConstants.LIVE_TRAVEL_TIME, travelTime);
                    intent.putExtra(LiveConstants.LIVE_DISTANCE, distance);
                    intent.putExtra(LiveConstants.LIVE_FUEL, fuel);
                    intent.putExtra(LiveConstants.LIVE_MILLAGE, millage);*/
//                    intent.putExtra(LiveConstants.LIVE_AVG_FUEL_CONSUMED, averageFuelConsumedForStride);
//                    intent.putExtra(LiveConstants.LIVE_AVG_FUEL_LITRE, averageFuelPerLiter);
                }

                Log.d(TAG, "Speed in LiveService ==> " + speed);
                Log.d(TAG, "rpm in LiveService ==> " + rpm);
                Log.d(TAG, "battery in LiveService ==> " + battery);
                Log.d(TAG, "travel time in LiveService ==> " + travelTime);

                intent.putExtra(LiveConstants.LIVE_SPEED, speed);
                intent.putExtra(LiveConstants.LIVE_RPM, rpm);
                intent.putExtra(LiveConstants.LIVE_BATTERY, battery);

                intent.putExtra(LiveConstants.LIVE_TRAVEL_TIME, travelTime);
                intent.putExtra(LiveConstants.LIVE_MILLAGE, millage);
                intent.putExtra(LiveConstants.LIVE_DISTANCE, distanceStride);
                intent.putExtra(LiveConstants.LIVE_FUEL, fuel);

                Log.d(TAG, "millage in LiveService ==> " + intent.getStringExtra(LiveConstants.LIVE_MILLAGE));


                if (intent != null) {
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(LiveService2.this);
                    localBroadcastManager.sendBroadcast(intent);
                }

            } catch (Exception e) {
                Log.e("LIve service", e.getMessage());
            }
            dataHandler.postDelayed(dataRunnable, 3000);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
        stopDataHandler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferenceUtils = new PreferenceUtils(this);
        handler = new Handler();
        handler.post(mStatusChecker);
    }


    /*
     *       Service call for get vehicle details like latlngs, speed, rpm.
     * */
    private void getVehicleDetailsService() {
        Call<JsonElement> callRetrofit = null;
        callRetrofit = Constants.service_config.getVehicleAllDetailsBasedOnDeviceId(preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER_TEST_DRIVE, ""));
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.isSuccessful()) {
                    position = 0;
                    // mapUtils.progressDialog(SignupTwo.this).dismiss();
//                    String latitude = "0.0", longitude = "0.0";
                    if (response.body() != null) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response.body().toString());

                            if (!j.getBoolean("liveTracking")) {
                                String latitude = "0.0", longitude = "0.0";
                                if (dataRunnable != null && dataHandler != null)
                                    dataHandler.removeCallbacks(dataRunnable);

                                latitude = j.getString("latitude");
                                longitude = j.getString("longitude");
                                speed = j.getString("averageCanSpeed");
                                rpm = j.getString("averageEngineRpm");
                                battery = j.getString("internalBatteryVoltage");

                                travelTime = j.getString("travelledTime");
                                distanceStride = j.getString("distanceSinceStrideON");
                                fuel = j.getString("fuelConsumed");
                                millage = j.getString("millage");

                                intent = new Intent(LiveConstants.LIVE_DATA_KEY);
                                intent.putExtra(LiveConstants.LIVE_LATITUDE, latitude);
                                intent.putExtra(LiveConstants.LIVE_LONGITUDE, longitude);
                                intent.putExtra(LiveConstants.LIVE_SPEED, speed);
                                intent.putExtra(LiveConstants.LIVE_RPM, rpm);
                                intent.putExtra(LiveConstants.LIVE_BATTERY, battery);

                                intent.putExtra(LiveConstants.LIVE_TRAVEL_TIME, travelTime);
                                intent.putExtra(LiveConstants.LIVE_MILLAGE, millage);
                                intent.putExtra(LiveConstants.LIVE_DISTANCE, distanceStride);
                                intent.putExtra(LiveConstants.LIVE_FUEL, fuel);

                                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(LiveService2.this);
                                localBroadcastManager.sendBroadcast(intent);

                                if (DELAY != 3000) {
                                    DELAY = 3000;
                                    handler.removeCallbacks(mStatusChecker);
                                    handler.postDelayed(mStatusChecker, DELAY);
                                }

                            } else {
                                speed = j.getString("canSpeed");
                                rpm = j.getString("engineRpm");
                                battery = j.getString("internalBatteryVoltage");

                                travelTime = j.getString("travelledTime");
                                distanceStride = j.getString("distanceSinceStrideON");
                                fuel = j.getString("fuelConsumed");
//                                averageFuelConsumedForStride = j.getString("averageFuelConsumedForStride");
//                                averageFuelPerLiter = j.getString("averageFuelPerLiter");
                                millage = j.getString("millage");


                                if (gpsData == null || (gpsData.length() > 0 && compareDataPackets(j.getJSONArray("gpsdata")))) {
                                    gpsData = j.getJSONArray("gpsdata");
                                    Log.d(TAG, "gpsData count ==> " + gpsData.length());
                                    if (dataHandler == null) {
                                        dataHandler = new Handler();
                                    }
                                    dataHandler.post(dataRunnable);
                                    if (DELAY != 60000) {
                                        DELAY = 60000;
                                        handler.removeCallbacks(mStatusChecker);
                                        handler.postDelayed(mStatusChecker, DELAY);
                                    }
                                } else {
                                    if (gpsData != null && gpsData.length() == 0) {
                                        j.getString("previousLatitude");
                                        j.getString("previousLongitude");
                                        getPreviousLatLng(j.getString("previousLatitude"), j.getString("previousLongitude"));
                                        return;
                                    }
                                    getLastLatLng();
                                    stopDataHandler();
                                    DELAY = 2000;
                                    handler.removeCallbacks(mStatusChecker);
                                    handler.postDelayed(mStatusChecker, DELAY);
                                }
                            }

//                            for (int i = 0; i < jsonArray.length(); i++){
//                                JSONObject object = jsonArray.getJSONObject(i);
//                                latitude = object.getString("latitude");
//                                longitude = object.getString("longitude");
//                            }
//
//                            double distance = getDistanceInMeters(Double.parseDouble(oldLatitude), Double.parseDouble(oldLongitude), Double.parseDouble(latitude), Double.parseDouble(longitude));
//
//                            if (distance >= 2) {
//                                intent = new Intent(LiveConstants.LIVE_DATA_KEY);
//                                oldLatitude = latitude;
//                                oldLongitude = longitude;
//                                intent.putExtra(LiveConstants.LIVE_LATITUDE, latitude);
//                                intent.putExtra(LiveConstants.LIVE_LONGITUDE, longitude);
//                                intent.putExtra(LiveConstants.LIVE_SPEED, speed);
//                                intent.putExtra(LiveConstants.LIVE_RPM, rpm);
//                                intent.putExtra(LiveConstants.LIVE_BATTERY, battery);
//                            }

                        } catch (JSONException e) {
                            Log.d(TAG, "JSON Exception -----> ");
                            e.printStackTrace();
                        }

//                        if (intent != null) {
//                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(LiveService2.this);
//                            localBroadcastManager.sendBroadcast(intent);
//                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }


    private void getLastLatLng() {
        try {
            String latitude = gpsData.getJSONObject(gpsData.length() - 1).getString("latitude");
            String longitude = gpsData.getJSONObject(gpsData.length() - 1).getString("longitude");

            intent = new Intent(LiveConstants.LIVE_DATA_KEY);
            intent.putExtra(LiveConstants.LIVE_LATITUDE, latitude);
            intent.putExtra(LiveConstants.LIVE_LONGITUDE, longitude);

            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(LiveService2.this);
            localBroadcastManager.sendBroadcast(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPreviousLatLng(String latitude, String longitude) {
        intent = new Intent(LiveConstants.LIVE_DATA_KEY);
        intent.putExtra(LiveConstants.LIVE_LATITUDE, latitude);
        intent.putExtra(LiveConstants.LIVE_LONGITUDE, longitude);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(LiveService2.this);
        localBroadcastManager.sendBroadcast(intent);
    }


    private boolean compareDataPackets(JSONArray jsonArray) {
        boolean isNewData = false;
        if (gpsData != null && gpsData.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            try {
                Date date = sdf.parse(gpsData.getJSONObject(0).getString("dateTime"));
                Date date1 = sdf.parse(jsonArray.getJSONObject(0).getString("dateTime"));
                if (date1.getTime() != date.getTime()) {
                    long l = date.getTime() - date1.getTime();
                    Log.d(TAG, "packet timings : " + l);
                    isNewData = true;
                }

            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "isNewData : " + isNewData);
        return isNewData;
    }

    /*
     *       After closing the app remove the handler
     * */
    void stopRepeatingTask() {
        if (mStatusChecker != null) {
            handler.removeCallbacks(mStatusChecker);
            handler.removeCallbacksAndMessages(mStatusChecker);
        } else {
            Log.d("handler", "not stoped");
        }

        if (dataRunnable != null && dataHandler != null) {
            dataHandler.removeCallbacks(dataRunnable);
            dataHandler.removeCallbacksAndMessages(dataRunnable);
        } else {
            Log.d("handler", " ==> not stoped");
        }
    }

    private void stopDataHandler() {
        if (dataRunnable != null && dataHandler != null) {
            dataHandler.removeCallbacks(dataRunnable);
            dataHandler.removeCallbacksAndMessages(dataRunnable);
        } else {
            Log.d("handler", " ==> not stoped");
        }
    }

}

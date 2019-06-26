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
 * File Name:  Base_activity.java
 *
 * Description: This file used for to set the values that are come from server to all the screens.
 *
 * Routines in this file:
 *     BroadcastReceiver()
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      Shiva        1.0         31/10/2018
 *    Description:     home screen values are checked .
 *    Modified By:     anil        2.0         22/10/2018
 *    Description:     live data set for speed management,fuel management,vehicle management
 *    Modified By:     Shiva        2.0         23/10/2018
 *    Description:     live data set for travel path,and geo fencing
 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.Activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.affluencesystems.insurancetelematics.common.Utils.CheckActivityStatus;

import com.affluencesystems.insurancetelematics.common.Utils.MyApplication;
import com.affluencesystems.insurancetelematics.common.fragments.GeofencingCustomFragment;
import com.affluencesystems.insurancetelematics.common.fragments.GeofencingRadiusFragment;
import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.NoNetworkDialog;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Base_activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static String TAG = "Base_activity_common";
    public static String addres = " ";
    public static Context context;
    public static PreferenceUtils preferenceUtils;
    public static GeofencingRadiusFragment geofencingRadiusFragment;
    public static RadiusItemListener radiusItemListener;
    public static GeofencingCustomFragment geofencingCustomFragment;
    public static CustomItemListener customItemListener;
    private NoNetworkDialog networkDialog;
    double lattitide=0,longitude=0;
    private LatLng zeroLatLng = new LatLng(0, 0);


    //    Getting values(lat, long, speed, rpm) from live service/broadcast receiver and display the values
    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            preferenceUtils = new PreferenceUtils(Base_activity.this);

           // double lattitide = Double.parseDouble(intent.getStringExtra(LiveConstants.LIVE_LATITUDE));
            //double longitude = Double.parseDouble(intent.getStringExtra(LiveConstants.LIVE_LONGITUDE));
            Log.d(TAG, "Driver App opened and live tracking running");
            if(intent.getStringExtra(LiveConstants.LIVE_LATITUDE)!=null) {
                lattitide = Double.parseDouble(intent.getStringExtra(LiveConstants.LIVE_LATITUDE));
            }

            if(intent.getStringExtra(LiveConstants.LIVE_LONGITUDE)!=null) {
                longitude  = Double.parseDouble(intent.getStringExtra(LiveConstants.LIVE_LONGITUDE));
            }


            /*       To show the live tracking in geo fence screens.
             *       we have two tabs based on visible tab it will show live tracking
             *
             */

            Log.d(TAG,"geo fence visible11 ==> " + CheckActivityStatus.isGeoFencingVisisble());
            Log.d(TAG,"radiusItemListener11 ==> " + radiusItemListener);
            Log.d(TAG,"customItemListener11 ==> " + customItemListener);

            LatLng latLng = new LatLng(lattitide, longitude);
            if (!zeroLatLng.equals(latLng)) {
                if (CheckActivityStatus.isGeoFencingVisisble()) {
                    if (radiusItemListener != null) {
                        radiusItemListener.onRadiusTabClicked(latLng);
                    }
                    if (customItemListener != null) {
                        customItemListener.onCustomTabClicked(latLng);
                    }
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        preferenceUtils = new PreferenceUtils(this);
        networkDialog = new NoNetworkDialog(this);
    }

    public Base_activity() {

    }


    /*
     *       Custom constructor for radius geo fence
     *       Here we send the interface
     * */
    public Base_activity(GeofencingRadiusFragment geofencingRadiusFragment, RadiusItemListener radiusItemListener) {
        this.radiusItemListener = radiusItemListener;
        this.geofencingRadiusFragment = geofencingRadiusFragment;
    }


    /*
     *       Custom constructor for custom geo fence
     *       Here we send the interface
     * */
    public Base_activity(GeofencingCustomFragment geofencingCustomFragment, CustomItemListener customItemListener) {
        this.customItemListener = customItemListener;
        this.geofencingCustomFragment = geofencingCustomFragment;
    }


    /*
     *       To show the messages in snack bar
     * */
    public static void message(Activity context, String message) {
        Snackbar snackbar = Snackbar.make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
         *       For getting live data from live service.
         *       We get data by broadcast receiver.
         * */
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter(LiveConstants.LIVE_DATA_KEY));
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         *       kill the service when app closed.
         * */
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
    }


    /*
     *       returns address.
     *       It takes lat and lng.
     * */
    public static String address(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        if (addresses.size() != 0) {
            addres = addresses.get(0).getAddressLine(0);
        } else {
        }
        return addres;
    }

    public void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        noWifiDialog(isConnected);
    }


    /*
     *       override method for checking network available or not
     * */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        noWifiDialog(isConnected);
    }

    // Showing the  wifi status in Dialog
    public void noWifiDialog(boolean isConnected) {
        networkDialog.setCanceledOnTouchOutside(false);
        networkDialog.setCancelable(false);
        Objects.requireNonNull(networkDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        if (isConnected) {
            if (networkDialog.isShowing() && networkDialog != null) {
                networkDialog.dismiss();

                /*//To refresh the Acitvity
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);*/
            }

        } else {
            if (!networkDialog.isShowing() && networkDialog != null) {
                networkDialog.show();
                networkDialog.startAnimation_wifi_img();
            }

        }


    }

    /*
     *       interface for radius geo fence.
     *       When it called, live tracking will show on radius fragment
     * */
    public interface RadiusItemListener {
        void onRadiusTabClicked(LatLng latLng);
    }

    /*
     *       interface for custom geo fence.
     *       When it called, live tracking will show on custom fragment
     * */
    public interface CustomItemListener {
        void onCustomTabClicked(LatLng latLng);
    }


    /*
     *       When pass the image url it will load the image in full screen and can zoom the image also.
     * */
    public static void fullscrren_img(Activity context, ImageView image_view, String img_link) {
        Intent i = new Intent(context, FullScreen_activity.class);
        i.putExtra(LiveConstants.FULL_IMAGE_KEY, img_link);
        View sharedView = image_view;
        String transitionName = context.getString(R.string.app_name);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(context, sharedView, transitionName);
        try {
            context.startActivity(i, transitionActivityOptions.toBundle());
        } catch (Exception e) {

        }
    }

}




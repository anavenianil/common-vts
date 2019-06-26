package com.affluencesystems.insurancetelematics.common.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Models.GeoResponse;
import com.affluencesystems.insurancetelematics.common.Models.RadiusLatLng;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.fragments.GeofencingCustomFragment;
import com.affluencesystems.insurancetelematics.common.fragments.GeofencingRadiusFragment;
import com.affluencesystems.insurancetelematics.common.Models.GeoFenceResponse;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;


public class GeoFencingViewPagerAdapter extends FragmentPagerAdapter {

    private String TAG = "GeoFencingViewPagerAdapter";
    private GeoFenceResponse geoFenceResponse;
    private GeofencingRadiusFragment geofencingRadiusFragment;
    private GeofencingCustomFragment geofencingCustomFragment;
    private int applicationKey = LiveConstants.PERSONAL_APP;
    HashMap<String, GeoResponse> geoResponseHash;

    public GeoFencingViewPagerAdapter(FragmentManager fm, int applicationKey, HashMap<String, GeoResponse> geoResponseHash) {
        super(fm);
        this.applicationKey = applicationKey;
        this.geoResponseHash = geoResponseHash;
    }

    /*
    *       Based on the position fragment changes.
    * */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(applicationKey == LiveConstants.DRIVER_APP){
            if (position == 0) {
                Bundle args = new Bundle();
                args.putInt(LiveConstants.APPLICATION_KEY, LiveConstants.DRIVER_APP);
                geofencingCustomFragment = new GeofencingCustomFragment();
                geofencingCustomFragment.setArguments(args);
                return geofencingCustomFragment;
            }
        } else {
            if (position == 0) {
                geofencingRadiusFragment = new GeofencingRadiusFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(LiveConstants.GEO_FENCE_HASH, geoResponseHash);
                geofencingRadiusFragment.setArguments(bundle);
                return geofencingRadiusFragment;
            } else if (position == 1) {
                geofencingCustomFragment = new GeofencingCustomFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(LiveConstants.GEO_FENCE_HASH, geoResponseHash);
                geofencingCustomFragment.setArguments(bundle);
                return geofencingCustomFragment;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        if(applicationKey == LiveConstants.DRIVER_APP){
            return 1;
        } else {
            return 2;
        }
    }


    /*
     *       Based on the position fragment title changes.
     * */
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if(applicationKey == LiveConstants.DRIVER_APP){

        } else {
            if (position == 0) {
                title = "Radius";
            } else if (position == 1) {
                title = "Custom";
            }
        }
        return title;
    }
}

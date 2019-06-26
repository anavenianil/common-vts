package com.affluencesystems.insurancetelematics.common.Utils;

import com.affluencesystems.insurancetelematics.common.Models.GeoResponse;
import com.affluencesystems.insurancetelematics.common.Models.RadiusLatLng;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/*
*       interface for communicating two geo fence fragments.
* */
public interface GeoFencingCommunicator {
//    void updatedGeoFencing(GeoResponse geoFenceResponse);
//    void fenceDeleteClicked();
    void modifyFence(String fenceName, String updateKey);
    void moveNextFragment(int fragmentNumber);
    void openAlert(String fenceName, ArrayList<RadiusLatLng> radiusLatLng, ArrayList<LatLng> customLatlng, boolean isForUpdate);
    void postGeoFence(boolean isFoUpdate);
    void getGeoFence(String fenceName, ArrayList<RadiusLatLng> radiusLatLng, ArrayList<LatLng> customLatlng, boolean isForUpdate);
}

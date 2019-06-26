package com.affluencesystems.insurancetelematics.common.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class GeoResponse implements Parcelable {
    private String fenceName, fenceId, latLongUpdateDate;
    private ArrayList<RadiusLatLng> radiusLatAndLong;
    private ArrayList<LatLng> customLatAndLong;

    public GeoResponse(String fenceName, String fenceId, String latLongUpdateDate, ArrayList<RadiusLatLng> radiusLatAndLong, ArrayList<LatLng> customLatAndLong) {
        this.fenceName = fenceName;
        this.fenceId = fenceId;
        this.latLongUpdateDate = latLongUpdateDate;
        this.radiusLatAndLong = radiusLatAndLong;
        this.customLatAndLong = customLatAndLong;
    }

    public GeoResponse(String fenceName, ArrayList<RadiusLatLng> radiusLatAndLong, ArrayList<LatLng> customLatAndLong) {
        this.fenceName = fenceName;
        this.radiusLatAndLong = radiusLatAndLong;
        this.customLatAndLong = customLatAndLong;
    }

    protected GeoResponse(Parcel in) {
        fenceName = in.readString();
        fenceId = in.readString();
        latLongUpdateDate = in.readString();
        customLatAndLong = in.createTypedArrayList(LatLng.CREATOR);
    }

    public static final Creator<GeoResponse> CREATOR = new Creator<GeoResponse>() {
        @Override
        public GeoResponse createFromParcel(Parcel in) {
            return new GeoResponse(in);
        }

        @Override
        public GeoResponse[] newArray(int size) {
            return new GeoResponse[size];
        }
    };

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public String getFenceId() {
        return fenceId;
    }

    public void setFenceId(String fenceId) {
        this.fenceId = fenceId;
    }

    public String getLatLongUpdateDate() {
        return latLongUpdateDate;
    }

    public void setLatLongUpdateDate(String latLongUpdateDate) {
        this.latLongUpdateDate = latLongUpdateDate;
    }

    public ArrayList<RadiusLatLng> getRadiusLatAndLong() {
        return radiusLatAndLong;
    }

    public void setRadiusLatAndLong(ArrayList<RadiusLatLng> radiusLatAndLong) {
        this.radiusLatAndLong = radiusLatAndLong;
    }

    public ArrayList<LatLng> getCustomLatAndLong() {
        return customLatAndLong;
    }

    public void setCustomLatAndLong(ArrayList<LatLng> customLatAndLong) {
        this.customLatAndLong = customLatAndLong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fenceName);
        parcel.writeString(fenceId);
        parcel.writeString(latLongUpdateDate);
        parcel.writeTypedList(customLatAndLong);
    }
}

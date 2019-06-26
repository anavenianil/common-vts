package com.affluencesystems.insurancetelematics.common.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class RadiusLatLng implements Parcelable {
    private double radiusDistance;
    private double latitude,longitude;

    public RadiusLatLng(double radiusDistance, double latitude, double longitude) {
        this.radiusDistance = radiusDistance;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected RadiusLatLng(Parcel in) {
        radiusDistance = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<RadiusLatLng> CREATOR = new Creator<RadiusLatLng>() {
        @Override
        public RadiusLatLng createFromParcel(Parcel in) {
            return new RadiusLatLng(in);
        }

        @Override
        public RadiusLatLng[] newArray(int size) {
            return new RadiusLatLng[size];
        }
    };

    public double getRadiusDistance() {
        return radiusDistance;
    }

    public void setRadiusDistance(double radiusDistance) {
        this.radiusDistance = radiusDistance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(radiusDistance);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}

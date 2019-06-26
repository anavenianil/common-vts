package com.affluencesystems.insurancetelematics.common.Models;

import java.util.ArrayList;

public class AllStrides {

    private String distanceTraveled, timeForStride;
    private ArrayList<Stride> arrayList;

    public AllStrides(ArrayList<Stride> arrayList) {
        this.arrayList = arrayList;
    }

    public AllStrides(String distanceTraveled, String timeForStride, ArrayList<Stride> arrayList) {
        this.distanceTraveled = distanceTraveled;
        this.timeForStride = timeForStride;
        this.arrayList = arrayList;
    }

    public String getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(String distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public String getTimeForStride() {
        return timeForStride;
    }

    public void setTimeForStride(String timeForStride) {
        this.timeForStride = timeForStride;
    }

    public ArrayList<Stride> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Stride> arrayList) {
        this.arrayList = arrayList;
    }

}

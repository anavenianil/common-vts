package com.affluencesystems.insurancetelematics.common.Models;

public class StrideGpsData {

    private String Stride, History;

    public StrideGpsData(String stride, String history) {
        Stride = stride;
        History = history;
    }

    public String getStride() {
        return Stride;
    }

    public void setStride(String stride) {
        Stride = stride;
    }

    public String getHistory() {
        return History;
    }

    public void setHistory(String history) {
        History = history;
    }
}

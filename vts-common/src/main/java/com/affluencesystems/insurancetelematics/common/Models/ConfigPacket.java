package com.affluencesystems.insurancetelematics.common.Models;

import java.util.ArrayList;

public class ConfigPacket {

    private ArrayList<ConfigSinglePacket> getSetParameters;
    private String imeiNumber;

    public ConfigPacket(ArrayList<ConfigSinglePacket> getSetParameters, String imeiNumber) {
        this.getSetParameters = getSetParameters;
        this.imeiNumber = imeiNumber;
    }

    public ArrayList<ConfigSinglePacket> getGetSetParameters() {
        return getSetParameters;
    }

    public void setGetSetParameters(ArrayList<ConfigSinglePacket> getSetParameters) {
        this.getSetParameters = getSetParameters;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }
}

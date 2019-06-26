package com.affluencesystems.insurancetelematics.common.Models;

public class PersonGeoFenceMap {

    private String personId, fenceId;

    public PersonGeoFenceMap(String personId, String fenceId) {
        this.personId = personId;
        this.fenceId = fenceId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFenceId() {
        return fenceId;
    }

    public void setFenceId(String fenceId) {
        this.fenceId = fenceId;
    }
}

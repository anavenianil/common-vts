package com.affluencesystems.insurancetelematics.common.Models;

public class NotificationType {
    private String typeOfAlert, shotCodeOfAlertType;

    public NotificationType(String typeOfAlert, String shotCodeOfAlertType) {
        this.typeOfAlert = typeOfAlert;
        this.shotCodeOfAlertType = shotCodeOfAlertType;
    }

    public String getTypeOfAlert() {
        return typeOfAlert;
    }

    public void setTypeOfAlert(String typeOfAlert) {
        this.typeOfAlert = typeOfAlert;
    }

    public String getShotCodeOfAlertType() {
        return shotCodeOfAlertType;
    }

    public void setShotCodeOfAlertType(String shotCodeOfAlertType) {
        this.shotCodeOfAlertType = shotCodeOfAlertType;
    }
}

package com.affluencesystems.insurancetelematics.common.Models;

public class Feedback {

    private String organizationId, personId, feedBackMessage, feedBackType;
    private float feedBackRating;

    public Feedback(String organizationId, String personId, String feedBackMessage, float feedBackRating, String feedBackType) {
        this.organizationId = organizationId;
        this.personId = personId;
        this.feedBackMessage = feedBackMessage;
        this.feedBackRating = feedBackRating;
        this.feedBackType = feedBackType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFeedBackMessage() {
        return feedBackMessage;
    }

    public void setFeedBackMessage(String feedBackMessage) {
        this.feedBackMessage = feedBackMessage;
    }

    public float getFeedBackRating() {
        return feedBackRating;
    }

    public void setFeedBackRating(float feedBackRating) {
        this.feedBackRating = feedBackRating;
    }

    public String getFeedBackType() {
        return feedBackType;
    }

    public void setFeedBackType(String feedBackType) {
        this.feedBackType = feedBackType;
    }
}

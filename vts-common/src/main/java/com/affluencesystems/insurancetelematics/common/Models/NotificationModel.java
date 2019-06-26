package com.affluencesystems.insurancetelematics.common.Models;

import java.util.ArrayList;

public class NotificationModel {

    private ArrayList<Notifications> content;
    private int totalPages, totalElements;
    private Pageable pageable;

    public NotificationModel(ArrayList<Notifications> content, int totalPages, int totalElements, Pageable pageable) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageable = pageable;
    }

    public ArrayList<Notifications> getContent() {
        return content;
    }

    public void setContent(ArrayList<Notifications> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}

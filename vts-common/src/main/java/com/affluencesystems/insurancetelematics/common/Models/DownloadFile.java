package com.affluencesystems.insurancetelematics.common.Models;

public class DownloadFile {

    private String filePath;

    public DownloadFile(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

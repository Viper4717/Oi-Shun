package com.example.oishun;

public class Recording {

    public Recording(){

    }

    private String recordingName;
    private String recordingURL;
    private String recordingUploader;
    private String recordingImageURL;

    public String getRecordingDuration() {
        return recordingDuration;
    }

    public void setRecordingDuration(String recordingDuration) {
        this.recordingDuration = recordingDuration;
    }

    private String recordingDuration;

    public String getRecordingName() {
        return recordingName;
    }

    public void setRecordingName(String recordingName) {
        this.recordingName = recordingName;
    }

    public String getRecordingURL() {
        return recordingURL;
    }

    public void setRecordingURL(String recordingURL) {
        this.recordingURL = recordingURL;
    }

    public String getRecordingUploader() {
        return recordingUploader;
    }

    public void setRecordingUploader(String recordingUploader) {
        this.recordingUploader = recordingUploader;
    }

    public String getRecordingImageURL() {
        return recordingImageURL;
    }

    public void setRecordingImageURL(String recordingImageURL) {
        this.recordingImageURL = recordingImageURL;
    }


}

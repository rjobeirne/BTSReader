package com.sail.btsreader;

public class MeditationModel {

    Integer aFileNumber;
    String aDuration;
    Long aRawDuration;
    String aBookDir;
    String aTitle;

    public String getaBookDir() { return aBookDir; }

    public Long getaRawDuration() { return aRawDuration; }

    public void setaRawDuration(Long aRawDuration) { this.aRawDuration = aRawDuration; }

    public String getaDuration() { return aDuration; }

    public Integer getaFileNumber() { return aFileNumber; }

    public void setaFileNumber(Integer aFileNumber) {
        this.aFileNumber = aFileNumber;
    }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }
}

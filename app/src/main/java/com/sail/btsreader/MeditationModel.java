package com.sail.btsreader;

public class MeditationModel {

    Integer aFileNumber;
    String aMeditation;
    String aPath, aBookDir;
    String aTitle, aAuthor;

    public String getaPath() { return aPath; }

    public void setaPath(String aPath) { this.aPath = aPath; }

    public String getaBookDir() { return aBookDir; }

    public void setaBookDir(String aBookDir) { this.aBookDir = aBookDir; }

    public Integer getaFileNumber() { return aFileNumber; }

    public void setaFileNumber(Integer aFileNumber) {
        this.aFileNumber = aFileNumber;
    }

    public String getaMeditation() { return aMeditation; }

    public void setaMeditation(String aMeditation) {
        this.aMeditation = aMeditation;
    }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public String getaAuthor() { return aAuthor; }

    public void setaAuthor(String aAuthor) { this.aAuthor = aAuthor; }
}

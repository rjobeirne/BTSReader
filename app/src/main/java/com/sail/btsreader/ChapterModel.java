package com.sail.btsreader;

public class ChapterModel {

    Integer aTrackNumber;
    String aChapter;
    String aDuration;
    Long aRawDuration;
    String aPath;
    String aTitle;
    String aCover;
    Boolean aRead;

    public String getaPath() { return aPath; }

    public void setaPath(String aPath) { this.aPath = aPath; }

    public Integer getaTrackNumber() { return aTrackNumber; }

    public void setaTrackNumber(Integer aTrackNumber) {
        this.aTrackNumber = aTrackNumber;
    }

    public String getaChapter() { return aChapter; }

    public void setaChapter(String aChapter) {
        this.aChapter = aChapter;
    }

    public Long getaRawDuration() { return aRawDuration; }

    public void setaRawDuration(Long aRawDuration) { this.aRawDuration = aRawDuration; }

    public String getaDuration() { return aDuration; }

    public void setaDuration(String aDuration) { this.aDuration = aDuration; }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public String getaCover() { return aCover;}

    public void setaCover(String aCover) { this.aCover = aCover; }

    public Boolean getRead() { return aRead;}

    public void setRead(Boolean aRead) { this.aRead = aRead; }

//    public String getaAuthor() {
//        return aAuthor;
//    }
//
//    public void setaAuthor(String aAuthor) {
//        this.aAuthor = aAuthor;
//    }
}

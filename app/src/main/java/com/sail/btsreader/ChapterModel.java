package com.sail.btsreader;

public class ChapterModel {

    Integer aTrackNumber;
    String aChapter;
    String aDuration;
    Long aRawDuration;
    String aPath, aBookDir;
    String aTitle;
    String aCover;
    Boolean aRead, pRead;
    Integer aPreviousStart, aPreviousLast;

    public String getaPath() { return aPath; }

    public void setaPath(String aPath) { this.aPath = aPath; }

    public String getaBookDir() { return aBookDir; }

    public void setaBookDir(String aBookDir) { this.aBookDir = aBookDir; }

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

    public Boolean getPossiblyRead() { return pRead;}

    public void setPossiblyRead(Boolean pRead) { this.pRead = pRead; }

    public Integer getPreviousStart() { return aPreviousStart;}

    public void setPreviousStart(Integer aPreviousStart) { this.aPreviousStart = aPreviousStart; }

    public Integer getPreviousLast() { return aPreviousLast;}

    public void setPreviousLast(Integer aPreviousLast) { this.aPreviousLast = aPreviousLast; }

//    public String getaAuthor() {
//        return aAuthor;
//    }
//
//    public void setaAuthor(String aAuthor) {
//        this.aAuthor = aAuthor;
//    }
}

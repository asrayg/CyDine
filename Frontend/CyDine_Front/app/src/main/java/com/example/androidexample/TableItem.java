package com.example.androidexample;

public class TableItem {
    private boolean isReviewed;
    private String nameOfPoster;
    private String reportedBy;

    public TableItem(boolean isReviewed, String nameOfPoster, String reportedBy) {
        this.isReviewed = isReviewed;
        this.nameOfPoster = nameOfPoster;
        this.reportedBy = reportedBy;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public String getNameOfPoster() {
        return nameOfPoster;
    }

    public void setNameOfPoster(String nameOfPoster) {
        this.nameOfPoster = nameOfPoster;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }
}

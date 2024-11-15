package com.example.androidexample;

/**
 * Represents an item in a table with review status, poster details, and reporting information.
 */
public class TableItem {
    private boolean isReviewed;
    private String nameOfPoster;
    private String reportedBy;

    /**
     * Constructs a new TableItem with the specified details.
     *
     * @param isReviewed    Indicates whether the item has been reviewed.
     * @param nameOfPoster  The name of the person who posted the item.
     * @param reportedBy    The name of the person who reported the item.
     */
    public TableItem(boolean isReviewed, String nameOfPoster, String reportedBy) {
        this.isReviewed = isReviewed;
        this.nameOfPoster = nameOfPoster;
        this.reportedBy = reportedBy;
    }

    /**
     * Checks if the item has been reviewed.
     *
     * @return True if the item has been reviewed, false otherwise.
     */
    public boolean isReviewed() {
        return isReviewed;
    }

    /**
     * Sets the review status of the item.
     *
     * @param reviewed True to mark the item as reviewed, false otherwise.
     */
    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    /**
     * Gets the name of the person who posted the item.
     *
     * @return The name of the poster.
     */
    public String getNameOfPoster() {
        return nameOfPoster;
    }


    /**
     * Sets the name of the person who posted the item.
     *
     * @param nameOfPoster The name of the poster.
     */
    public void setNameOfPoster(String nameOfPoster) {
        this.nameOfPoster = nameOfPoster;
    }


    /**
     * Gets the name of the person who reported the item.
     *
     * @return The name of the reporter.
     */
    public String getReportedBy() {
        return reportedBy;
    }

    /**
     * Sets the name of the person who reported the item.
     *
     * @param reportedBy The name of the reporter.
     */
    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }
}

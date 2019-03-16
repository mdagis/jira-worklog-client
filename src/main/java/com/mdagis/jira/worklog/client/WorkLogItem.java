package com.mdagis.jira.worklog.client;

import org.joda.time.LocalDateTime;

public class WorkLogItem {

    private final String author;
    private final double timeSpentMinutes;
    private final LocalDateTime startedDate;

    public WorkLogItem(String author, double timeSpentMinutes, LocalDateTime startedDate) {
        this.author = author;
        this.timeSpentMinutes = timeSpentMinutes;
        this.startedDate = startedDate;
    }

    public String getAuthor() {
        return author;
    }

    public double getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

}

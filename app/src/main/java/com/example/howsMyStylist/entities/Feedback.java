package com.example.howsMyStylist.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "FEEDBACK_TABLE")
public class Feedback {
    @PrimaryKey(autoGenerate = true)
    private long feedbackId;
    private String details;

    // for relationship
    @NonNull
    private long userCreatorId;
    private long stylistContainedId;
    private long salonContainedId;

    public Feedback(long feedbackId, String details, long userCreatorId, long stylistContainedId, long salonContainedId) {
        this.feedbackId = feedbackId;
        this.details = details;
        this.userCreatorId = userCreatorId;
        this.stylistContainedId = stylistContainedId;
        this.salonContainedId = salonContainedId;
    }

    public long getFeedbackId() {
        return feedbackId;
    }

    public String getDetails() {
        return details;
    }

    public long getUserCreatorId() {
        return userCreatorId;
    }

    public long getStylistContainedId() {
        return stylistContainedId;
    }

    public long getSalonContainedId() {
        return salonContainedId;
    }
}

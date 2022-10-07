package com.example.howsMyStylist.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "REVIEW_TABLE")
public class Review {
    @PrimaryKey(autoGenerate = true)
    private long reviewId;
    private String details;

    //for relationship
    @NonNull
    private long userCreatorId;
    private long stylistContainedId;
    private long salonContainedId;

    public Review(long reviewId, String details, long userCreatorId, long stylistContainedId, long salonContainedId){
        this.reviewId = reviewId;
        this.details = details;
        this.userCreatorId = userCreatorId;
        this.stylistContainedId = stylistContainedId;
        this.salonContainedId = salonContainedId;
    }

    public long getReviewId() {
        return reviewId;
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

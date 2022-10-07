package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Review;
import com.example.howsMyStylist.entities.Stylist;

import java.util.List;

public class StylistWithReviews {
    @Embedded
    private Stylist stylist;
    @Relation(
            parentColumn = "stylistId",
            entityColumn = "stylistContainedId"
    )
    public List<Review> reviews;

}

package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Review;
import com.example.howsMyStylist.entities.Salon;

import java.util.List;

public class SalonWithReviews {
    @Embedded
    private Salon salon;
    @Relation(
            parentColumn = "salonId",
            entityColumn = "salonContainedId"
    )
    public List<Review> reviews;
}

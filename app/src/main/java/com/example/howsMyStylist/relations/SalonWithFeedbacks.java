package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Feedback;
import com.example.howsMyStylist.entities.Salon;

import java.util.List;

public class SalonWithFeedbacks {
    @Embedded
    private Salon salon;
    @Relation(
            parentColumn = "salonId",
            entityColumn = "salonContainedId"
    )
    public List<Feedback> feedbacks;
}

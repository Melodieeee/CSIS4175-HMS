package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Feedback;
import com.example.howsMyStylist.entities.Stylist;

import java.util.List;

public class StylistWithFeedbacks {
    @Embedded
    private Stylist stylist;
    @Relation(
            parentColumn = "stylistId",
            entityColumn = "stylistContainedId"
    )
    public List<Feedback> feedbacks;
}

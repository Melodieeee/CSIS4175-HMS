package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Feedback;
import com.example.howsMyStylist.entities.Photo;

import java.util.List;

public class FeedbackWithPhotos {
    @Embedded
    private Feedback feedback;
    @Relation(
            parentColumn = "feedbackId",
            entityColumn = "ownerId"
    )
    private List<Photo> photos;
}

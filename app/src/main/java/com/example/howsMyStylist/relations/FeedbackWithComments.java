package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Comment;
import com.example.howsMyStylist.entities.Feedback;

import java.util.List;

public class FeedbackWithComments {
    @Embedded
    private Feedback feedback;
    @Relation(
            parentColumn = "feedbackId",
            entityColumn = "ownerId"
    )
    private List<Comment> comments;
}

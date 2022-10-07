package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Comment;
import com.example.howsMyStylist.entities.Review;

import java.util.List;

public class ReviewWithComments {
    @Embedded
    private Review review;
    @Relation(
            parentColumn = "reviewId",
            entityColumn = "ownerId"
    )
    public List<Comment> comments;
}

package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Photo;
import com.example.howsMyStylist.entities.Review;

import java.util.List;

public class ReviewWithPhotos {
    @Embedded private Review review;
    @Relation(
            parentColumn = "reviewId",
            entityColumn = "ownerId"
    )
    private List<Photo> photos;

}

package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Review;
import com.example.howsMyStylist.entities.User;

import java.util.List;

public class UserWithReviews {
    @Embedded
    private User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userCreatorId"
    )
    public List<Review> reviews;
}

package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Feedback;
import com.example.howsMyStylist.entities.User;

import java.util.List;

public class UserWithFeedbacks {
    @Embedded
    private User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userCreatorId"
    )
    public List<Feedback> feedbacks;
}

package com.example.howsMyStylist.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.howsMyStylist.entities.Comment;
import com.example.howsMyStylist.entities.User;

import java.util.List;

public class UserWithComments {
    @Embedded
    private User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userCreatorId"
    )
    public List<Comment> comments;
}

package com.example.howsMyStylist.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "COMMENT_TABLE")
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private long commentId;
    private String comment;

    //for relationship user->comment
    @NonNull
    private long ownerId; //either reviewId or feedbackId
    private long userCreatorId;

    public Comment(long commentId, String comment, long ownerId, long userCreatorId){
        this.commentId = commentId;
        this.comment = comment;
        this.ownerId = ownerId;
        this.userCreatorId = userCreatorId;
    }

    public long getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public long getUserCreatorId() {
        return userCreatorId;
    }
}

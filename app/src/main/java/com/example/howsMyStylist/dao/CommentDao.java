package com.example.howsMyStylist.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.howsMyStylist.entities.Comment;

@Dao
public interface CommentDao {
    @Insert
    void insert(Comment comment);
    @Query("DELETE FROM COMMENT_TABLE")
    void deleteAll();
}

package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.FeedbackWithComments;

import java.util.List;

@Dao
public interface FeedbackWithCommentsDao {
    @Transaction
    @Query("SELECT * FROM FEEDBACK_TABLE")
    List<FeedbackWithComments> getFeedbackWithComments();
}

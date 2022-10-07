package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.FeedbackWithPhotos;

import java.util.List;

@Dao
public interface FeedbackWithPhotosDao {
    @Transaction
    @Query("SELECT * FROM FEEDBACK_TABLE")
    List<FeedbackWithPhotos> getFeedbackWithPhotos();
}

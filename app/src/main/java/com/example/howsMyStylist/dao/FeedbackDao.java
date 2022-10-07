package com.example.howsMyStylist.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.howsMyStylist.entities.Feedback;

@Dao
public interface FeedbackDao {
    @Insert
    void insert(Feedback feedback);
    @Query("DELETE FROM FEEDBACK_TABLE")
    void deleteAll();

}

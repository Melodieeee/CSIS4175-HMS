package com.example.howsMyStylist.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.howsMyStylist.entities.Review;

@Dao
public interface ReviewDao {
    @Insert
    void insert(Review review);
    @Query("DELETE FROM REVIEW_TABLE")
    void deleteAll();
}

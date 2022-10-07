package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.ReviewWithPhotos;

import java.util.List;

@Dao
public interface ReviewWithPhotosDao {
    @Transaction
    @Query("SELECT * FROM REVIEW_TABLE")
    List<ReviewWithPhotos> getReviewWithPhotos();
}

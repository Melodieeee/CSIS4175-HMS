package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.ReviewWithComments;

import java.util.List;

@Dao
public interface ReviewWithCommentsDao {
    @Transaction
    @Query("SELECT * FROM REVIEW_TABLE")
    List<ReviewWithComments> getReviewWithComments();
}

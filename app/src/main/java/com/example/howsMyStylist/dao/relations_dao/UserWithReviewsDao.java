package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.UserWithReviews;

import java.util.List;

@Dao
public interface UserWithReviewsDao {
    @Transaction
    @Query("SELECT * FROM USER_TABLE")
    List<UserWithReviews> getUserWithReviews();
}

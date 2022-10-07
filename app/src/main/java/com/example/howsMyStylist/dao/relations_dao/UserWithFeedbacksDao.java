package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.UserWithFeedbacks;

import java.util.List;

@Dao
public interface UserWithFeedbacksDao {
    @Transaction
    @Query("SELECT * FROM USER_TABLE")
    List<UserWithFeedbacks> getUserWithFeedbacks();
}

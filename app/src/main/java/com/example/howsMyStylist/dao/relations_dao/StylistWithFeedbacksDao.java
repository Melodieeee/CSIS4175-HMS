package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.StylistWithFeedbacks;

import java.util.List;

@Dao
public interface StylistWithFeedbacksDao {
    @Transaction
    @Query("SELECT * FROM STYLIST_TABLE")
    List<StylistWithFeedbacks> getStylistWithFeedbacks();
}

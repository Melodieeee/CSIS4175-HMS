package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.SalonWithReviews;

import java.util.List;

@Dao
public interface SalonWithReviewsDao {
    @Transaction
    @Query("SELECT * FROM SALON_TABLE")
    List<SalonWithReviews> getSalonWithReviews();
}

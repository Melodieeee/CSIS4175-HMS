package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.SalonWithStylists;

import java.util.List;

@Dao
public interface SalonWithStylistsDao {
    @Transaction
    @Query("SELECT * FROM SALON_TABLE")
    List<SalonWithStylists> getSalonWithStylists();
}

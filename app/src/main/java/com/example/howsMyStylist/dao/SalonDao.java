package com.example.howsMyStylist.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.howsMyStylist.entities.Salon;

@Dao
public interface SalonDao {
    @Insert
    void insert(Salon salon);
    @Query("DELETE FROM SALON_TABLE")
    void deleteAll();
}


package com.example.howsMyStylist.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.howsMyStylist.entities.Stylist;

@Dao
public interface StylistDao {
    @Insert
    void insert(Stylist stylist);

    @Query("DELETE FROM STYLIST_TABLE")
    void deleteAll();

}

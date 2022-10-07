package com.example.howsMyStylist.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.howsMyStylist.entities.Photo;

@Dao
public interface PhotoDao {
    @Insert
    void insert(Photo photo);
    @Query("DELETE FROM PHOTO_TABLE")
    void deleteAll();
}

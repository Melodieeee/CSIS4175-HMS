package com.example.petstagram.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.petstagram.entities.User;


@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("DELETE FROM USER_TABLE")
    void deleteAll();

}

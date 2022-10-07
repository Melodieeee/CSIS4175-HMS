package com.example.howsMyStylist.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.howsMyStylist.entities.User;


@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("DELETE FROM USER_TABLE")
    void deleteAll();

}

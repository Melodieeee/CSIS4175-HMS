package com.example.howsMyStylist.dao.relations_dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.howsMyStylist.relations.UserWithComments;

import java.util.List;

@Dao
public interface UserWithCommentsDao {
    @Transaction
    @Query("SELECT * FROM USER_TABLE")
    List<UserWithComments> getUserWithComments();
}

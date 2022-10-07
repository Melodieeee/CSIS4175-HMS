package com.example.howsMyStylist;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.howsMyStylist.dao.StylistDao;
import com.example.howsMyStylist.dao.UserDao;
import com.example.howsMyStylist.entities.Stylist;
import com.example.howsMyStylist.entities.User;

import java.util.List;

public class Repository {

    // Dao reference
    private UserDao userDao;
    private StylistDao stylistDao;

    // Constructor
    public Repository (Application application){
        HMSRoomDatabase db = HMSRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        stylistDao = db.stylistDao();
    }

    public void insert (User user){
        HMSRoomDatabase.databaseWriteExecutor.execute(
                () -> userDao.insert(user)

        );
    }

    public void insert (Stylist stylist){
        HMSRoomDatabase.databaseWriteExecutor.execute(
                () -> stylistDao.insert(stylist)

        );
    }

}

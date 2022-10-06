package com.example.petstagram;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.petstagram.dao.StylistDao;
import com.example.petstagram.dao.UserDao;
import com.example.petstagram.entities.Stylist;
import com.example.petstagram.entities.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Stylist.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class HMSRoomDatabase extends RoomDatabase {

    // abstract method:
    public abstract UserDao userDao();
    public abstract StylistDao stylistDao();

    // Multi-threading processing:
    private static volatile HMSRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Get Instance
    public static HMSRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (HMSRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HMSRoomDatabase.class, "HMS_DATABASE").build();
                }
            }
        }
        return INSTANCE;
    }




}

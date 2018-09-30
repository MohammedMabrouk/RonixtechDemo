package com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static AppDatabase single_instance = null;


    public static AppDatabase getInstance(Context appContext)
    {
        if (single_instance == null)
            single_instance = Room.databaseBuilder(appContext,
                    AppDatabase.class, "Ronitech_DataBase").build();;

        return single_instance;
    }
}

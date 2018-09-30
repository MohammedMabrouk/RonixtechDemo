package com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE user_email = :userMail AND user_password = :userPassword")
    User getUser(String userMail, String userPassword);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);


}

package com.kingominho.taskmanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table WHERE userEmail = :userEmail AND password = :password")
    LiveData<List<User>> getUser(String userEmail, String password);

    @Query("SELECT * FROM user_table WHERE user_table.userEmail LIKE :userEmail")
    User getUser(String userEmail);
}

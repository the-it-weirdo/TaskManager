package com.kingominho.taskmanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE userId = :userId")
    LiveData<List<Task>> getAllTasks(int userId);

    @Query("SELECT * FROM task_table WHERE userId = :userId AND category = :category AND isCompleted = :isCompleted")
    LiveData<List<Task>> getAllTasks(int userId, String category, int isCompleted);

    @Query("SELECT * FROM task_table WHERE id = :id")
    LiveData<List<Task>> getTask(int id);
}

package com.kingominho.taskmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private int isCompleted;
    private int userId;
    private String category;

    public Task(String title, int isCompleted, int userId, String category) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.userId = userId;
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }
}

package com.kingominho.taskmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> tasks;
    private LiveData<List<Task>> tasksIsCompletedTrue;
    private LiveData<List<Task>> tasksIsCompletedFalse;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
    }

    //TaskViewModel.setUserAndCategory(int userId, String category) must follow
    //immediately after initialisation of TaskViewModel.
    public void setUserAndCategory(int userId, String category) {
        taskRepository.setUserIdAndCategory(userId, category);
        tasks = taskRepository.getAllTasks();
        tasksIsCompletedTrue = taskRepository.getAllisCompletedTrueTasks();
        tasksIsCompletedFalse = taskRepository.getAllisCompletedFalseTasks();
    }

    public void insert(Task task) {
        taskRepository.insert(task);
    }

    public void update(Task task) {
        taskRepository.update(task);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public LiveData<List<Task>> getTasksIsCompletedTrue() {
        //tasksIsCompletedTrue = taskRepository.getAllisCompletedTrueTasks();
        return tasksIsCompletedTrue;
    }

    public LiveData<List<Task>> getTasksIsCompletedFalse() {
        return tasksIsCompletedFalse;
    }
}

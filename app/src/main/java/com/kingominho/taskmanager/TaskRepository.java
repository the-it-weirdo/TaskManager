package com.kingominho.taskmanager;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> allisCompletedTrueTasks;
    private LiveData<List<Task>> allisCompletedFalseTasks;
    private int userId;
    private String category;

    public TaskRepository(Application application) {
        DataBase dataBase = DataBase.getInstance(application);
        taskDao = dataBase.taskDao();
    }


    public void setUserIdAndCategory(int userId, String category) {
        this.userId = userId;
        this.category = category;
        allTasks = taskDao.getAllTasks(this.userId);
        allisCompletedTrueTasks = taskDao.getAllTasks(this.userId, this.category, 1);
        allisCompletedFalseTasks = taskDao.getAllTasks(this.userId, this.category, 0);
    }


    public void insert(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateTaskAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public LiveData<List<Task>> getAllisCompletedTrueTasks() {
        return allisCompletedTrueTasks;
    }

    public LiveData<List<Task>> getAllisCompletedFalseTasks() {
        return allisCompletedFalseTasks;
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }


    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        public InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        public UpdateTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        public DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    /*private static class GetIsCompletedTrueTask extends AsyncTask<Integer, Void, LiveData<List<Task>>> {

        private TaskDao taskDao;

        public GetIsCompletedTrueTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected LiveData<List<Task>> doInBackground(Integer... integers) {
            LiveData<List<Task>> list = taskDao.getAllTasks(integers[0], 1);
            return list;
        }

        @Override
        protected void onPostExecute(LiveData<List<Task>> listLiveData) {
            //super.onPostExecute(listLiveData);
            //setAllisCompletedTrueTasks()
        }
    }

    private static class GetIsCompletedFalseTask extends AsyncTask<Integer, Void, LiveData<List<Task>>> {

        private TaskDao taskDao;

        public GetIsCompletedFalseTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected LiveData<List<Task>> doInBackground(Integer... integers) {
            LiveData<List<Task>> list = taskDao.getAllTasks(integers[0], 0);
            return list;
        }
    }*/
}

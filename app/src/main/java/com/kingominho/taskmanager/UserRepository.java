package com.kingominho.taskmanager;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> user;

    public UserRepository(Application application)
    {
        DataBase dataBase = DataBase.getInstance(application);
        userDao = dataBase.userDao();
    }


    public void insert(User user)
    {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void update(User user)
    {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void delete(User user)
    {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public LiveData<List<User>> getUser(String email, String password)
    {
        user = userDao.getUser(email, password);
        return user;
    }

    public boolean isValidUser(String email, String password)
    {
        User user = userDao.getUser(email);
        if(user == null)
        {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public int getUserId(String email)
    {
        User user = userDao.getUser(email);
        return user.getId();
    }

    public String getUserName(String email) {
        User user = userDao.getUser(email);
        return  user.getUserName();
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        public InsertUserAsyncTask(UserDao userDao) {
            this.userDao= userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        public UpdateUserAsyncTask(UserDao userDao) {
            this.userDao= userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        public DeleteUserAsyncTask(UserDao userDao) {
            this.userDao= userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }


}

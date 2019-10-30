package com.kingominho.taskmanager;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class, User.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    private static DataBase instance;

    public abstract TaskDao taskDao();
    public abstract UserDao userDao();

    public static synchronized DataBase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class, "task_manager_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateUserDbAsyncTask(instance).execute();
            new PopulateTaskDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateTaskDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;

        private PopulateTaskDbAsyncTask(DataBase dataBase)
        {
            this.taskDao = dataBase.taskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insert(new Task("Test 1 Work", 0, 1, "Work"));
            taskDao.insert(new Task("Test 2 Work", 1, 1, "Work"));
            taskDao.insert(new Task("Test 3 Work", 1, 1, "Work"));
            taskDao.insert(new Task("Test 4 Work", 0, 1, "Work"));

            taskDao.insert(new Task("Test 1 Home", 0, 1, "Home"));
            taskDao.insert(new Task("Test 2 Home", 1, 1, "Home"));
            taskDao.insert(new Task("Test 3 Home", 1, 1, "Home"));
            taskDao.insert(new Task("Test 4 Home", 0, 1, "Home"));

            return null;
        }
    }

    private static class PopulateUserDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao userDao;

        private PopulateUserDbAsyncTask(DataBase dataBase)
        {
            this.userDao = dataBase.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("Test",
                    "password1234", "test@test.com"));
            userDao.insert(new User("Test2",
                    "password1234", "test2@test.com"));
            return null;
        }
    }

}

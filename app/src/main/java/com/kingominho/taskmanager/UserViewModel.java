package com.kingominho.taskmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> user;

    public UserViewModel(@NonNull Application application)
    {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void insert(User user)
    {
        userRepository.insert(user);
    }

    public void update(User user)
    {
        userRepository.update(user);
    }

    public void delete(User user)
    {
        userRepository.delete(user);
    }

    public LiveData<List<User>> getUser(String email, String password)
    {
        user = userRepository.getUser(email, password);
        return user;
    }


}

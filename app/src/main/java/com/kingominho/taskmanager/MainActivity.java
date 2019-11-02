package com.kingominho.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCE_NAME_KEY = "PREFERENCE_TASKMGR";
    public static final String SIGNED_UP_KEY = "SIGNED_UP_KEY";
    public static final String LOGGED_IN_KEY = "LOGGED_IN_KEY";
    public static final String USER_NAME_KEY = ViewCategory.USER_NAME_KEY;
    public static final String USER_ID_KEY = ViewCategory.USER_ID_KEY;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFERENCE_NAME_KEY, MODE_PRIVATE);

        Boolean signedUP = sharedPreferences.getBoolean(SIGNED_UP_KEY, false);


        if (signedUP) {
            Boolean loggedIn = sharedPreferences.getBoolean(LOGGED_IN_KEY, false);
            if (loggedIn) {
                String userName = sharedPreferences.getString(USER_NAME_KEY, "");
                int userId = sharedPreferences.getInt(USER_ID_KEY, -1);
                Intent intent = new Intent(getApplicationContext(), SwipeMenuActivity.class);
                intent.putExtra(ViewCategory.USER_ID_KEY, userId);
                intent.putExtra(ViewCategory.USER_NAME_KEY, userName);
                startActivity(intent);
                finish();
            } else {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
            finish();
        }
    }
}

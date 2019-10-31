package com.kingominho.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    Button loginButton, signUpButton;
    EditText uName, uPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        uName = findViewById(R.id.uNameInput);
        uPass = findViewById(R.id.passInput);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();*/
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFunction();
                /*Intent intent = new Intent(getApplicationContext(), SwipeMenuActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
    }

    private void loginFunction() {

        String userEmail = uName.getText().toString().trim();
        String password = uPass.getText().toString().trim();

        boolean isValid = userViewModel.checkValidLogin(userEmail, password);
        if(isValid) {
            String userName = userViewModel.getUserName(userEmail);
            int userId = userViewModel.getUserId(userEmail);


            getSharedPreferences(MainActivity.PREFERENCE_NAME_KEY, MODE_PRIVATE).edit()
                    .putBoolean(MainActivity.LOGGED_IN_KEY, true)
                    .putInt(MainActivity.USER_ID_KEY, userId)
                    .putString(MainActivity.USER_NAME_KEY, userName)
                    .apply();

            Intent intent = new Intent(getApplicationContext(), SwipeMenuActivity.class);
            intent.putExtra(ViewCategory.USER_ID_KEY, userId);
            intent.putExtra(ViewCategory.USER_NAME_KEY, userName);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Invalid login credentials!!", Toast.LENGTH_SHORT).show();
        }
    }
}

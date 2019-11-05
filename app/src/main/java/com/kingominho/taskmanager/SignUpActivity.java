package com.kingominho.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private TextView userName;
    private TextView userEmail;
    private TextView userPassword;
    private TextView userRePassword;
    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.nameInput);
        userEmail = findViewById(R.id.uNameInput);
        userPassword = findViewById(R.id.passInput);
        userRePassword = findViewById(R.id.rePassInput);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    private void signUp() {
        String name = userName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String rePass = userRePassword.getText().toString().trim();

        boolean flag = true;
        if (name.isEmpty()) {
            userName.setError("Please enter your name!");
            userName.requestFocus();
            flag = false;
        }
        if (email.isEmpty()) {
            userEmail.setError("Please enter your Email!");
            userEmail.requestFocus();
            flag = false;
        }
        if (password.isEmpty()) {
            userRePassword.setError("Please create a password!");
            userRePassword.requestFocus();
            flag = false;
        }
        if (rePass.isEmpty()) {
            userRePassword.setError("Please re-enter your password!");
            userRePassword.requestFocus();
            flag = false;
        }

        if (!flag) {
            return;
        }

        if (!password.equals(rePass)) {
            userRePassword.setError("Passwords doesn't match!");
            userRePassword.requestFocus();
            //userRePassword.setError("Passwords doesn't match!");
            return;
        }

        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:" +
                "[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.*!,^&+=])(?=\\S+$).{8,}$";

        String nameRegex = "^[\\p{L} .'-]+$";

        Pattern patternName = Pattern.compile(nameRegex);
        Pattern patternEmail = Pattern.compile(emailRegex);
        Pattern patternPassword = Pattern.compile(passwordRegex);


        Matcher nameMatcher = patternName.matcher(name);
        Matcher matcherEmail = patternEmail.matcher(email);
        Matcher matcherPassword = patternPassword.matcher(password);

        if (!nameMatcher.matches()) {
            userName.setError("Alphabets only!");
            userName.requestFocus();
            flag = false;
        }
        if (!matcherEmail.matches()) {
            userEmail.setError("Please enter a valid Email!");
            userEmail.requestFocus();
            flag = false;
        }
        if (!matcherPassword.matches()) {
            userPassword.setError("Please enter a valid password." + "\n" + "" +
                    "Your password should contain at least 1 number, 1 capital letter, 1 small letter" + "\n" +
                    " and 1 special character (@ # $ % . * ! , ^ & + =). Minimum password length should be 8 characters.");
            userPassword.requestFocus();
            flag = false;
        }

        if (!flag) {
            return;
        } else {
            User user = new User(name, password, email);


            if(userViewModel.isDuplicateUser(email))
            {
                Toast.makeText(getApplicationContext(), "User already exists!\n" +
                        "Duplicate email. Please try logging in.", Toast.LENGTH_SHORT).show();
                return;
            }
            userViewModel.insert(user);
            Toast.makeText(getApplicationContext(), "Sign-up successful!", Toast.LENGTH_SHORT).show();

            getSharedPreferences(MainActivity.PREFERENCE_NAME_KEY, MODE_PRIVATE).edit()
                    .putBoolean(MainActivity.SIGNED_UP_KEY, true)
                    .apply();
        }

        //Login to the created account
        /*int userId = userViewModel.getUserId(email);

        getSharedPreferences(MainActivity.PREFERENCE_NAME_KEY, MODE_PRIVATE).edit()
                .putBoolean(MainActivity.LOGGED_IN_KEY, true)
                .putInt(MainActivity.USER_ID_KEY, userId)
                .putString(MainActivity.USER_NAME_KEY, name)
                .apply();*/

        Intent intent = new Intent();
        intent.putExtra(LoginActivity.USER_EMAIL_KEY, email);
        intent.putExtra(LoginActivity.USER_P_KEY, password);
        //startActivity(intent);*/
        setResult(RESULT_OK, intent);
        finish();
    }
}

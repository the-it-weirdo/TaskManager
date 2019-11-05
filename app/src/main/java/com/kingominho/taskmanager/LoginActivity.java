package com.kingominho.taskmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final int SIGN_UP_REQUEST = 1;

    public static final String USER_EMAIL_KEY = "USER_EMAIL_KEY";
    public static final String USER_P_KEY = "USER_P_KEY";

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
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, SIGN_UP_REQUEST);
                //finish();
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

        if (userEmail.isEmpty()) {
            uName.setError("Please enter you Email!");
            uName.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            uPass.setError("Please enter your Password!");
            uPass.requestFocus();
            return;
        }

        //test1234@test.com
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:" +
                "[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        //Password should contain at least 1 number, 1 capital letter, 1 small letter
        //and 1 special character. Minimum password length should be 8 characters.
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.*!,^&+=])(?=\\S+$).{8,}$";

        Pattern patternEmail = Pattern.compile(emailRegex);
        Pattern patternPassword = Pattern.compile(passwordRegex);

        Matcher matcherEmail = patternEmail.matcher(userEmail);
        Matcher matcherPassword = patternPassword.matcher(password);

        if (!matcherEmail.matches()) {
            //
            uName.setError("Invalid Email! Please enter a valid Email.");
            uName.requestFocus();
            return;
        }
        if (!matcherPassword.matches()) {
            //
            uPass.setError("Not a valid password!");
            uPass.requestFocus();
            return;
        }

        boolean isValid = userViewModel.checkValidLogin(userEmail, password);
        if (isValid) {
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
        } else {
            Toast.makeText(getApplicationContext(), "Invalid login credentials!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_UP_REQUEST && resultCode == RESULT_OK) {
            String email = data.getStringExtra(USER_EMAIL_KEY);
            String pass = data.getStringExtra(USER_P_KEY);

            uName.setText(email);
            uPass.setText(pass);
            loginFunction();
        } else {
            Toast.makeText(getApplicationContext(), "No new account created.", Toast.LENGTH_SHORT).show();
        }
    }
}

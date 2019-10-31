package com.kingominho.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {

    public static final String NEW_TASK_TITLE_KEY = "NEW_TASK_TITLE";

    private EditText editTextNewTaskTitle;
    private TextView textViewCategoryName;
    private TextView textViewUserName;
    private ImageButton imageButtonAddTaskButton;

    String username;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextNewTaskTitle = findViewById(R.id.newTaskTitle);
        textViewCategoryName = findViewById(R.id.categoryName);
        textViewUserName = findViewById(R.id.userName);
        imageButtonAddTaskButton = findViewById(R.id.addTaskButton);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        Intent intent = getIntent();

        if (intent != null) {
            username = intent.getStringExtra(ViewCategory.USER_NAME_KEY);
            category = intent.getStringExtra(ViewCategory.CATEGORY_KEY);
        }
        else {
            username = "Test";
            category = "Home";
        }

        textViewUserName.setText(username);
        textViewCategoryName.setText(category);
        if (category.equals("Work")) {
            imageButtonAddTaskButton.setBackgroundColor(getResources().getColor(R.color.color_work));
        } else if (category.equals("Home")) {
            imageButtonAddTaskButton.setBackgroundColor(getResources().getColor(R.color.color_home));
        }

        editTextNewTaskTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextNewTaskTitle.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageButtonAddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void addTask() {
        String title = editTextNewTaskTitle.getText().toString();

        if (title.trim().isEmpty()) {
            /*TODO:Following logic clashing with OnTextChanged. Check */
            //editTextNewTaskTitle.setTextColor(Color.RED);
            editTextNewTaskTitle.setError("Field cannot be empty!!");
        } else {

            Intent intent = new Intent();

            int id = getIntent().getIntExtra(ViewCategory.USER_ID_KEY, -1);

            if (id != -1) {
                intent.putExtra(ViewCategory.USER_ID_KEY, id);
            }

            intent.putExtra(ViewCategory.USER_NAME_KEY, username);
            intent.putExtra(ViewCategory.CATEGORY_KEY, category);
            intent.putExtra(NEW_TASK_TITLE_KEY, title);

            setResult(RESULT_OK, intent);
            finish();
        }
    }
}

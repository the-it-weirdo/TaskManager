package com.kingominho.taskmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ViewCategory extends AppCompatActivity {

    public static final String CATEGORY_KEY = "CATEGORY_KEY";
    public static final String USER_NAME_KEY = "USER_NAME_KEY";
    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String TASK_KEY = "TASK_KEY";

    private static final int ADD_TASK_REQUEST = 1;

    private TaskViewModel taskViewModel;

    private ImageView categoryIconImageView;
    private TextView textViewCategoryTitle;
    private TextView textViewTaskRemainingCount;
    private FloatingActionButton floatingActionButtonAddTask;

    int userId;
    String category;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        Intent intent = getIntent();

        if (intent != null) {
            userId = intent.getIntExtra(ViewCategory.USER_ID_KEY, 1);
            userName = intent.getStringExtra(ViewCategory.USER_NAME_KEY);
            category = intent.getStringExtra(ViewCategory.CATEGORY_KEY);
        }
        else {
            userId = 1;
            userName = "Test";
            category = "Home";
        }

        RecyclerView mRecyclerViewRemaining = findViewById(R.id.taskRemainingRecycler);
        RecyclerView mRecyclerViewCompleted = findViewById(R.id.taskCompletedRecycler);
        mRecyclerViewRemaining.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewCompleted.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final TaskAdapter mTaskAdapterRemaining = new TaskAdapter();
        final TaskAdapter mTaskAdapterCompleted = new TaskAdapter();

        categoryIconImageView = findViewById(R.id.categoryIcon);
        textViewCategoryTitle = findViewById(R.id.categoryName);
        textViewTaskRemainingCount = findViewById(R.id.taskRemainingCount);

        floatingActionButtonAddTask = findViewById(R.id.addTaskFab);

        /*userId = 1;
        category = "Home";
        userName = "Test";*/

        textViewCategoryTitle.setText(category);
        if(category.equals("Work"))
        {
            categoryIconImageView.setImageDrawable(getDrawable(R.drawable.ic_work_32b1c4_24dp));
        }
        else if(category.equals("Home"))
        {
            categoryIconImageView.setImageDrawable(getDrawable(R.drawable.ic_person_f5a110_24dp));
        }




        mRecyclerViewRemaining.setAdapter(mTaskAdapterRemaining);
        mRecyclerViewCompleted.setAdapter(mTaskAdapterCompleted);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.setUserAndCategory(userId, category);

        taskViewModel.getTasksIsCompletedFalse().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTaskAdapterRemaining.submitList(tasks);
                //int remaining = mTaskAdapterRemaining.getItemCount();
                int remaining = tasks.size();
                String text = remaining + " tasks remaining.";
                textViewTaskRemainingCount.setText(text);
            }
        });


        int remaining = mTaskAdapterRemaining.getItemCount();
        String text = remaining + " tasks remaining.";
        textViewTaskRemainingCount.setText(text);

        taskViewModel.getTasksIsCompletedTrue().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTaskAdapterCompleted.submitList(tasks);
            }
        });


        mTaskAdapterRemaining.setOnTaskInteractionListener(new TaskAdapter.OnTaskInteractionListener() {
            @Override
            public void OnDeleteButtonPressed(Task task) {
                taskViewModel.delete(task);

            }

            @Override
            public void OnCheckBoxChanged(Task task, boolean isChecked) {
                int isCompleted = isChecked?1:0;
                task.setIsCompleted(isCompleted);
                taskViewModel.update(task);
                //Toast.makeText(getApplicationContext(), String.valueOf(mTaskAdapterRemaining.getItemCount()-1), Toast.LENGTH_SHORT).show();
            }
        });

        mTaskAdapterCompleted.setOnTaskInteractionListener(new TaskAdapter.OnTaskInteractionListener() {
            @Override
            public void OnDeleteButtonPressed(Task task) {
                taskViewModel.delete(task);
            }

            @Override
            public void OnCheckBoxChanged(Task task, boolean isChecked) {
                int isCompleted = isChecked?1:0;
                task.setIsCompleted(isCompleted);
                taskViewModel.update(task);
            }
        });

        floatingActionButtonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

    }

    private void addTask()
    {
        Intent intent = new Intent(ViewCategory.this, AddTaskActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        intent.putExtra(USER_NAME_KEY, userName);
        intent.putExtra(CATEGORY_KEY, category);

        startActivityForResult(intent, ADD_TASK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK)
        {
            String title = data.getStringExtra(AddTaskActivity.NEW_TASK_TITLE_KEY);

            userId = data.getIntExtra(USER_ID_KEY, -1);
            category = data.getStringExtra(CATEGORY_KEY);
            userName = data.getStringExtra(USER_NAME_KEY);

            Task task = new Task(title, 0, userId, category);
            taskViewModel.insert(task);

            //Toast.makeText(getApplicationContext(), "Task created.", Toast.LENGTH_SHORT).show();
            Snackbar.make(getCurrentFocus(), "Task created!", Snackbar.LENGTH_SHORT).show();
        }
    }

}

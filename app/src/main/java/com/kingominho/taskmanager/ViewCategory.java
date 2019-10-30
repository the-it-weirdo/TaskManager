package com.kingominho.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ViewCategory extends AppCompatActivity {

    private static final String CATEGORY_KEY = "CATEGORY_KEY";
    private static final String USER_KEY = "USER_KEY";
    private static final String TASK_KEY = "TASK_KEY";

    private static final int ADD_TASK_REQUEST = 1;

    private TaskViewModel taskViewModel;

    private ImageView categoryIconImageView;
    private TextView textViewCategoryTitle;
    private TextView textViewTaskRemainingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        RecyclerView mRecyclerViewRemaining = findViewById(R.id.taskRemainingRecycler);
        RecyclerView mRecyclerViewCompleted = findViewById(R.id.taskCompletedRecycler);
        mRecyclerViewRemaining.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewCompleted.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final TaskAdapter mTaskAdapterRemaining = new TaskAdapter();
        final TaskAdapter mTaskAdapterCompleted = new TaskAdapter();

        categoryIconImageView = findViewById(R.id.categoryIcon);
        textViewCategoryTitle = findViewById(R.id.categoryName);
        textViewTaskRemainingCount = findViewById(R.id.taskRemainingCount);

        int userId = 1;
        String category = "Home";

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
                int remaining = mTaskAdapterRemaining.getItemCount();
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

    }
}

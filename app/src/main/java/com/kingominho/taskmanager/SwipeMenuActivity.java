package com.kingominho.taskmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SwipeMenuActivity extends AppCompatActivity implements SwipeMenuAdapter.AdapterOnCardClickListener {

    ViewPager viewPager;
    SwipeMenuAdapter swipeMenuAdapter;
    List<SwipeMenuItem> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private RelativeLayout relativeLayoutParentLayout;
    private TextView textViewUserName;
    private TextView textViewGreetings;
    private ImageButton imageButtonLogOutButton;

    private int noOfCategories;
    private int userId;
    private String userName;
    private String greetings;
    //private String currentDay;
    private String currentDate;
    private String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu);


        //setting greetings
        currentTime = new SimpleDateFormat("HH:MM", Locale.getDefault()).format(new Date());
        String[] split = currentTime.split(":");
        int hour = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);

        if (hour >= 6 && hour < 12) {
            greetings = "Good morning,";
        } else if (hour >= 12 && hour < 14) {
            greetings = "Good noon,";
        } else if (hour >= 14 && hour <= 23) {
            greetings = "Good evening,";
        } else if (hour >= 0 && hour < 6) {
            greetings = "Hello night crawler,";
        }

        Intent intent = getIntent();

        if (intent != null) {
            userName = intent.getStringExtra(ViewCategory.USER_NAME_KEY);
            userId = intent.getIntExtra(ViewCategory.USER_ID_KEY, -1);
        } else {
            userId = 1;
            userName = "Test";
        }
        if (userId == -1) {
            Toast.makeText(getApplicationContext(), "Invalid user id!!\nResetting user id to 1.", Toast.LENGTH_SHORT).show();
            userId = 1;
        }

        relativeLayoutParentLayout = findViewById(R.id.relativeLayout);
        textViewUserName = findViewById(R.id.userName);
        textViewGreetings = findViewById(R.id.greetings);
        imageButtonLogOutButton = findViewById(R.id.logout_button);

        textViewGreetings.setText(greetings);
        textViewUserName.setText(userName);


        //loading the categories
        String[] categoryNames = getResources().getStringArray(R.array.categoryNames);
        noOfCategories = categoryNames.length;

        //loading the category icons
        TypedArray imgTypedArray = getResources().obtainTypedArray(R.array.categoryIcons);
        int[] iconImageIDs = new int[imgTypedArray.length()];
        for (int i = 0; i < imgTypedArray.length(); i++) {
            iconImageIDs[i] = imgTypedArray.getResourceId(i, -1);
        }
        imgTypedArray.recycle();

        //SwipeMenuItem(int icon, String categoryTitle, String taskRemaining)
        String appendString = " tasks remaining.";
        models = new ArrayList<>();
        for (int i = 0; i < noOfCategories; i++) {
            models.add(new SwipeMenuItem(iconImageIDs[i], categoryNames[i],
                    appendString));
        }

        Log.d("Models created.", models.size() + ".");

        swipeMenuAdapter = new SwipeMenuAdapter(models, this);

        Log.d("Adapter created.", swipeMenuAdapter.getCount() + ".");

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(swipeMenuAdapter);
        viewPager.setPadding(100, 0, 100, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                /*getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),*/
                getResources().getColor(R.color.color5)
        };

        colors = colors_temp;

        final Drawable[] gradients = new Drawable[]{
                getResources().getDrawable(R.drawable.gradient_orange),
                getResources().getDrawable(R.drawable.gradient_blue)
        };


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (swipeMenuAdapter.getCount() - 1) && position < (colors.length - 1)) {
                    relativeLayoutParentLayout.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                    if (positionOffset == 0) {
                        relativeLayoutParentLayout.setBackground(gradients[position]);
                    }
                } else {
                    //relativeLayout.setBackgroundColor(colors[colors.length - 1]);
                    relativeLayoutParentLayout.setBackground(gradients[position]);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageButtonLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

    }

    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_NAME_KEY, MODE_PRIVATE);
        sharedPreferences.edit()
                .remove(MainActivity.USER_ID_KEY)
                .remove(MainActivity.USER_NAME_KEY)
                .putBoolean(MainActivity.LOGGED_IN_KEY, false)
                .apply();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCardClick(String param) {
        Intent intent = new Intent(getApplicationContext(), ViewCategory.class);
        intent.putExtra(ViewCategory.CATEGORY_KEY, param);
        intent.putExtra(ViewCategory.USER_ID_KEY, userId);
        intent.putExtra(ViewCategory.USER_NAME_KEY, userName);
        //mBundle.putInt(TASK_REMAINING_KEY, map.get(param));
        //intent.putExtras(mBundle);

        /*Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(view, "parentCard");*/

        /*if(Build.VERSION.SDK_INT>=21)
        {
            //@TargetApi(LOLLIPOP)
            activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    SwipeMenuActivity.this,
                    view,
                    ViewCompat.getTransitionName(view));
            startActivity(intent, activityOptions.toBundle());
        }
        else
        {
            startActivity(intent);
        }*/

        startActivity(intent);
        //finish();
        //overridePendingTransition(R.anim.go_up, R.anim.go_down);

        //Toast.makeText(getApplicationContext(), param+" clicked!", Toast.LENGTH_SHORT).show();
    }
}

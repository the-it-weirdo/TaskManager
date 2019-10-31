package com.kingominho.taskmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SwipeMenuActivity extends AppCompatActivity implements  SwipeMenuAdapter.AdapterOnCardClickListener{

    ViewPager viewPager;
    SwipeMenuAdapter swipeMenuAdapter;
    List<SwipeMenuItem> models;
    Integer[] colors = null;
    ArgbEvaluator  argbEvaluator = new ArgbEvaluator();

    private RelativeLayout relativeLayoutParentLayout;
    private TextView textViewUserName;
    private TextView textViewGreetings;
    private ImageButton imageButtonLogOutButton;

    private int noOfCategories;
    private int userId;
    private String userName;
    private String greetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu);

        userId = 1;
        userName = "Test";
        greetings = "Hello, ";

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
        for (int i = 0; i < noOfCategories ; i++) {
            models.add(new SwipeMenuItem(iconImageIDs[i], categoryNames[i],
                    appendString));
        }

        Log.d("Models created.", models.size()+".");

        swipeMenuAdapter = new SwipeMenuAdapter(models, this);

        Log.d("Adapter created.", swipeMenuAdapter.getCount()+".");

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
                    if(positionOffset==0)
                    {
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

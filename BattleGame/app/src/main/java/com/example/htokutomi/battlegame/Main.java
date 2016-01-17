package com.example.htokutomi.battlegame;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    //************************************
    // define each components
    //************************************

    // setting page and start page
    Intent settingIntent, startIntent;

    // button animation
    Animation animShake;

    // setting button and start button
    Button btnSetting, btnStart;

    LinearLayout settingLayout, startLayout;

    //************************************
    // Life cycle
    //************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // *************************
        // set up each components
        // *************************

        btnSetting = (Button)findViewById(R.id.settingButton);
        btnStart = (Button)findViewById(R.id.startButton);

        // *************************
        // instantiation
        // *************************

        settingIntent = new Intent(this, Setting.class);
        startIntent = new Intent(this, Start.class);

        // animation
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        btnSetting.startAnimation(animShake);
        btnStart.startAnimation(animShake);

        settingLayout = (LinearLayout)findViewById(R.id.settingLayout);

        // get window size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x; // window width
        int height = size.y; // window height
        int display_mode = getResources().getConfiguration().orientation; // get orientation (horizontal or vertical)

        final ObjectAnimator horizontalAnimator1;
        final ObjectAnimator horizontalAnimator2;

        // horizontal
        // 300 -> button width size
        if (display_mode == 1){
            horizontalAnimator1 = ObjectAnimator.ofInt(new ButtonAnimatorHelper(btnSetting), "marginLeft", 0, width-300); // from left to right
            horizontalAnimator2 = ObjectAnimator.ofInt(new ButtonAnimatorHelper(btnStart), "marginRight", 0, width-300); // from right to left
        }
        // vertical
        // 300 -> button height size
        else{
            horizontalAnimator1 = ObjectAnimator.ofInt(new ButtonAnimatorHelper(btnSetting), "marginTop", 0, height-300); // from top to bottom
            horizontalAnimator2 = ObjectAnimator.ofInt(new ButtonAnimatorHelper(btnStart), "marginBottom", 0, height-300); // from bottom to top
        }

        // setting animation for type of horizontal
        horizontalAnimator1.setDuration(5000);
        horizontalAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        horizontalAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        horizontalAnimator1.setInterpolator(new LinearInterpolator());

        // setting animation for type of vertical
        horizontalAnimator2.setDuration(5000);
        horizontalAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        horizontalAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        horizontalAnimator2.setInterpolator(new LinearInterpolator());

        Log.d("ANIM", "PASS");

        horizontalAnimator1.start();
        horizontalAnimator2.start();
    }


    //************************************
    // functions
    //************************************

    // go to setting page
    public void setting(View v)
    {
        Toast.makeText(this, R.string.setting, Toast.LENGTH_SHORT).show();
        startActivity(settingIntent);
    }

    // go to start page
    public void start(View v)
    {
        Toast.makeText(this, R.string.start, Toast.LENGTH_SHORT).show();
        startActivity(startIntent);
    }


    //************************************
    // class
    //************************************

    private static class ButtonAnimatorHelper {

        final Button mButton;

        public ButtonAnimatorHelper(final Button button) {
            mButton = button;
        }
        // animation from left to right
        public void setMarginLeft(final int margin) {
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mButton.getLayoutParams();
            params.leftMargin = margin;
            mButton.setLayoutParams(params);
        }
        // animation from right to left
        public void setMarginRight(final int margin) {
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mButton.getLayoutParams();
            params.rightMargin = margin;
            mButton.setLayoutParams(params);
        }
        // animation from top to bottom
        public void setMarginTop(final int margin) {
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mButton.getLayoutParams();
            params.topMargin = margin;
            mButton.setLayoutParams(params);
        }
        // animation from bottom to top
        public void setMarginBottom(final int margin) {
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mButton.getLayoutParams();
            params.bottomMargin = margin;
            mButton.setLayoutParams(params);
        }
    }

}


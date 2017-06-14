package com.example.user.chpok;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.util.Random;

public class SplashActivity extends AppCompatActivity implements SpringListener{

    private final String MYLOG = "MYLOG";

    private View viewFlyingMug;
    private SpringSystem springSystem;
    private Spring spring;
    private Display display;
    private DisplayMetrics displayMetric;
    private static double TENSION = 50;
    private static double DAMPER = 5;

    private float startViewPointX;
    private float startViewPointY;

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListenerViewFlyingMug =
            new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            //move view to bottom
            viewFlyingMug.setX(displayMetric.widthPixels / 3);

            spring.setCurrentValue(displayMetric.heightPixels);
            spring.setEndValue(displayMetric.heightPixels / 3);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewFlyingMug.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            else {
                viewFlyingMug.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        display = getWindowManager().getDefaultDisplay();
        displayMetric = new DisplayMetrics();
        display.getMetrics(displayMetric);

        viewFlyingMug = (View) findViewById(R.id.activitySplash_View_FlyingMug);

        //get start points
        viewFlyingMug
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(mOnGlobalLayoutListenerViewFlyingMug);

        //animation object
        springSystem = SpringSystem.create();
        spring = springSystem.createSpring();
        spring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        spring.setSpringConfig(config);


    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        viewFlyingMug.setY(value);
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

}

package com.example.user.chpok;

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
    private static double TENSION = 800;
    private static double DAMPER = 20;

    private float startViewPointX;
    private float startViewPointY;
    private boolean isMoveY;
    private Random rnd;

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListenerViewFlyingMug =
            new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Log.i(MYLOG, " ");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        startFlyingMugAnim();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rnd = new Random();
        display = getWindowManager().getDefaultDisplay();
        displayMetric = new DisplayMetrics();
        display.getMetrics(displayMetric);

        viewFlyingMug = (View) findViewById(R.id.activitySplash_View_FlyingMug);

        //get start points
        viewFlyingMug
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(mOnGlobalLayoutListenerViewFlyingMug);
        startViewPointY = viewFlyingMug.getY();

        //move view to bottom
        viewFlyingMug.setY(displayMetric.heightPixels);

        //animation object
        springSystem = SpringSystem.create();
        spring = springSystem.createSpring();
        spring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        spring.setSpringConfig(config);

    }

    private void startFlyingMugAnim(){
        Log.i(MYLOG, ""+viewFlyingMug.getHeight());
        Log.i(MYLOG, ""+startViewPointY);

        int squareBound = 50;

        //move to start point
        spring.setEndValue(startViewPointY);

        //get random square points
        int rndX = rnd.nextInt(squareBound) - 100;
        int rndY = rnd.nextInt(squareBound) - 100;

        Log.i(MYLOG, "rndX " + rndX + " rndY " + rndY);

        //spring.setEndValue()
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

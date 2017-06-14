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
    private static double TENSION = 200;
    private static double DAMPER = 5;

    private float startViewPointX;
    private float startViewPointY;
    private boolean inOnGlobalLayout = true;
    private Random rnd;

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListenerViewFlyingMug =
            new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            if(inOnGlobalLayout){
                startFlyingMugAnim();
            }

            inOnGlobalLayout = false;

        }
    };

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

        //animation object
        springSystem = SpringSystem.create();
        spring = springSystem.createSpring();
        spring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        spring.setSpringConfig(config);

        //move view to bottom
        viewFlyingMug.setY(displayMetric.heightPixels);
        viewFlyingMug.setX(displayMetric.widthPixels/3);
    }

    private void startFlyingMugAnim(){

        startViewPointY = displayMetric.heightPixels / 3;

        Log.i(MYLOG, ""+startViewPointY);

//        int squareBound = 100;

        //move to start point
//        spring.setEndValue(displayMetric.heightPixels);
        spring.setEndValue(startViewPointY);
//        try {
//            Thread.sleep(1000);
//            //get random square point X
//            spring.setEndValue(getRandomPoint(squareBound) + startViewPointY);
//            Thread.sleep(1000);
//
//            spring.setEndValue(getRandomPoint(squareBound) + startViewPointY);
//            Thread.sleep(1000);
//            spring.setEndValue(getRandomPoint(squareBound) + startViewPointY);
//
//            Thread.sleep(1000);
//        }
//        catch (InterruptedException e){
//
//        }
//        spring.setEndValue(startViewPointY);

    }

//    private int getRandomPoint(int bound){
//        return rnd.nextInt(bound) - bound * 2;
//    }

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

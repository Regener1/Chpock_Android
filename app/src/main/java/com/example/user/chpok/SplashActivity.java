package com.example.user.chpok;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class SplashActivity extends AppCompatActivity implements SpringListener{

    private final String MYLOG = "MYLOG";

    private View mViewFlyingMug;
    private SpringSystem mSpringSystem;
    private Spring mSpring;
    private Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
    private static double TENSION = 50;
    private static double DAMPER = 5;

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListenerViewFlyingMug =
            new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            //move view to bottom
            mViewFlyingMug.setX(mDisplayMetrics.widthPixels / 3);

            mSpring.setCurrentValue(mDisplayMetrics.heightPixels);
            mSpring.setEndValue(mDisplayMetrics.heightPixels / 3);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mViewFlyingMug.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            else {
                mViewFlyingMug.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mDisplay = getWindowManager().getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getMetrics(mDisplayMetrics);

        mViewFlyingMug = (View) findViewById(R.id.activitySplash_View_FlyingMug);

        //get start points
        mViewFlyingMug
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(mOnGlobalLayoutListenerViewFlyingMug);

        //animation object
        mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        mViewFlyingMug.setY(value);
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

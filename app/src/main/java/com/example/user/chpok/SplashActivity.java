package com.example.user.chpok;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity implements SpringListener{

    private final String MYLOG = "MYLOG";
    private  final int SPEED = 1;

    private View mViewFlyingMug;
    private SpringSystem mSpringSystem;
    private Spring mSpring;
    private Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
    private static double TENSION = 15;
    private static double DAMPER = 4;

    private float mBeginMugPointX = 0;

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListenerViewFlyingMug =
            new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            //move view to bottom
            mBeginMugPointX = mViewFlyingMug.getX();

            //move view to bottom
            mViewFlyingMug.setX(mBeginMugPointX);


            mSpring.setCurrentValue(mDisplayMetrics.heightPixels/1.09);
            mSpring.setEndValue(mDisplayMetrics.heightPixels /3);


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

        SpringSystem spSys = SpringSystem.create();
        final Spring  sp = spSys.createSpring();
        sp.setEndValue(1);
        sp.addListener(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                if(sp.getEndValue() == 1)
                    mViewFlyingMug.offsetLeftAndRight(SPEED);
                else
                {
                    mViewFlyingMug.offsetLeftAndRight(-1*SPEED);
                }
            }
        });
        Timer timerAnimatinLR = new Timer();//необходим для зацикливания анимации движения
        TimerTask taskTimer = new TimerTask() {
            @Override
            public void run() {
                double EndV = sp.getEndValue();
                if(EndV == 1) {
                    sp.setEndValue(0);
                }
                if(EndV == 0){
                    sp.setEndValue(1);// 0 or 1
                }
            }
        };
        timerAnimatinLR.schedule(taskTimer,0,900);




        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);

        //delay 3s before open mainActivity
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
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

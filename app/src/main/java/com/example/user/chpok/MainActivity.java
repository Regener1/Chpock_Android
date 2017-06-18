package com.example.user.chpok;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 2;
    private static final String BEER_COUNT_KEY = "BEER_COUNT";

    private ViewPager mSlidePager;
    private PagerAdapter mPagerAdapter;
    private View mViewBeerCount;
    private SharedPreferences mSharedPreferences;
    private int mBeerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlidePager = (ViewPager)findViewById(R.id.activityMain_ViewPager_SlidePager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mSlidePager.setAdapter(mPagerAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.action_bar_title, null);
        //get image view counter
        mViewBeerCount = view.findViewById(R.id.actionBarTitle_TextView_TextViewCount);
        actionBar.setCustomView(view);

        loadCounter();

    }

    //ask
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        saveCounter();
//    }

    public void btnBeerCup_OnClick(View view) {
        mBeerCount++;
        displayBeerCount(mBeerCount);
        saveCounter();


    }

    private void saveCounter(){
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(BEER_COUNT_KEY, mBeerCount);
        editor.commit();

        Log.i("MYLOG", "counter saved");
    }

    private void loadCounter(){
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        mBeerCount = mSharedPreferences.getInt(BEER_COUNT_KEY, 0);
        displayBeerCount(mBeerCount);

        Log.i("MYLOG", "counter loaded");
    }

    private void displayBeerCount(int value){
        ((TextView)mViewBeerCount).setText(Integer.toString(value));
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

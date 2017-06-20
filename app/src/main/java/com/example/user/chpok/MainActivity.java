package com.example.user.chpok;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String MY_LOG= "MY_LOG";

    private static final String BEER_COUNT_KEY = "BEER_COUNT";

    private SwipeFlingAdapterView mSlidePager;
    private PagerAdapter mPagerAdapter;
    private BaseAdapter mModelAdapter;
    private View mViewBeerCount;
    private SharedPreferences mSharedPreferences;
    private int mBeerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mSlidePager = (ViewPager)findViewById(R.id.activityMain_ViewPager_SlidePager);
//        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//        mSlidePager.setAdapter(mPagerAdapter);

        mSlidePager = (SwipeFlingAdapterView) findViewById(R.id.activityMain_SwipeFlingAdapterView_SwipeView);
        mModelAdapter = new MyAdapter();

        /*

        TEST

         */

        mSlidePager.setAdapter(mModelAdapter);
        mSlidePager.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object o) {

            }

            @Override
            public void onRightCardExit(Object o) {

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        /*

        TEST

         */



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

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                Log.v(MY_LOG,"Permission is granted");
                return true;
            } else {
                Log.v(MY_LOG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(MY_LOG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            share();
        }
    }

    public void btnBeerCup_OnClick(View view) {
        useBeerCounter();
        share();

    }

    private void useBeerCounter(){
        mBeerCount++;
        displayBeerCount(mBeerCount);
        saveCounter();
    }

    private void share() {
        if(!isStoragePermissionGranted()){
            return;
        }
        //получение картинки с фрагмента и привязка к bitmap через canvas
        RelativeLayout fragment = (RelativeLayout) findViewById(R.id.fragmentScreenSlidePage);

        Bitmap bitmap = Bitmap.createBitmap(fragment.getWidth(), fragment.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Drawable d = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.background_gray);
        d.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        d.draw(canvas);
        fragment.draw(canvas);

        //моздание интента для шаринга и перенаправление в другие приложения
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        saveImageToInternalStorage(bitmap);
        shareIntent.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/pic.png")));
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, "Выберите способ отправки"));


    }

    private boolean saveImageToInternalStorage(Bitmap image) {
        File sdPath = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "pic.png");
        if(sdPath.exists()) {
            sdPath.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(sdPath);//cоздание файла в потоке и
            image.compress(Bitmap.CompressFormat.PNG, 100, fos); //запись картинки в файл
            //fos.flush();
            fos.close();
            return true;
        }
        catch (FileNotFoundException ex)
        {
            Log.i(MY_LOG,ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        catch (IOException ex)
        {
            Log.i(MY_LOG,ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        catch(Exception ex){
            Log.i(MY_LOG,ex.getMessage());
            ex.printStackTrace();
            return false;
        }
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
//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            ScreenSlidePageFragment newScreenSlidePageFragment = new ScreenSlidePageFragment();
//
//            newScreenSlidePageFragment.setText(ServiceMessage.getInstance().getAllMess().get(position).getText());
//            newScreenSlidePageFragment.setImg(ServiceMessage.getInstance().getAllMess().get(position).getJpgImg());
//
//            return newScreenSlidePageFragment;
//        }
//
//        @Override
//        public int getCount() {
//            return ServiceMessage.getInstance().getAllMess().size();
//        }
//    }

    class MyAdapter extends BaseAdapter {

        private List<MessageModel> items;

        public MyAdapter(List<MessageModel> items){
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View v;
            MessageModel item = (MessageModel) getItem(i);

            if(view == null){
                v = new View();
            }

            v.findViewById(R.id.fragmentScreenSlidePage_TextView);

            return v;
        }
    }
}

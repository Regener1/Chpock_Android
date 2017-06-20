package com.example.user.chpok;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String MY_LOG = "MY_LOG";

    private static final String BEER_COUNT_KEY = "BEER_COUNT";

    private DisplayMetrics mDisplayMetrics;

    private SwipeFlingAdapterView mSlidePager;
    private List<MessageModel> mSliderModelList = new ArrayList<>();
    private BaseAdapter mModelAdapter;
    private View mViewBeerCount;
    private SharedPreferences mSharedPreferences;
    private int mBeerCount = 0;

    private SwipeFlingAdapterView.onFlingListener mOnFlingListener
            = new SwipeFlingAdapterView.onFlingListener() {
        @Override
        public void removeFirstObjectInAdapter() {
            Log.i(MY_LOG, "in removeFirstObjectInAdapter");
            mSliderModelList.remove(0);
            mModelAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLeftCardExit(Object o) {

        }

        @Override
        public void onRightCardExit(Object o) {

        }

        @Override
        public void onAdapterAboutToEmpty(int i) {
            Log.i(MY_LOG, "in onAdapterAboutToEmpty");
            if(mSliderModelList.size() == 2) {
                mSliderModelList.addAll(ServiceMessage.getInstance().getAllMess());
                mModelAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScroll(float scrollProgressPercent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDisplayParams();
        initSlidePager();
        initActionBar();


        loadCounter();
    }

    private void initDisplayParams(){
        Display display = getWindowManager().getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        display.getMetrics(mDisplayMetrics);
    }

    private void initSlidePager(){

        mSlidePager = (SwipeFlingAdapterView) findViewById(R.id.activityMain_SwipeFlingAdapterView_SwipeView);
        mSliderModelList.addAll(ServiceMessage.getInstance().getAllMess());
        mModelAdapter = new MyAdapter(this, mSliderModelList);

        mSlidePager.setAdapter(mModelAdapter);
        mSlidePager.setFlingListener(mOnFlingListener);
    }

    private void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.action_bar_title, null);
        //get image view counter
        mViewBeerCount = view.findViewById(R.id.actionBarTitle_TextView_TextViewCount);
        actionBar.setCustomView(view);
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
        else {
            Log.v(MY_LOG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
        com.lorentzos.flingswipe.SwipeFlingAdapterView fragment =
                (com.lorentzos.flingswipe.SwipeFlingAdapterView) findViewById(R.id.activityMain_SwipeFlingAdapterView_SwipeView);

        Bitmap bitmap = Bitmap.createBitmap(fragment.getWidth(),
                fragment.getHeight() - Math.round(63 * mDisplayMetrics.density),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //draw background
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

    class MyAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        private List<MessageModel> items;

        public MyAdapter(Context context, List<MessageModel> items){
            this.items = items;
            this.context = context;
            inflater = (LayoutInflater) this.context.getSystemService(LAYOUT_INFLATER_SERVICE);
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

            View v = view;
            MessageModel item = (MessageModel) getItem(i);

            if(view == null){
                v = inflater.inflate(R.layout.fragment_screen_slide_page, viewGroup, false);
                v.getLayoutParams().height = mDisplayMetrics.heightPixels - Math.round(76 * mDisplayMetrics.density);

            }

            TextView textView = (TextView)v.findViewById(R.id.fragmentScreenSlidePage_TextView);
            textView.setText(item.getText());
            textView.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/FiveMinutes.ttf"));


            ((ImageView) v.findViewById(R.id.fragmentScreenSlidePage_ImageView_Img))
                    .setImageBitmap(item.getJpgImg());

            return v;
        }
    }
}

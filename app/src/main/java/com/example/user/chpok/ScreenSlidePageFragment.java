package com.example.user.chpok;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Regener on 14.06.2017.
 */

public class ScreenSlidePageFragment extends Fragment {

    private CustomTextView mTextView;
    private ImageView mImageView;

    private String mText;
    private Bitmap mImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        mTextView = (CustomTextView) rootView.findViewById(R.id.fragmentScreenSlidePage_TextView);
        mImageView = (ImageView) rootView.findViewById(R.id.fragmentScreenSlidePage_ImageView_ImgShakespeare);

        mTextView.setText(mText);
        mImageView.setImageBitmap(mImg);

        return rootView;
    }



    public void setText(String text){
        mText = text;
    }

    public void setImg(Bitmap image){
        mImg = image;
    }

    public String getText(){
        return mText;
    }

    public Bitmap getImg(){
        return mImg;
    }
}

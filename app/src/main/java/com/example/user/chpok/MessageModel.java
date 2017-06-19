package com.example.user.chpok;

import android.graphics.Bitmap;

/**
 * Created by iMac on 15.06.17.
 */

public class MessageModel {
    private String mText;
    private String mJpgUrl;
    private Bitmap mJpgImg;

    public MessageModel(){}

    public MessageModel(String text, String pngUrl, Bitmap pngImg) {
        this.mText = text;
        this.mJpgUrl = pngUrl;
        this.mJpgImg = pngImg;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getJpgUrl() {
        return mJpgUrl;
    }

    public void setJpgUrl(String mPngUrl) {
        this.mJpgUrl = mPngUrl;
    }

    public Bitmap getJpgImg() {
        return mJpgImg;
    }

    public void setJpgImg(Bitmap mPngImg) {
        this.mJpgImg = mPngImg;
    }
}

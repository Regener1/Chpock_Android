package com.example.user.chpok;

import android.media.Image;

/**
 * Created by iMac on 15.06.17.
 */

public class Message {
    private String mText;
    private String mPngUrl;
    private Image mPngImg;

    public Message(String text, String pngUrl, Image pngImg) {
        this.mText = text;
        this.mPngUrl = pngUrl;
        this.mPngImg = pngImg;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getPngUrl() {
        return mPngUrl;
    }

    public void setPngUrl(String mPngUrl) {
        this.mPngUrl = mPngUrl;
    }

    public Image getPngImg() {
        return mPngImg;
    }

    public void setPngImg(Image mPngImg) {
        this.mPngImg = mPngImg;
    }
}

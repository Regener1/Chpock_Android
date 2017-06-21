package com.example.user.chpok;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by iMac on 15.06.17.
 */

public class CustomTextView extends TextView {

    private float mScreenDensity;

    public void setScreenDensity(float screenDensity) {
        mScreenDensity = screenDensity;
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomTextView(Context context) {
        super(context);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float k = getWidth();
        float l = getHeight();
        int letterCount = getText().length();
        float size = (float)Math.sqrt(k * l/ letterCount) / mScreenDensity;

        if(size > 26){
            size = 26;
        }

        setTextSize(size);
    }
}

package com.example.user.chpok;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by iMac on 15.06.17.
 */

public class CustomTextView extends TextView {

    private int mWidth = 0;
    private int mHeight = 0;
    Context mContext;

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_font);
            try {
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeface);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            a.recycle();
        }
    }

    private boolean isFitTextHeight(){
//        Paint p = new Paint();
//        Rect bounds = new Rect();
//        p.setTextSize(this.getTextSize());
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + "FiveMinutes.ttf");
//        p.getTextBounds(this.getText().toString(), 0, this.getText().length(), bounds);
//
//        int i = 0;
//        return true;

        this.measure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        int height = this.getMeasuredHeight();

        return true;
    }

    public void setAutoSizeText() {

        while(!isFitTextHeight()) {
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, this.getTextSize() - 1f);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        setAutoSizeText();
    }
}

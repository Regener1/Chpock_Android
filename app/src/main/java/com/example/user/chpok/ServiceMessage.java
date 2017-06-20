package com.example.user.chpok;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iMac on 15.06.17.
 */

public class ServiceMessage {
    private static final String MYLOG = "MYLOG";

    private static ServiceMessage mInstance;
    private List<MessageModel> mModels = null;

    private ServiceMessage(){}

    public static ServiceMessage getInstance(){
        Log.i(MYLOG, "ServiceMessage::getInstance()");

        if(mInstance == null){
            mInstance = new ServiceMessage();

        }

        return mInstance;
    }

    public List<MessageModel> getAllMess() {
        if(mModels == null){
            mModels = new ArrayList<>();
            createModels();
        }

        return mModels;
    }

    private void createModels(){
        mModels.add(new MessageModel("Текст на первой странице" +
                "Это очень большой текст на первой странице" +
                "Это очень большой текст на первой странице" +
                "Это очень большой текст на первой странице" +
                "Это очень большой текст на первой странице" +
                "Это очень большой текст на первой странице","", Bitmap.createBitmap(200,200, Bitmap.Config.ALPHA_8)));
        mModels.add(new MessageModel("Текст на второй странице","", Bitmap.createBitmap(200,200, Bitmap.Config.ALPHA_8)));
        mModels.add(new MessageModel("Текст на третьей странице","", Bitmap.createBitmap(200,200, Bitmap.Config.ALPHA_8)));
        mModels.add(new MessageModel("Текст на четвнртой странице","", Bitmap.createBitmap(200,200, Bitmap.Config.ALPHA_8)));
    }


}

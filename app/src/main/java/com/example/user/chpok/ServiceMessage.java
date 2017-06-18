package com.example.user.chpok;

import android.util.Log;

/**
 * Created by iMac on 15.06.17.
 */

public class ServiceMessage {
    private static final String MYLOG = "MYLOG";

    private static ServiceMessage mInstance;

    private ServiceMessage(){}

    public static ServiceMessage getInstance(){
        Log.i(MYLOG, "ServiceMessage::getInstance()");

        if(mInstance == null){
            mInstance = new ServiceMessage();
        }

        return mInstance;
    }

    public Message[] getAllMess(){

    }
}

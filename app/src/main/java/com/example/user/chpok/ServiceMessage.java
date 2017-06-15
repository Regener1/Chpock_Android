package com.example.user.chpok;

import android.util.Log;

/**
 * Created by iMac on 15.06.17.
 */

public class ServiceMessage {
    private static final String MYLOG = "MYLOG";

    private static ServiceMessage mInstance;
    private Message mMessage;

    public static ServiceMessage getInstance(){
        Log.i(MYLOG, "ServiceMessage::getInstance()");

        if(mInstance == null){
            mInstance = new ServiceMessage();
        }

        return mInstance;
    }

    public Message getMessage() {
        return mMessage;
    }

    public void setMessage(Message message) {
        this.mMessage = message;
    }
}

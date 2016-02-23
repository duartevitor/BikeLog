package com.vduarte.bikelog.comm;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.vduarte.bikelog.interfaces.OnTimerTick;

/**
 * Created by Vitor on 12/01/16.
 * The class using this must implement OnTimerTick interface
 */
public class TimerHandler  extends Handler {

    private Context mContext;
    private long    mTimeout;
    private boolean mTicking = false; // default

    @Override
    public void handleMessage(Message msg) {
        if(mTicking) {
            // call function
            ((OnTimerTick) mContext).OnTick();
            // do another tick
            tick();
        }
    }

    public void setContextAndTimeout(Context context,long timeout ){
        this.mContext = context;
        this.mTimeout = timeout;
    }

    public void startTicking() {
        if(!mTicking) {
            mTicking = true;
            tick();
        }
    }

    public void stopTicking(){
        mTicking = false;
    }

    private void tick(){
        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), mTimeout);
    }
}

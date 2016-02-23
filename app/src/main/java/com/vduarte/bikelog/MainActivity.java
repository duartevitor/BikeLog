    package com.vduarte.bikelog;


    import android.app.Activity;
    import android.app.PendingIntent;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.Intent;
    import android.content.IntentFilter;
    import android.os.Bundle;
    import android.os.Debug;
    import android.telephony.SmsManager;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;

    import com.vduarte.bikelog.R;
    import com.vduarte.bikelog.comm.SMSComm;
    import com.vduarte.bikelog.comm.TimerHandler;
    import com.vduarte.bikelog.interfaces.OnTimerTick;
    import com.vduarte.bikelog.views.LocationActivity;

    import org.w3c.dom.Text;

    public class MainActivity extends Activity implements View.OnClickListener, OnTimerTick{

        private SMSComm mSMS;

        private TextView mTick;
        private int mSeconds;
        private TimerHandler mTimerHandler;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            // start relevant objects
            initialize();

            Button smsButton = (Button)findViewById(R.id.buttonSMS);
            smsButton.setOnClickListener(this);

            mTick = (TextView)findViewById(R.id.txtTick);

            // ignore all above and start activity Location for testing
            Intent intent = new Intent(this, LocationActivity.class);
            //String message = editText.getText().toString();
            //intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }

        private void initialize(){
           // this.mSMS = new SMSComm(this);

            //mTimerHandler = new TimerHandler();
            //mTimerHandler.setContextAndTimeout(this,1000);
            //mSeconds = 0;
        }


        @Override
        public void onClick(View v) {
            // send sms with message
            // working | tested
            //this.mSMS.sendSMS("933479252", "SMS test");

            // ticks
            // working | tested
            //mTimerHandler.startTicking();
        }

        @Override
        public void OnTick() {
           // updateTimer();
        }

        private void updateTimer(){
            int hr = mSeconds/3600;
            int rem = mSeconds%3600;
            int mn = rem/60;
            int sec = rem%60;
            String hrStr = (hr<10 ? "0" : "")+hr;
            String mnStr = (mn<10 ? "0" : "")+mn;
            String secStr = (sec<10 ? "0" : "")+sec;

            mTick.setText(hrStr+":"+mnStr+":"+secStr);

            mSeconds++; // inc

            // test case Do Something
           // if(mSeconds == 65)
           //     mTimerHandler.stopTicking();
        }
    }

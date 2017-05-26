package com.chkry.callendprogram;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

//import com.chkry.callterminator.R;

public class MainActivity extends AppCompatActivity {


    public final static Runnable mLaunchTask = new Runnable() {
        public void run() {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse(String.format("tel:%s", Uri.encode(SmsReceiver.Dial_No))));
            SmsReceiver.mContext.startActivity(callIntent);
        }
    };
    public SmsReceiver mSMSreceiver;
    public IntentFilter mIntentFilter;
    public ITelephony telephonyService;
    public final Runnable mRejectCall = new Runnable() {
        public void run() {
            TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            try {
                Class c = Class.forName(telephony.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                telephonyService = (ITelephony) m.invoke(telephony);
                //telephonyService.silenceRinger();
                telephonyService.endCall();
                Log.i("END_CALL", "THE LOG CAME HERE");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button startBtn = (Button) findViewById(R.id.start);
        final Button stopBtn = (Button) findViewById(R.id.stop);

        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startBtn.setEnabled(false);
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startBtn.setEnabled(true);
            }
        });

        mSMSreceiver = new SmsReceiver();
        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(SmsReceiver.ACTION_SMS_RECEIVED);
        registerReceiver(mSMSreceiver, mIntentFilter);


//        Thread t1 = new Thread(mRejectCall);
//        t1.start();


    }
}


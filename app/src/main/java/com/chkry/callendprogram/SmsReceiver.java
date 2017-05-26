package com.chkry.callendprogram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class SmsReceiver extends BroadcastReceiver {
    public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String ACTION_PHONE_CALL_RECEIVED = "android.intent.action.PHONE_STATE";
    public static final String ACTION_PHONE_OUT_CALL = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String ACTION_CUSTOM = "android.intent.action.CUSTOM_DISCONNECT";
    public static String REJECT_INFO = "";
    public static String Dial_No = "";
    public static Context mContext = null;
    public ITelephony telephonyService;
    SmsReceiver s;
    String pwd = "";

    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();
        if (action.equals(ACTION_SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String str = "";

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                msgs[0] = SmsMessage.createFromPdu((byte[]) pdus[0]);
                Dial_No = msgs[0].getOriginatingAddress();
                str = msgs[0].getMessageBody().toString();

                if (str.equalsIgnoreCase("CALL")) {
                    Log.e("SEROTONIN", "onReceive(), str=" + str);
                    mContext = context;
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(MainActivity.mLaunchTask, 4000);

                } else if (str.equalsIgnoreCase("REJECT")) {
                    REJECT_INFO = "OTHER";

                    Intent i = new Intent(ACTION_CUSTOM);
                    context.sendBroadcast(i);
                } else if (str.equalsIgnoreCase("ANSWER")) {
                    REJECT_INFO = str;

                }
                Toast.makeText(context, "got it" + str, Toast.LENGTH_SHORT).show();
            }
        }


        if (action.equals(ACTION_PHONE_CALL_RECEIVED) && REJECT_INFO.equals("REJECT")) {
            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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

//		if(action.equals(ACTION_CUSTOM)){
//			TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			try {
//				Class c = Class.forName(telephony.getClass().getName());
//				Method m = c.getDeclaredMethod("getITelephony");
//				m.setAccessible(true);
//				telephonyService = (ITelephony) m.invoke(telephony);
//				//telephonyService.silenceRinger();
//				telephonyService.endCall();
//				Log.i("END_CALL", "THE LOG CAME HERE");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

    }

}

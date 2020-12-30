package com.example.rdvgeolocalise.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;

public class SMSReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sBuilder = new StringBuilder();
        StringBuilder content = new StringBuilder();//用于存储短信内容
        String sender = null;//存储短信发送方手机号
        Bundle bundle = intent.getExtras();//通过getExtras()方法获取短信内容
        String format = intent.getStringExtra("format");
        if (bundle != null) {
            Object[] pdus = (Object[])bundle.get("pdus");
            assert pdus != null;
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for(int i = 0; i < messages.length; ++i)
            {
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i],format);
            }
            for(SmsMessage msg : messages)
            {
                sBuilder.append("来自：").append(msg.getDisplayOriginatingAddress()).append("\n").append("短信内容：");
                sBuilder.append(msg.getDisplayMessageBody()).append("\n");
            }
        }
        Toast.makeText(context, "吆喝，快看!! 您收到了一条短信!!\n" + sBuilder.toString(), Toast.LENGTH_LONG).show();
    }
}

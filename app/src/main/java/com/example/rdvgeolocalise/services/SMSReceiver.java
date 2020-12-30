package com.example.rdvgeolocalise.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.rdvgeolocalise.utils.NotificationUtils;


public class SMSReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String smsBody = getMsg(context, intent);
        if(isLocation(smsBody)){
            NotificationUtils notificationUtils = new NotificationUtils(context);
            notificationUtils.sendInvitationMsg(smsBody);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getMsg(Context context, Intent intent) {
        //pdus短信单位pdu
        //解析短信内容
        StringBuilder content = new StringBuilder();//用于存储短信内容
        Bundle bundle = intent.getExtras();//通过getExtras()方法获取短信内容
        String format = intent.getStringExtra("format");
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");//根据pdus关键字获取短信字节数组，数组内的每个元素都是一条短信
            for (Object object : pdus) {
                SmsMessage message=SmsMessage.createFromPdu((byte[])object,format);//将字节数组转化为Message对象
                content.append(message.getMessageBody());//获取短信内容
            }
        }
        return content.toString();
    }

    private boolean isLocation(String smsBody){
        if(smsBody.contains("Longitude") && smsBody.contains("Latitude")){
            return true;
        } else {
            return false;
        }
    }
}



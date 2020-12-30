package com.example.rdvgeolocalise.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import androidx.annotation.RequiresApi;

import com.example.rdvgeolocalise.utils.NotificationUtils;


public class SMSReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String smsBody = getMsg(intent);
        String sender = getSender(intent);
        if(isReply(smsBody)){
                NotificationUtils notificationUtils = new NotificationUtils(context);
                notificationUtils.sendReplyMsg(smsBody, sender);
        } else {
            if(isLocation(smsBody)){
                NotificationUtils notificationUtils = new NotificationUtils(context);
                notificationUtils.sendInvitationMsg(smsBody, sender);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getSender(Intent intent){
        String sender = null;//存储短信发送方手机号
        Bundle bundle = intent.getExtras();//通过getExtras()方法获取短信内容
        String format = intent.getStringExtra("format");
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");//根据pdus关键字获取短信字节数组，数组内的每个元素都是一条短信
            for (Object object : pdus) {
                SmsMessage message=SmsMessage.createFromPdu((byte[])object,format);//将字节数组转化为Message对象
                sender = message.getOriginatingAddress();//获取短信手机号
            }
        }
        return sender.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getMsg(Intent intent) {
        //pdus短信单位pdu
        //解析短信内容
        StringBuilder content = new StringBuilder();//用于存储短信内容
        String sender = null;//存储短信发送方手机号
        Bundle bundle = intent.getExtras();//通过getExtras()方法获取短信内容
        String format = intent.getStringExtra("format");
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");//根据pdus关键字获取短信字节数组，数组内的每个元素都是一条短信
            for (Object object : pdus) {
                SmsMessage message=SmsMessage.createFromPdu((byte[])object,format);//将字节数组转化为Message对象
                sender = message.getOriginatingAddress();//获取短信手机号
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

    private boolean isReply(String smsBody){
        if(smsBody.contains("Longitude") && smsBody.contains("Latitude") && smsBody.contains("Reply")){
            return true;
        } else {
            return false;
        }
    }
}



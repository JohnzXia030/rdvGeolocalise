package com.example.rdvgeolocalise.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.rdvgeolocalise.MainActivity;
import com.example.rdvgeolocalise.R;
import com.example.rdvgeolocalise.services.NotificationClickReceiver;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {
    private Context context;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    //创建通知渠道
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "invitation";
            String channelName = "约会邀请";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    }

    //发送收到邀请通知
    public void sendInvitationMsg(String location, String initiator) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("您收到一条约会邀请，位于：\n").append(location);

        Intent intentClick =new Intent (context, NotificationClickReceiver.class);
        intentClick.setAction("notification_clicked");
        intentClick.putExtra("LOCATION", location);
        intentClick.putExtra("INITIATOR", initiator);
        PendingIntent pendingIntentClick =PendingIntent.getBroadcast(context, 1003, intentClick, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "invitation")
                .setContentTitle("收到一条邀请")
                .setContentText("您收到一条来自："+ initiator +"\n的约会邀请，请点击于应用内查看")
                .setSmallIcon(R.drawable.edit_bg)
                .setContentIntent(pendingIntentClick)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("收到一条邀请")
                .bigText("您收到一条来自："+ initiator +"\n的约会邀请，位于：\n"+ location +"\n请点击于应用内查看");
        builder.setStyle(bigTextStyle);


        Notification notification = builder.build();

        manager.notify(1, notification);
    }

    public void sendReplyMsg(String msg, String replier){
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "invitation")
                .setContentTitle("收到一条回复")
                .setContentText("您收到一条来自："+ replier +"\n的邀请回复，位于：\n"+ msg)
                .setSmallIcon(R.drawable.edit_bg)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("收到一条回复")
                .bigText("您收到一条来自："+ replier +"\n的邀请回复，位于：\n"+ msg);
        builder.setStyle(bigTextStyle);

        Notification notification = builder.build();

        manager.notify(2, notification);
    }
}

package com.example.rdvgeolocalise.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.rdvgeolocalise.R;

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
    public void sendInvitationMsg(String location) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "invitation")
                .setContentTitle("收到一条邀请")
                .setContentText("您收到一条约会邀请，位于：\n"+ location)
                .setSmallIcon(R.drawable.edit_bg)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("收到一条邀请")
                .bigText("您收到一条约会邀请，位于：\n"+ location);
        builder.setStyle(bigTextStyle);

        Notification notification = builder.build();

        manager.notify(1, notification);
    }
}

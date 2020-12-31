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
            String channelName = "Appointment invitation";
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
        stringBuilder.append("You have received an invitation of an appointment，located at:\n").append(location);

        Intent intentClick =new Intent (context, NotificationClickReceiver.class);
        intentClick.setAction("notification_clicked");
        intentClick.putExtra("LOCATION", location);
        intentClick.putExtra("INITIATOR", initiator);
        PendingIntent pendingIntentClick =PendingIntent.getBroadcast(context, 1003, intentClick, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "invitation")
                .setContentTitle("Appointment invitation")
                .setContentText("You have received an invitation of an appointment from: "+ initiator +"\n, please click to check it in the application!")
                .setSmallIcon(R.drawable.edit_bg)
                .setContentIntent(pendingIntentClick)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Appointment invitation")
                .bigText("You have received an invitation of an appointment from: "+ initiator +"\n, located at: \n"+ location +"\n, please click to check it in the application!");
        builder.setStyle(bigTextStyle);


        Notification notification = builder.build();

        manager.notify(1, notification);
    }

    public void sendReplyMsg(String msg, String replier){
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "invitation")
                .setContentTitle("Invitation reply")
                .setContentText("You have received a reply of an invitation from: "+ replier +"\n, located at:\n"+ msg)
                .setSmallIcon(R.drawable.edit_bg)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Invitation reply")
                .bigText("You have received a reply of an invitation from: "+ replier +"\n, located at:\n"+ msg);
        builder.setStyle(bigTextStyle);

        Notification notification = builder.build();

        manager.notify(2, notification);
    }
}

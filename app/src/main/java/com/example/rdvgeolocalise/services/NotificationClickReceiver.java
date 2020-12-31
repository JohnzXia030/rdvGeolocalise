package com.example.rdvgeolocalise.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.example.rdvgeolocalise.MainActivity2;

public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("notification_clicked")) {
            //处理点击事件
            StringBuilder viewMsgBuilder = new StringBuilder();
            String initiator = intent.getStringExtra("INITIATOR");
            String location = intent.getStringExtra("LOCATION");
            viewMsgBuilder.append("You have received an invitation of an appointment from："+ initiator +"\nlocated at：\n")
                    .append(location);
            String viewMsg = viewMsgBuilder.toString();
            Intent newIntent = new Intent(context, MainActivity2.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.putExtra("viewMsg", viewMsg);
            newIntent.putExtra("INITIATOR", initiator);
            newIntent.putExtra("LOCATION", location);
            context.startActivity(newIntent);
        }


    }
}

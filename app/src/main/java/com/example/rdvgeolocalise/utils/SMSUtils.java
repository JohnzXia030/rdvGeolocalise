package com.example.rdvgeolocalise.utils;

import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;

import com.example.rdvgeolocalise.utils.GPSUtils;

import java.util.List;

public class SMSUtils {
    //参数为选择的联系人列表
    public void sendLocationAndInvitation(List<String> listContacts, Context context, Location location) {
        GPSUtils gpsUtils = new GPSUtils(context);
        for (String contactNumber:
                listContacts
             ) {
            String message = gpsUtils.getLocation().toString();
            SmsManager.getDefault().sendTextMessage(contactNumber,null,message,null,null);
        }
    }


}

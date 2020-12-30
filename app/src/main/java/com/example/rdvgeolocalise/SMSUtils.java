package com.example.rdvgeolocalise;

import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

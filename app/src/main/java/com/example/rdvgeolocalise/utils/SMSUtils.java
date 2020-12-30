package com.example.rdvgeolocalise.utils;

import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;

import com.example.rdvgeolocalise.utils.GPSUtils;

import java.util.List;
import java.util.UUID;

public class SMSUtils {
    public static String getUUID(){
        UUID uuid= UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }

    //参数为选择的联系人列表
    public void sendLocationAndInvitation(List<String> listContacts, Context context, Location location) {
        try{
            for (String contactNumber:
                    listContacts
            ) {
                StringBuilder sMessage = new StringBuilder();
                sMessage.append("Longitude：").append(location.getLongitude())
                        .append("\nLatitude：").append(location.getLatitude());
                System.out.println(sMessage.toString());
                System.out.println(contactNumber);
                SmsManager.getDefault().sendTextMessage(contactNumber,null,sMessage.toString(),null,null);
            }
            listContacts.clear();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void sendReplyAccepted(String numberReply,String msg){
        try{
            StringBuilder sMessage = new StringBuilder();
            sMessage.append(msg)
                    .append("\nReply:").append("Accepted");
            SmsManager.getDefault().sendTextMessage(numberReply,null,sMessage.toString(),null,null);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void sendReplyRefused(String numberReply, String msg){
        try{
            StringBuilder sMessage = new StringBuilder();
            sMessage.append(msg)
                    .append("\nReply:").append("Refused");
            SmsManager.getDefault().sendTextMessage(numberReply,null,sMessage.toString(),null,null);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}

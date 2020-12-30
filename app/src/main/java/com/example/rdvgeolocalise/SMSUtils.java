package com.example.rdvgeolocalise;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSUtils {
    //参数为选择的联系人列表
    public boolean sendLocationAndInvitation(List<String> listContacts, Context context) {
        if(listContacts.size()<=0){
            CharSequence text = "还没有输入号码或者选择联系人！";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {
            for (int j = 0; j<listContacts.size(); j++) {
                //判断字符是否包含“；”
                if(listContacts.get(j).contains(";")){
                    String[] numberSplitList = listContacts.get(j).split(";");
                    //判断分割后的字符数组是否为空
                    if(numberSplitList.length<=0){
                        CharSequence text = "输入的号码为空！";
                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                        toast.show();
                        return false;
                    } else {
                        //如果不为空则判断是否只有一个号码
                        //开始判断每个号码的合法性
                        if(numberSplitList.length>1){
                            for(int i = 0; i < numberSplitList.length; i++){
                                //判断是否是数字
                                if (!this.isNumeric(numberSplitList[i])) {
                                    CharSequence text = "输入的数字包含非法字符！";
                                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                                    toast.show();
                                    return false;
                                } else {
                                    //若号码合法则将号码加入listContacts
                                    listContacts.add(numberSplitList[i]);
                                }
                            }
                            //移除未分割的字符串
                            listContacts.remove(j);
                        }
                    }
                } else {
                    //若号码不包含“；”则开始直接判断其合法性

                }
                if (this.isNumeric(listContacts.get(j))) {
                    CharSequence text = "输入的数字包含非法字符！";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                    return false;
                }
//                if (contactNumber.isEmpty()) {
//                    CharSequence text = "Le message est vide, veuillez tapez le message s'il vous plait! ";
//                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//                    toast.show();
//                } else {
//                    try {
//                        SmsManager.getDefault().sendTextMessage(number, null, message, null, null);
//                        String sendSuccess = "Le message a été envoyé.";
//                        Toast toast = Toast.makeText(context, sendSuccess, duration);
//                        toast.show();
//                        System.out.println("发送成功");
//                    } catch (Exception e) {
//                        Toast toast = Toast.makeText(context, e.getMessage(), duration);
//                        toast.show();
//                    }
//                }
            }
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


}

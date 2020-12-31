package com.example.rdvgeolocalise;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdvgeolocalise.services.SMSReceiver;
import com.example.rdvgeolocalise.services.SMSService;
import com.example.rdvgeolocalise.utils.ContactUtil;
import com.example.rdvgeolocalise.utils.GPSUtils;
import com.example.rdvgeolocalise.utils.NotificationUtils;
import com.example.rdvgeolocalise.utils.PermissionUtils;
import com.example.rdvgeolocalise.utils.SMSUtils;
import com.example.rdvgeolocalise.widgets.MultiSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CONTACT = 1;
    private JSONObject contactData;
    private JSONObject jsonObject;
    public static final String NUM_REGEX = "^[+]?[0-9]{10,13}$";
    private static final List<String> totalNumArrayToSend = new ArrayList<String>();
    private static final List<String> contactNumArray = new ArrayList<String>();
    private static final List<String> enteredNumArray = new ArrayList<String>();
    private static final String TAG = MainActivity.class.getSimpleName();

    private static Location location ;
    private SMSReceiver mSmsReceiver;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Obtenir les coordonness de GPS
         */



        //启动时添加SMSService

        mSmsReceiver = new SMSReceiver();
        IntentFilter smsfilter = new IntentFilter(SMSReceiver.SMS_RECEIVED);
        smsfilter.setPriority(2147483647);
        registerReceiver(mSmsReceiver, smsfilter);

        Intent intent = new Intent(MainActivity.this, SMSService.class);
        startService(intent);

        //注册通知渠道
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.createNotificationChannels();

    }

    /**
     * 暂时不管
     * @param view
     * @throws JSONException
     */
    public void addContact(View view) throws JSONException {
        ContactUtil contactUtil = new ContactUtil(MainActivity.this);
        /*contactUtil.getContactInfo();
        getContactInfo();*/
        Toast.makeText(MainActivity.this,contactUtil.getContactInfo(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_PICK);

        intent.setData(ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(intent, REQUEST_CONTACT);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Context context = getApplicationContext();
        PermissionUtils permissionUtils = new PermissionUtils();
        permissionUtils.request_permissions(context, this);
        /**
         * Load contact info
         */
        setContentView(R.layout.activity_main);
        ContactUtil contactUtil = new ContactUtil(MainActivity.this);
        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.multi_spinner);
        List<String> items = new ArrayList<String>();
        String contactInfo;
        try {
            contactInfo = contactUtil.getContactInfo();
            JSONObject jsonObject = new JSONObject(contactInfo);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                JSONObject contact = (JSONObject) jsonObject.get(key);
                items.add(contact.getString("lastname") + " : " + contact.getString("mobile"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        multiSpinner.setItems(items, "Select contact", selected -> {
            contactNumArray.clear();
            for (int i = 0; i < items.size(); i++) {
                if (selected[i]) {
                   // Toast.makeText(MainActivity.this, items.get(i), Toast.LENGTH_SHORT).show();
                    String num = items.get(i).substring(items.get(i).lastIndexOf(":") + 1);
                    //Toast.makeText(MainActivity. this, num  , Toast.LENGTH_SHORT).show();
                    contactNumArray.add(num);
                }
            }
            for (int i = 0; i < contactNumArray.size(); i++) {
                Toast.makeText(MainActivity.this, contactNumArray.get(i), Toast.LENGTH_SHORT).show();

            }
        });
        /**
         * Add edittext listener
         */
        EditText numeroView = findViewById(R.id.numEntered);
        numeroView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                toast("Veuillez saisir le(s) numéros sous forms de numéro en 'NUM1; NUM2; ...'");
            }
        });

        Intent intent1 = this.getIntent();
        /*Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null) {*/
        location = new Location("");
        location.setLatitude(intent1.getDoubleExtra("lat",0 ));
        location.setLongitude(intent1.getDoubleExtra("lng",0 ));
        TextView gps = (TextView) findViewById(R.id.gps);

        gps.setText("Lat:" + location.getLatitude() + "\nLng:" + location.getLongitude());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0) {
                    // 因为是多个权限，所以需要一个循环获取每个权限的获取情况
                    for (int i = 0; i < grantResults.length; i++) {
                        // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                            Toast.makeText(MainActivity.this, permissions[i] + "permission has been denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    public Location getGps(View view){
        GPSUtils gpsUtils = GPSUtils.getInstance(MainActivity.this);
        location = gpsUtils.getLocation();
        TextView gps = (TextView)findViewById(R.id.gps);
        if(location!=null){
            gps.setText("Lat:" + location.getLatitude() + ";\nLng" + location.getLongitude());
        } else {
            gps.setText("No info of location");
        }
        return location;
    }

    public void sendSms(View view){
        EditText numberView = (EditText) findViewById(R.id.numEntered);
        String numbers = numberView.getText().toString();
        String[] arrNumber = numbers.split(";");
        String formatedNum;
        for (String num: arrNumber){
            if(!num.isEmpty()) {
                formatedNum = PhoneNumberUtils.formatNumber(num);
                enteredNumArray.add(formatedNum);
            }
        }
        enteredNumArray.addAll(contactNumArray);
        SMSUtils smsUtils = new SMSUtils();
        smsUtils.sendLocationAndInvitation(enteredNumArray,this, location);
    }

    public void rendezVous(View view){
        Intent intent = new Intent(this,GestionRendezVous.class);
        startActivity(intent);
    }

    public void choisirLocation(View view){
        Intent intent = new Intent(this, ChoosePositionMapsActivity.class);
        startActivity(intent);
    }
    public void toast(String s){
        Toast.makeText(getApplication() , s , Toast.LENGTH_SHORT).show();
    }
}
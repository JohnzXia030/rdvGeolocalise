package com.example.rdvgeolocalise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Data;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CONTACT = 1;
    private JSONObject contactData;
    private JSONObject jsonObject;
    private static final List<String> totalNumArrayToSend = new ArrayList<String>();
    private static final List<String> contactNumArray = new ArrayList<String>();
    private static final List<String> enteredNumArray = new ArrayList<String>();
    private static final String TAG = MainActivity.class.getSimpleName();
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
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
        EditText numeroView = (EditText) findViewById(R.id.num);
        numeroView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                toast("Veuillez saisir le(s) numéros sous forms de numéro en'NUM1; NUM2; ...'");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                            Toast.makeText(MainActivity.this, permissions[i] + "权限被拒绝了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    private String getGps(View view){
        GPSUtils gpsUtils = GPSUtils.getInstance(this);
        location = gpsUtils.getLocation();
        return null;
    }

    private void toast(String s){
        Toast.makeText(getApplication(),s,Toast.LENGTH_SHORT).show();
    }
}
package com.example.rdvgeolocalise;

import android.content.Intent;
import android.os.Bundle;

import com.example.rdvgeolocalise.utils.SMSUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Invitation received!");
        setSupportActionBar(toolbar);

        Intent intent =getIntent();
        String msg=intent.getStringExtra("viewMsg");
        TextView textView=(TextView)findViewById(R.id.textView);
        textView.setText(msg);
        textView.setTextSize(18);

        Button buttonAccept = findViewById(R.id.buttonAccept);
        Button buttonRefuse = findViewById(R.id.buttonRefuse);
        Button buttonShowInMap = findViewById(R.id.buttonShowInMap);

        buttonAccept.setText("ACCEPT");
        buttonRefuse.setText("REFUSE");
        buttonShowInMap.setText("SHOW THIS PLACE IN MAP");

    }

    public void acceptInvitation(View view){
        Intent intent =getIntent();
        SMSUtils smsUtils = new SMSUtils();
        System.out.println("接收邀请");
        smsUtils.sendReplyAccepted(intent.getStringExtra("INITIATOR"),intent.getStringExtra("LOCATION"));
        Toast.makeText(this, "消息已发送", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity2.this, MainActivity.class));
    }

    public void refuseInvitation(View view){
        Intent intent =getIntent();
        SMSUtils smsUtils = new SMSUtils();
        System.out.println("拒绝邀请");
        smsUtils.sendReplyRefused(intent.getStringExtra("INITIATOR"),intent.getStringExtra("LOCATION"));
        Toast.makeText(this, "消息已发送", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity2.this, MainActivity.class));
    }

    public void showInMap(View view){
        Intent intent =getIntent();
        String location = intent.getStringExtra("LOCATION");
        Intent newIntent = new Intent(MainActivity2.this, MapsActivity.class);
        newIntent.putExtra("LOCATION", location);
        startActivity(newIntent);
    }

}
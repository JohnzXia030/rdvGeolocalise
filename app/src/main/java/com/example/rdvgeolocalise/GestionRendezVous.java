package com.example.rdvgeolocalise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rdvgeolocalise.R;

import static com.example.rdvgeolocalise.services.SMSReceiver.invitationAccepted;

public class GestionRendezVous extends AppCompatActivity {


    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_rendez_vous);


    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = (ListView)findViewById(R.id.listInvitationAccepted);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, invitationAccepted);
        listView.setAdapter(adapter);
    }
}
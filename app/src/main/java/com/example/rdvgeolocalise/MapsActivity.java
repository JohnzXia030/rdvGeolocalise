package com.example.rdvgeolocalise;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        String location = intent.getStringExtra("LOCATION");
        String[] num = location.split(";");
        String longitude = num[0].replaceAll("[^\\d.-]+", "");
        String latitude = num[1].replaceAll("[^\\d.-]+", "");

        System.out.println(longitude);
        System.out.println(latitude);
        // Add a marker in Sydney and move the camera
        LatLng pos = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        System.out.println(pos.toString());
        mMap.addMarker(new MarkerOptions().position(pos).title("Position"));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(pos, 18, 0, 0)));
    }


}
package com.ninefives.driverhere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapSearch extends AppCompatActivity
        implements OnMapReadyCallback {

    EditText edit4;
    EditText edit5;

    String Lati;
    String Long;

    String GPSLATI;
    String GPSLONG;
    String ROUTENM;

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsearch);

        edit4 = (EditText) findViewById(R.id.edit4);
        edit5 = (EditText) findViewById(R.id.edit5);


        Intent intent = getIntent();

        GPSLATI = intent.getStringExtra("gpsLati");
        GPSLONG = intent.getStringExtra("gpsLong");
        ROUTENM = intent.getStringExtra("routeNm");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Lati = edit4.getText().toString();
                Long = edit5.getText().toString();

                LatLng MAKER = new LatLng(Double.parseDouble(Lati), Double.parseDouble(Long));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(MAKER);
                markerOptions.title("내 위치");
                mMap.addMarker(markerOptions);

            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        LatLng BUS = new LatLng(36.809, 127.146);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(BUS);
        markerOptions.title(ROUTENM);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(BUS));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

}
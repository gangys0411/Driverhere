package com.ninefives.driverhere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class RouteMap extends AppCompatActivity
        implements OnMapReadyCallback {

    EditText edit4;
    EditText edit5;

    String Lati;
    String Long;

    String GPSLATI;
    String GPSLONG;
    String ROUTENM;

    GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routemap);

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        // 구글 맵 객체를 불러온다.
        mMap = googleMap;

        // 기준 위치 설정
        LatLng BUS = new LatLng(36.809, 127.146);

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.station_marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120, 50, false);

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(BUS)
                .title("테스트")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        // 마커를 생성한다.
        mMap.addMarker(markerOptions);

        //카메라를 기준 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(BUS));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

}
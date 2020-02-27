package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ninefives.driverhere.bus_search.route_search.BusRouteSearch;
import com.ninefives.driverhere.station_search.station_search.BusStationSearch;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void BusSearchClick(View view){
        Intent intent = new Intent(this, BusRouteSearch.class);
        startActivity(intent);
    }

    public void StationSearchClick(View view){
        Intent intent = new Intent(this, BusStationSearch.class);
        startActivity(intent);
    }

    public void AroundStationSearchClick(View view){
        Intent intent = new Intent(this, AlarmSend.class);
        startActivity(intent);
    }

    /*public void AroundStationSearchClick(View view){
        Intent intent = new Intent(this, AroundBusStationSearch.class);
        startActivity(intent);
    }*/ // 알람용

    public void mOnClick(View view){
        Intent intent = new Intent(this, List.class);
        startActivity(intent);
    }

    /*public void mOnClick(View view){
        Intent intent = new Intent(this, BusLocate.class);
        startActivity(intent);
    }*/ // 버스 위치
}
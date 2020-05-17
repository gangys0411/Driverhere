package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReserveResult extends Activity {
    TextView select_bus;
    TextView select_station;
    TextView reserve_time;

    String busno; // 버스 번호
    String busid; // 노선 ID
    String stationno; // 정류소 번호
    String stationnm; // 정류소 이름
    String selecttime; // 예약 시간

    String routeid; // 경로 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserveresult);

        select_bus = (TextView) findViewById(R.id.select_bus_num);
        select_station = (TextView) findViewById(R.id.select_station_nm);
        reserve_time = (TextView) findViewById(R.id.reserve_time);

        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장
        stationno = intent.getStringExtra("NodeNo"); // 인탠트로 받아온 정류소 ID 저장
        stationnm = intent.getStringExtra("NodeNm"); // 인탠트로 받아온 정류소 이름 저장
        selecttime = intent.getStringExtra("SelectTime"); // 인탠트로 받아온 예약 시간

        routeid = stationno+busid;

        select_bus.setText(busno);
        select_station.setText(stationnm);
        reserve_time.setText(selecttime);
    }
}

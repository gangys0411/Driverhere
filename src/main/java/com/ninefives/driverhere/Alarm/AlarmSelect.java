package com.ninefives.driverhere.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ninefives.driverhere.R;

public class AlarmSelect extends Activity {

    TextView select_bus;
    TextView select_station;

    String busid;
    String busno;
    String nodeid;
    String nodeno;
    String nodenm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmselect);

        select_bus = (TextView)findViewById(R.id.select_bus_num);
        select_station = (TextView)findViewById(R.id.select_station_name);

        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장
        nodeid = intent.getStringExtra("NodeID"); // 인탠트로 받아온 정류소 ID 저장
        nodeno = intent.getStringExtra("NodeNo"); // 인탠트로 받아온 정류소 번호 저장
        nodenm = intent.getStringExtra("NodeNm"); // 인탠트로 받아온 정류소 이름 저장

        select_bus.setText(busno);
        select_station.setText(nodenm);
    }

    public void select_send(View v){
        Intent intent = new Intent(this, AlarmResult.class); // 알림 체크로 넘어감

        intent.putExtra("BusID", busid); // 인탠트에 현재 버스 데이터를 전달
        intent.putExtra("BusNo", busno);
        intent.putExtra("NodeID", nodeid);
        intent.putExtra("NodeNo", nodeno);
        intent.putExtra("NodeNm", nodenm);

        startActivity(intent);
    }

    public void select_reserve(View v){
        Intent intent = new Intent(this, AlarmReserve.class); // 알림 체크로 넘어감

        intent.putExtra("BusID", busid); // 인탠트에 현재 버스 데이터를 전달
        intent.putExtra("BusNo", busno);
        intent.putExtra("NodeID", nodeid);
        intent.putExtra("NodeNo", nodeno);
        intent.putExtra("NodeNm", nodenm);

        startActivity(intent);
    }
}

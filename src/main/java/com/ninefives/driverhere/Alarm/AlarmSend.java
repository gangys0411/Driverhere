package com.ninefives.driverhere.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ninefives.driverhere.DEV.DEV_AlarmSend;
import com.ninefives.driverhere.R;

public class AlarmSend extends Activity {

    TextView select_bus;
    TextView select_station;

    String busno; // 버스 번호
    String routeid; // 노선 ID
    String nodeid; // 정류소 ID
    String nodenm; // 정류소 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmsend);

        select_bus = (TextView)findViewById(R.id.select_bus_num);
        select_station = (TextView)findViewById(R.id.select_station_name);

        Intent intent = getIntent();

        routeid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장
        nodeid = intent.getStringExtra("NodeID"); // 인탠트로 받아온 정류소 ID 저장
        nodenm = intent.getStringExtra("NodeNm"); // 인탠트로 받아온 정류소 이름 저장

        select_bus.setText(busno);
        select_station.setText(nodenm);
    }

    public void aroundcheck(View v) { // 알림보내기 버튼을 누르면
        Intent intent = new Intent(this, AlarmResult.class); // 알림 체크로 넘어감

        intent.putExtra("BusID", routeid); // 인탠트에 현재 버스 데이터를 전달
        intent.putExtra("BusNo", busno);
        intent.putExtra("NodeID", nodeid);
        intent.putExtra("NodeNm", nodenm);

        startActivity(intent);
    }

    public void DEV_alarm(View v) { // 개발자 버튼을 누르면
        Intent intent = new Intent(this, DEV_AlarmSend.class); // 개발자용 화면으로 넘어감

        intent.putExtra("BusID", routeid); // 인탠트에 현재 버스 데이터를 전달
        intent.putExtra("BusNo", busno);

        startActivity(intent);
    }


}

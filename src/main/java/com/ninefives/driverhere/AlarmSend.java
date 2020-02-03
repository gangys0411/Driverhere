package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AlarmSend extends Activity {

    TextView select_bus;

    String busno; // 버스 번호
    String routeid; // 노선 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmsend);

        select_bus = (TextView)findViewById(R.id.select_bus_num);

        Intent intent = getIntent();

        routeid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장

        select_bus.setText(busno);
    }


}

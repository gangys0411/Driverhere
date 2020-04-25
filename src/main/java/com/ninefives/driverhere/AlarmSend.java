package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ninefives.driverhere.DEV.DEV_AlarmSend;

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

    public void DEV_alarm(View v) { // 개발자 버튼을 누르면
        Intent intent = new Intent(this, DEV_AlarmSend.class); // 개발자용 화면으로 넘어감

        intent.putExtra("BusID", routeid); // 인탠트에 현재 버스 데이터를 전달
        intent.putExtra("BusNo", busno);

        startActivity(intent);
    }


}

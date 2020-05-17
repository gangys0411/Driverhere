package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AlarmReserve extends Activity {

    private  Spinner minutespinner;

    String busid;
    String busno;
    String nodeno;
    String nodenm;

    EditText minuteedit;
    TextView timeresult;

    int select_time=0;

    Toast error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmreserve);

        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장
        nodeno = intent.getStringExtra("NodeNo"); // 인탠트로 받아온 정류소 ID 저장
        nodenm = intent.getStringExtra("NodeNm"); // 인탠트로 받아온 정류소 이름 저장

        minuteedit = (EditText)findViewById(R.id.direct_time);
        timeresult = (TextView)findViewById(R.id.time_result);

        minutespinner = (Spinner)findViewById(R.id.select_time);

        final ArrayList<String> timelist = new ArrayList<>();
        timelist.add("시간 선택");
        timelist.add("1분");
        timelist.add("2분");
        timelist.add("3분");
        timelist.add("5분");
        timelist.add("8분");
        timelist.add("10분");
        timelist.add("15분");
        timelist.add("20분");
        timelist.add("30분");

        ArrayAdapter minuteAdapter;
        minuteAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, timelist); // 스피너 어뎁터 연결
        minutespinner.setAdapter(minuteAdapter);

        minutespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { // 어떤 것을 선택하면
                switch(position){
                    case 0 : select_time = 0;
                        break;
                    case 1 : select_time = 1;
                        break;
                    case 2 : select_time = 2;
                        break;
                    case 3 : select_time = 3;
                        break;
                    case 4 : select_time = 5;
                        break;
                    case 5 : select_time = 8;
                        break;
                    case 6 : select_time = 10;
                        break;
                    case 7 : select_time = 15;
                        break;
                    case 8 : select_time = 20;
                        break;
                    case 9 : select_time = 30;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { // 아무것도 선택하지 않았을때
                select_time = 0;
            }
        });
    }

    public void reserve_time(View view){ // 버튼을 클릭하면
        if(minuteedit.getText().toString().getBytes().length <= 0) { // 직접 입력 값이 없다면
            if(select_time == 0){ // 선택 시간 값도 없으면
                error.makeText(this.getApplicationContext(),"예약 시간을 설정해 주세요", Toast.LENGTH_SHORT).show();
            }
            else{ // 선택 시간 값이 있으면
                Intent intent = new Intent(this, ReserveResult.class); // 알림 체크로 넘어감

                intent.putExtra("BusID", busid); // 인탠트에 현재 버스 데이터를 전달
                intent.putExtra("BusNo", busno);
                intent.putExtra("NodeNo", nodeno);
                intent.putExtra("NodeNm", nodenm);
                intent.putExtra("SelectTime", Integer.toString(select_time));

                startActivity(intent);
            }
        }
        else { // 직접 입력 값이 있으면
            Intent intent = new Intent(this, ReserveResult.class); // 알림 체크로 넘어감

            intent.putExtra("BusID", busid); // 인탠트에 현재 버스 데이터를 전달
            intent.putExtra("BusNo", busno);
            intent.putExtra("NodeNo", nodeno);
            intent.putExtra("NodeNm", nodenm);
            intent.putExtra("SelectTime", minuteedit.getText().toString());

            startActivity(intent);
        }
    }
}

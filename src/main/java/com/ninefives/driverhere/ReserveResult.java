package com.ninefives.driverhere;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

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

    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

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

        check_reserve(selecttime);
    }

    public void check_reserve(String time){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                showNoti();
            }
        },Integer.parseInt(time)*60000);
    }

    public void showNoti(){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );

            builder = new NotificationCompat.Builder(this,CHANNEL_ID);

            //하위 버전일 경우
        }else{
            builder = new NotificationCompat.Builder(this);
        }

        Intent intent = new Intent(this, Noti.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //알림창 제목
        builder.setContentTitle("기사님 요기요");

        //알림창 메시지
        builder.setContentText(busno+"번 버스에 알림을 보냈습니다.");

        //알림창 아이콘
        builder.setSmallIcon(R.drawable.hand);

        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
        builder.setAutoCancel(true);

        //pendingIntent를 builder에 설정 해줍니다.
        // 알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        //알림창 실행
        manager.notify(1,notification);
    }
}

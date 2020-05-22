package com.ninefives.driverhere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Noti extends AppCompatActivity {
    Button bt;
    EditText et;
    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_noti);
        et = findViewById(R.id.et);
        bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showNoti(et.getText().toString()); }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if(name!=null){
            //name 값이 저장됐다면 EditText에 띄워줍니다.
            et.setText(name);
        }
    }

    public void showNoti(String name){
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
        intent.putExtra("name",name);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //알림창 제목
        builder.setContentTitle("알림");

        //알림창 메시지
        builder.setContentText("알림 메시지");

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


package com.ninefives.driverhere;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.Task;
import com.ninefives.driverhere.DEV.DEV_AlarmControl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

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

    String mJsonString;

    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserveresult);

        //select_bus = (TextView) findViewById(R.id.select_bus_num);
        select_station = (TextView) findViewById(R.id.select_station_nm);
        reserve_time = (TextView) findViewById(R.id.reserve_time);

        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장
        stationno = intent.getStringExtra("NodeNo"); // 인탠트로 받아온 정류소 번호 저장
        stationnm = intent.getStringExtra("NodeNm"); // 인탠트로 받아온 정류소 이름 저장
        selecttime = intent.getStringExtra("SelectTime"); // 인탠트로 받아온 예약 시간

        routeid = stationno + busid;

        //select_bus.setText(busno);
        select_station.setText(stationnm);
        reserve_time.setText(selecttime);

        check_reserve(selecttime);
    }

    public void check_reserve(String time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locate_check();
            }
        }, Integer.parseInt(time) * 60000);
    }

    public void locate_check() {
        alarm_increase();
        showNoti();
    }

    public void showNoti() {
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //버전 오레오 이상일 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

            //하위 버전일 경우
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        Intent intent = new Intent(this, DEV_AlarmControl.class);
        intent.putExtra("routeID",routeid);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //알림창 제목
        builder.setContentTitle("기사님 요기요");

        //알림창 메시지
        builder.setContentText(busno + "번 버스에 알림을 보냈습니다.");

        //알림창 아이콘
        builder.setSmallIcon(R.drawable.hand);

        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
        builder.setAutoCancel(true);

        //pendingIntent를 builder에 설정 해줍니다.
        // 알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        //알림창 실행
        manager.notify(1, notification);
    }

    public void alarm_increase() { // 대기 승객 수 증가시키기
        AlarmIncrease task = new AlarmIncrease();
        task.execute(routeid);
    }

    private class AlarmIncrease extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "response - " + result);

            if (result != null) { // 결과가 있다면
                mJsonString = result;
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = "http://35.185.229.27/stop_increase.php"; // 접속할 웹서버 주소
            String postParameters = "routeID=" + routeid; // 검색할 내용


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }
}

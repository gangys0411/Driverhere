package com.ninefives.driverhere.Ride_Help;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.Task;
import com.ninefives.driverhere.Alarm.AlarmResult;
import com.ninefives.driverhere.Favorite.TinyDB;
import com.ninefives.driverhere.MainActivity;
import com.ninefives.driverhere.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class RideOutHelpAlarm extends AppCompatActivity {

    NotificationManager manager;
    NotificationCompat.Builder builder;

    String mJsonString;

    String key = "hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    String citycode = "34010"; // 천안 도시 코드

    String busid; // 노선 ID
    String busno; // 버스 번호

    String VehicleNo; // 인탠트 차량 번호

    String stationid; // 검사할 정류소 ID
    String stationnm; // 검사할 정류소 이름
    int stationord; // 검사할 정류소 순서 번호

    int nodeord;
    String nodenm; // 정류소 이름
    String nodeno; // 정류소 번호

    String vehicleno; // 차량 번호

    String routeid; // 경로 ID

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    TimerTask refresh = new TimerTask() {
        @Override
        public void run() {
            getXmlData(busid);
        }
    };

    HelpListViewAdapter adapter = new HelpListViewAdapter(); // 어뎁터 생성

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_rideouthelp);

        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 노선 ID 저장
        VehicleNo = intent.getStringExtra("VehicleNo"); // 인탠트로 받아온 차량 번호 저장
        nodeord = intent.getIntExtra("StationOrd", 0); // 인탠트로 받아온 정류장 ID 저장
        nodenm = intent.getStringExtra("StationNm"); // 인탠트로 받아온 정류장 이름 저장
        nodeno = intent.getStringExtra("StationNo"); // 인탠트로 받아온 정류장 번호 저장

        routeid = nodeno + busid;

        check_bus_locate();

        finish();
    }

    public void check_bus_locate(){
        Timer timer = new Timer();
        timer.schedule(refresh, 0, 30000);
    }

    public void getXmlData(String routeid) {
        String queryUrl = "http://openapi.tago.go.kr/openapi/service/BusLcInfoInqireService/getRouteAcctoBusLcList?" + // 요청 URL
                "serviceKey=" + key + // 서비스 키 추가
                "&cityCode=" + citycode + // 도시 코드 추가
                "&routeId=" + routeid;// 노선 ID 추가

        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); // 파서 선언
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag; // 태그 변수 선언

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) { // 문서의 끝을 만날때 까지 반복
                switch (eventType) {

                    case XmlPullParser.START_TAG: // 시작 태그 별로 행동
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("item")) ;// 하나의 검색결과
                        else if (tag.equals("gpslati")) { // 차량 위도
                            xpp.next();
                        } else if (tag.equals("gpslong")) { // 차량 경도
                            xpp.next();
                        } else if (tag.equals("nodeid")) { // 정류소 ID
                            xpp.next();
                            stationid = xpp.getText();
                        } else if (tag.equals("nodenm")) { // 정류소 이름
                            xpp.next();
                            stationnm = xpp.getText();
                        } else if (tag.equals("nodeord")) { // 정류소 순서
                            xpp.next();
                            stationord = Integer.parseInt(xpp.getText());
                        } else if (tag.equals("routenm")) { // 노선 번호
                            xpp.next();
                        } else if (tag.equals("routetp")) { // 노선 타입
                            xpp.next();
                        } else if (tag.equals("vehicleno")) { // 차량 번호
                            xpp.next();
                            vehicleno = xpp.getText();
                        }
                        break;

                    case XmlPullParser.TEXT: // 텍스트라면 스킵
                        break;

                    case XmlPullParser.END_TAG: // 종료 태그라면
                        tag = xpp.getName(); //테그 이름 얻어오기

                        if (tag.equals("item")) { // 하나의 버스 정보가 끝이 났으면
                            if(VehicleNo.equals(vehicleno)){
                                if(stationord==nodeord-1) {
                                    showNoti();
                                    alarm_increase();
                                    refresh.cancel();
                                }
                            }
                        }
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }
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

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //알림창 제목
        builder.setContentTitle("기사님 요기요");

        //알림창 메시지
        builder.setContentText("곧 " + nodenm + " 정류장에 도착합니다.");

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

            if (result != null){ // 결과가 있다면
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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
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


package com.ninefives.driverhere.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ninefives.driverhere.DEV.DEV_AlarmControl;
import com.ninefives.driverhere.R;
import com.ninefives.driverhere.Ride_Help.RideInHelp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class AlarmResult extends Activity {

    TextView select_bus;
    TextView select_station;
    TextView con_stop;

    String mJsonString;

    String busno; // 버스 번호
    String busid; // 노선 ID
    String stationno; // 정류소 번호
    String stationnm; // 정류소 이름
    String stationid; // 정류소 ID

    String routeid; // 경로 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmresult);

        select_bus = (TextView) findViewById(R.id.select_bus_num);
        select_station = (TextView) findViewById(R.id.select_station_nm);


        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장
        stationid = intent.getStringExtra("NodeID"); // 인탠트로 받아온 정류소 ID 저장
        stationno = intent.getStringExtra("NodeNo"); // 인탠트로 받아온 정류소 번호 저장
        stationnm = intent.getStringExtra("NodeNm"); // 인탠트로 받아온 정류소 이름 저장

        routeid = stationno+busid;

        select_bus.setText(busno);
        select_station.setText(stationnm);

        alarm_increase();
    }

    public void DEV_alarmcontrol(View view){
        Intent intent = new Intent(getApplicationContext(), DEV_AlarmControl.class); // 인탠트 선언

        intent.putExtra("routeID",routeid);

        startActivity(intent);
    }

    public void ride_in_help(View view){
        Intent intent = new Intent(getApplicationContext(), RideInHelp.class); // 인탠트 선언

        intent.putExtra("BusID",busid);
        intent.putExtra("BusNo",busno);
        intent.putExtra("StationID",stationid);

        Toast toast = Toast.makeText(this.getApplicationContext(),"버스가 진입하면 알림이 옵니다.", Toast.LENGTH_SHORT);
        toast.show();

        startActivity(intent);
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

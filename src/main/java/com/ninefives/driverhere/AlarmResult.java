package com.ninefives.driverhere;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ninefives.driverhere.bus_search.search_result.BusRouteResult;
import com.ninefives.driverhere.station_search.station_pass_bus.StationPassBus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    String routeid; // 경로 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmresult);

        select_bus = (TextView) findViewById(R.id.select_bus_num);
        select_station = (TextView) findViewById(R.id.select_station_nm);
        con_stop = (TextView) findViewById(R.id.con_stop);

        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장
        stationno = intent.getStringExtra("NodeNo"); // 인탠트로 받아온 정류소 ID 저장
        stationnm = intent.getStringExtra("NodeNm"); // 인탠트로 받아온 정류소 이름 저장

        routeid = stationno+busid;

        select_bus.setText(busno);
        select_station.setText(stationnm);

        search();
    }

    public void DEV_alarmcontrol(View view){
        Intent intent = new Intent(getApplicationContext(), DEV_AlarmControl.class); // 인탠트 선언
        intent.putExtra("routeID",routeid);
    }

    public void search() { // 데이터베이스 검색
        GetData task = new GetData(); // 데이터를 가져옴
        task.execute(routeid);
    }


    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AlarmResult.this,
                    "Please Wait", null, true, true); // 불러오는 동안 나올 팝업
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result != null){ // 결과가 있다면
                mJsonString = result;
                showResult(); // 결과를 출력
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = "http://35.185.229.27/alarmsend.php"; // 접속할 웹서버 주소
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


    private void showResult(){

        String TAG_JSON="route_info";
        String TAG_STOP = "stop";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON); // 태그에 해당하는 전체 요소 가져오기

            for(int i=0; i<jsonArray.length(); i++){ // 가져온 세부요소 만큼 반복

                JSONObject item = jsonArray.getJSONObject(i);

                String stop = item.getString(TAG_STOP); // 정류장에 대기 중인 인원 파악

                con_stop.setText(stop);
            }


        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}

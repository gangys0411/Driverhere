package com.ninefives.driverhere.station_search.station_pass_bus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.ninefives.driverhere.bus_search.search_result.BusRouteResult;
import com.ninefives.driverhere.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class StationPassBus extends AppCompatActivity {

    private static String TAG = "phpquerytest";

    PassListViewAdapter adapter = new PassListViewAdapter(); // 어뎁터 생성

    TextView mTextView;
    String mJsonString;
    String stationid;
    String stationnm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationpassbus);

        mTextView = (TextView)findViewById(R.id.stationnm);

        ListView listview; // 리스트 뷰 변수 선언

        listview=(ListView) findViewById(R.id.pass_listview); // 리스트 뷰 연결
        listview.setAdapter(adapter); // 어뎁터 연결
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){ // 리스트 뷰 클릭 이벤트

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){ // 클릭 이벤트 함수
                Intent send_intent = new Intent(getApplicationContext(), BusRouteResult.class); // 인탠트 선언
                send_intent = adapter.sendIntent(position, send_intent); // 리스트 뷰 사용을 위한 함수

                startActivity(send_intent); // 다음 액티비티에 인탠트 전달
            }
        });

        Intent get_intent = getIntent();

        stationid = get_intent.getStringExtra("NodeID"); // 인탠트로 받아온 정류소 ID 저장
        stationnm = get_intent.getStringExtra("NodeNm"); // 인탠트로 받아온 정류소 이름 저장

        mTextView.setText(stationnm);

        search();

        adapter.busArrive(stationid);
    }

    public void search() { // 데이터베이스 검색

        adapter.clearItems();
        adapter.notifyDataSetChanged(); // 리스트 뷰를 초기화 하고 적용

        GetData task = new GetData(); // 데이터를 가져옴
        task.execute(stationid);
    }


    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(StationPassBus.this,
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

            String serverURL = "http://35.185.229.27/station_pass.php"; // 접속할 웹서버 주소
            String postParameters = "stationID=" + stationid; // 검색할 내용


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
        String TAG_BUSID = "busid";
        String TAG_BUSNO = "busno";
        String TAG_START_STATION = "start_station";
        String TAG_END_STATION = "end_station";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON); // 태그에 해당하는 전체 요소 가져오기

            for(int i=0;i<jsonArray.length();i++){ // 가져온 세부요소 만큼 반복

                JSONObject item = jsonArray.getJSONObject(i);

                String busid = item.getString(TAG_BUSID); // 각각의 정보를 저장
                String busno = item.getString(TAG_BUSNO);
                String start_station  = item.getString(TAG_START_STATION);
                String end_station  = item.getString(TAG_END_STATION);

                adapter.addItem(busno, busid, start_station, end_station);
            }

            adapter.notifyDataSetChanged(); //리스트 뷰 갱신
        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}
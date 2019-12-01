package com.ninefives.driverhere;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AlarmSend extends AppCompatActivity {

    private static String IP_ADDRESS = "35.185.229.27";
    private static String TAG = "phptest";

    private EditText mEditTextStationID;
    private TextView mTextViewResult;
    private TextView mTextViewReceive;
    private ArrayList<PersonalData> mArrayList;
    private UsersAdapter mAdapter;
    private ListView mListView;
    private EditText mEditTextSearchKeyword;
    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmsend);

        mEditTextStationID = (EditText)findViewById(R.id.editText_main_name);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        mTextViewReceive = (TextView)findViewById(R.id.textView_receive_result);
        mListView = (ListView)findViewById(R.id.listView_main_list);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        mArrayList = new ArrayList<>();

        mAdapter = new UsersAdapter(this, mArrayList);
        mListView.setAdapter(mAdapter);

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stationID = mEditTextStationID.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/station_pass.php", stationID);

                mEditTextStationID.setText("");

            }
        });

        Button button_all = (Button)findViewById(R.id.button_main_all);
        button_all.setOnClickListener(new View.OnClickListener() { // 버튼 클릭시
            public void onClick(View v) {

                mArrayList.clear(); // 기존 리스트를 삭제하고
                mAdapter.notifyDataSetChanged(); // 데이터가 변경되었음을 어뎁터에 알림

                GetData task = new GetData();
                task.execute("http://" + IP_ADDRESS + "/getjson.php", ""); // 그리고 해당 주소로부터 데이터를 받아옴
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = progressDialog.show(AlarmSend.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String stationID = (String)params[1];
            String serverURL = (String)params[0];

            // PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터 준비
            // POST 방식으로 데이터 전달시 데이터가 주소에 직접 입력되지 않음

            String postParameters = "stationid=" + stationID;

            // HTTP 메세지 본문에 포함되어 전송되므로 따로 데이터를 준비
            // 전송할 데이터는 "이름=값" 형식이며 항목 사이에 &를 추가
            // 여기서 사용한 이름을 PHP에서 사용해 값을 얻음

            try {

                // HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터 전송

                URL url = new URL(serverURL); // 주소 지정
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setReadTimeout(5000); // 5초안에 응답이 오지 않으면 예외처리
                httpURLConnection.setConnectTimeout(5000); // 5초안에 연결이 되지 않으면 예외처리
                httpURLConnection.setRequestMethod("POST"); // POST 방식으로 요청
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                // 전송할 데이터가 저장된 변수를 입력. 인코딩 주의
                outputStream.flush();
                outputStream.close();

                // 응답을 읽음

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) { // 정상적인 응답 데이터일 경우
                    inputStream = httpURLConnection.getInputStream();
                }
                else{ // 에러가 발생 했을 경우
                    inputStream = httpURLConnection.getErrorStream();
                }

                // StringBuilder를 사용해 수신되는 데이터 저장

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString(); // 저장된 데이터를 문자열로 변환해 반환

            } catch (Exception e) {
                Log.d(TAG, "InsertData : Error ", e);

                return new String("Error : " + e.getMessage());
            }
        }
    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AlarmSend.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewReceive.setText(result);
            Log.d(TAG, "response - " + result);

            if(result == null){

                mTextViewReceive.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];

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

                while((line = bufferedReader.readLine()) !=null){
                    sb.append(line);
                }

                bufferedReader.close();

                return  sb.toString().trim();

            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ",e);
                errorString = e.toString();

                return null;
            }
        }
    }


    private void showResult(){

        String TAG_JSON="데이터출력";
        String TAG_ID="id";
        String TAG_NAME="name";
        String TAG_COUNTRY="country";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON); // 해당 태그를 가지는 대괄호의 객체들을 받아옴

            for(int i=0; i<jsonArray.length(); i++){ // 객체 갯수 만큼 반복하여

                JSONObject item = jsonArray.getJSONObject(i); // 하나씩 저장

                String id = item.getString(TAG_ID); // 해당 태그를 가지는 정보들을 저장
                String name = item.getString(TAG_NAME);
                String country = item.getString(TAG_COUNTRY);

                PersonalData personalData = new PersonalData(); // 아이템을 생성하고

                personalData.setMember_id(id); // 각 값을 저장 후
                personalData.setMember_name(name);
                personalData.setMember_country(country);

                mArrayList.add(personalData); // 리스트에 추가
                mAdapter.notifyDataSetChanged(); // 값이 변경되었음을 알림
            }

        }catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}

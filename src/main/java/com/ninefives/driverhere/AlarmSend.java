package com.ninefives.driverhere;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlarmSend extends AppCompatActivity {

    private static String IP_ADDRESS = "35.185.229.27";
    private static String TAG = "phptest";

    private EditText mEditTextName;
    private EditText mEditTextCountry;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmsend);

        mEditTextName = (EditText)findViewById(R.id.editText_main_name);
        mEditTextCountry = (EditText)findViewById(R.id.editText_main_country);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mEditTextName.getText().toString();
                String country = mEditTextCountry.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert.php", name, country);

                mEditTextName.setText("");
                mEditTextCountry.setText("");

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

            String name = (String)params[1];
            String country = (String)params[2];
            String serverURL = (String)params[0];

            // PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터 준비
            // POST 방식으로 데이터 전달시 데이터가 주소에 직접 입력되지 않음

            String postParameters = "name=" + name + "&country=" + country;

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
}

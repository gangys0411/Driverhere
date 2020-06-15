package com.ninefives.driverhere;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Setting extends AppCompatActivity {

    ArrayList<String> spinnerlist = new ArrayList<String>();
    TextView textView;
    TextView textshow;

    String key = "hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D&";

    String cityname;
    String citycode;
    String citynm;
    String citycd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        textView = (TextView) findViewById(R.id.textView);
        textshow = (TextView) findViewById(R.id.textshow);

        spinnerlist.add("세종특별시");
        spinnerlist.add("대구광역시");
        spinnerlist.add("인천광역시");
        spinnerlist.add("광주광역시");
        spinnerlist.add("대전광역시");
        spinnerlist.add("울산광역시");
        spinnerlist.add("제주도");
        spinnerlist.add("춘천시");
        spinnerlist.add("원주시");
        spinnerlist.add("강릉시");
        spinnerlist.add("동해시");
        spinnerlist.add("태백시");
        spinnerlist.add("속초시");
        spinnerlist.add("삼척시");
        spinnerlist.add("홍천군");
        spinnerlist.add("영월군");
        spinnerlist.add("평창군");
        spinnerlist.add("정선군");
        spinnerlist.add("철원군");
        spinnerlist.add("화천군");
        spinnerlist.add("양구군");
        spinnerlist.add("인제군");
        spinnerlist.add("고성군");
        spinnerlist.add("양양군");
        spinnerlist.add("청주시");
        spinnerlist.add("천안시");
        spinnerlist.add("공주시");
        spinnerlist.add("아산시");
        spinnerlist.add("서산시");
        spinnerlist.add("논산시");
        spinnerlist.add("부여군");
        spinnerlist.add("전주시");
        spinnerlist.add("군산시");
        spinnerlist.add("익산시");
        spinnerlist.add("정읍시");
        spinnerlist.add("진안군");
        spinnerlist.add("무주군");
        spinnerlist.add("장수군");
        spinnerlist.add("임실군");
        spinnerlist.add("순창군");
        spinnerlist.add("목포시");
        spinnerlist.add("여수시");
        spinnerlist.add("순천시");
        spinnerlist.add("광양시");
        spinnerlist.add("강진군");
        spinnerlist.add("영암군");
        spinnerlist.add("무안군");
        spinnerlist.add("함평군");
        spinnerlist.add("영광군");
        spinnerlist.add("신안군");
        spinnerlist.add("포항시");
        spinnerlist.add("경주시");
        spinnerlist.add("김천시");
        spinnerlist.add("구미시");
        spinnerlist.add("영주시");
        spinnerlist.add("경산시");
        spinnerlist.add("칠곡군");
        spinnerlist.add("창원시");
        spinnerlist.add("진주시");
        spinnerlist.add("통영시");
        spinnerlist.add("김해시");
        spinnerlist.add("밀양시");
        spinnerlist.add("거제시");
        spinnerlist.add("양산시");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerlist);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                citynm = spinnerlist.get(position);
                textView.setText(citynm);
                search();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void search() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 ListView 글씨 변경하도록 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textshow.setText(citycd);
                    }
                });
            }
        }).start();
    }

    void getXmlData(){

        String queryUrl = "http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getCtyCodeList?" +
                "serviceKey=" + key;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("citycode")) {
                            xpp.next();
                            citycode = xpp.getText();
                        } else if (tag.equals("cityname")) {
                            xpp.next();
                            cityname = xpp.getText();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) {
                            if (citynm.equals(cityname)) {
                                citycd = citycode;
                            }

                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



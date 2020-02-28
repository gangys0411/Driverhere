package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class List extends Activity {
    ListViewAdapter adapter = new ListViewAdapter(); // 어뎁터 생성

    TextView result;

    String busno="200"; // 버스 번호
    String routeid = "CAB285000104"; // 노선 ID
    String nodeid; // 정류소 ID
    String nodenm; // 정류소 이름

    String test;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        result=(TextView) findViewById(R.id.result);

        ListView listview; // 리스트 뷰 변수 선언

        listview=(ListView) findViewById(R.id.list_listview); // 리스트 뷰 연결
        listview.setAdapter(adapter); // 어뎁터 연결

        result.setText(busno); // 버스 번호 출력

        search();
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
                        adapter.notifyDataSetChanged(); //리스트 뷰 갱신
                    }
                });
            }
        }).start();
    }

    void getXmlData(){
        adapter.clearItems(); // 리스트 뷰 초기화

        try {
            for(int i=0; i<10; i++)
            {
                adapter.addItem(i+1,i);
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }

        adapter.listadd();
    }
}

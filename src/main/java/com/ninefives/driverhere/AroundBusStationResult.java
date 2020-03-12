package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ninefives.driverhere.bus_search.route_search.SearchListViewAdapter;
import com.ninefives.driverhere.bus_search.search_result.BusRouteResult;
import com.ninefives.driverhere.station_search.station_pass_bus.StationPassBus;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class AroundBusStationResult extends Activity {

    AroundListViewAdapter adapter = new AroundListViewAdapter(); // 어뎁터 생성

    EditText edit; // 에딧 텍스트 뷰 변수

    String key="hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    String gpsLati; // gps 위도
    String gpsLong; // gps 경도

    // 리스트 뷰 사용을 위한 변수
    String nodeid; // 정류소 ID
    String nodenm; // 정류소 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aroundbusstationresult);

        ListView listview; // 리스트 뷰 변수 선언

        listview=(ListView) findViewById(R.id.around_listview); // 리스트 뷰 연결
        listview.setAdapter(adapter); // 어뎁터 연결
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){ // 리스트 뷰 클릭 이벤트

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){ // 클릭 이벤트 함수
                Intent intent = new Intent(getApplicationContext(), StationPassBus.class); // 인탠트 선언
                intent = adapter.sendIntent(position, intent); // 리스트 뷰 사용을 위한 함수

                startActivity(intent); // 다음 액티비티에 인탠트 전달
            }
        });

        AroundSearch();
    }

    //Button을 클릭시
    public void AroundSearch(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged(); //리스트 뷰 갱신
                    }
                });
            }
        }).start();
    }


    //XmlPullParser를 이용하여 OpenAPI XML 파일 파싱하기(parsing)
    void getXmlData(){
        adapter.clearItems(); // 리스트 뷰 초기화

        String queryUrl="http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getCrdntPrxmtSttnList?" + // 요청 URL
                "serviceKey="+ key+ // 서비스 키 추가
                "&gpsLati="+ gpsLati+ // 위도 추가
                "&gpsLong="+gpsLong; // 경도 추가

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance(); // 파서 선언
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag; // 태그 변수 선언

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){ // 문서의 끝을 만날때 까지 반복
                switch( eventType ){

                    case XmlPullParser.START_TAG: // 시작 태그 별로 행동
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ;// 하나의 검색결과
                        else if(tag.equals("citycode")){ // 도시코드
                            xpp.next();
                        }
                        else if(tag.equals("gpslati")){ // 위도
                            xpp.next();
                        }
                        else if(tag.equals("gpslong")){ // 경도
                            xpp.next();
                        }
                        else if(tag.equals("nodeid")){ // 정류소 ID
                            xpp.next();
                            nodeid=xpp.getText();
                        }
                        else if(tag.equals("nodenm")){ // 정류소 이름
                            xpp.next();
                            nodenm=xpp.getText();
                        }
                        else if(tag.equals("nodeno")){ // 정류소 번호
                            xpp.next();

                        }
                        break;

                    case XmlPullParser.TEXT: // 텍스트라면 스킵
                        break;

                    case XmlPullParser.END_TAG: // 종료 태그라면
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")){ // 하나의 버스 정보가 끝이 났으면
                            adapter.addItem(nodenm, nodeid); // 리스트뷰에 버스 정보 추가
                        }
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }
    }
}
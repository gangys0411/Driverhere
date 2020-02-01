package com.ninefives.driverhere.station_search.station_search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.ninefives.driverhere.station_search.station_pass_bus.StationPassBus;
import com.ninefives.driverhere.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class BusStationSearch extends Activity {

    StationListViewAdapter adapter = new StationListViewAdapter(); // 어뎁터 생성

    EditText edit; // 에딧 텍스트 뷰 변수

    XmlPullParser xpp;
    String key="hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    String cityCode="34010"; // 천안 도시 코드
    String nodeNm; // 입력받는 정류소 이름

    // 리스트 뷰 사용을 위한 변수
    String nodenm; // 정류소 이름
    String nodeid; // 정류소 id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busstationsearch);

        edit= (EditText)findViewById(R.id.station_edit); // 에딧 텍스트 뷰 연결

        ListView listview; // 리스트 뷰 변수 선언

        listview=(ListView) findViewById(R.id.search_listview); // 리스트 뷰 연결
        listview.setAdapter(adapter); // 어뎁터 연결
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){ // 리스트 뷰 클릭 이벤트

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){ // 클릭 이벤트 함수
                Intent intent = new Intent(getApplicationContext(), StationPassBus.class); // 인탠트 선언
                intent = adapter.sendIntent(position, intent); // 리스트 뷰 사용을 위한 함수

                startActivity(intent); // 다음 액티비티에 인탠트 전달
            }
        });
    }

    //Button을 클릭시
    public void StationSearch(View v){
        switch( v.getId() ){
            case R.id.search_button:

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
                break;
        }
    }


    //XmlPullParser를 이용하여 OpenAPI XML 파일 파싱하기(parsing)
    void getXmlData(){
        adapter.clearItems(); // 리스트 뷰 초기화

        try {
            nodeNm = URLEncoder.encode(edit.getText().toString(), "UTF-8"); // api에 한글을 그냥 입력시 인식하지 못하므로 UTF-8 형식으로 인코딩
        } catch (UnsupportedEncodingException e) { // 예외 처리
            e.printStackTrace();
        }

        String queryUrl= "http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getSttnNoList?" + // 요청 URL
                "serviceKey="+ key + // 서비스 키 추가
                "&cityCode="+ cityCode + // 도시 코드 추가
                "&nodeNm="+ nodeNm + // 정류소 이름
                "&numOfRows="+ "50" + // 한 페이지 결과값 수
                "&pageNo="+ "1"; // 출력할 페이지 번호

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
                        else if(tag.equals("gpslati")){ // 정류소 위도
                            xpp.next();
                        }
                        else if(tag.equals("gpslong")){ // 정류소 경도
                            xpp.next();
                        }
                        else if(tag.equals("nodeid")){ // 정류소 id
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

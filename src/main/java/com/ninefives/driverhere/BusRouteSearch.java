package com.ninefives.driverhere;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class BusRouteSearch extends Activity {

    ListViewAdapter adapter = new ListViewAdapter(); // 어뎁터 생성

    EditText edit; // 에딧 텍스트 뷰 변수

    XmlPullParser xpp;
    String key="hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    int cityCode=34010; // 천안 도시 코드
    String routeNo; // 버스 노선 번호

    // 리스트 뷰 사용을 위한 변수
    String busno; // 버스 번호
    String routeid; // 노선 id
    String startnm; // 기점
    String endnm; // 종점

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busroutesearch);

        edit= (EditText)findViewById(R.id.edit); // 에딧 텍스트 뷰 연결

        ListView listview; // 리스트 뷰 변수 선언

        listview=(ListView) findViewById(R.id.listview); // 리스트 뷰 연결
        listview.setAdapter(adapter); // 어뎁터 연결
    }

    //Button을 클릭시
    public void BusSearch(View v){
        switch( v.getId() ){
            case R.id.button:

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

        routeNo= edit.getText().toString();//EditText에 작성된 노선 번호 얻어오기

        String queryUrl="http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteNoList?serviceKey="//요청 URL
                + key+ // 서비스 키 추가
                "&cityCode="+ cityCode+ // 도시 코드 추가
                "&routeNo="+ routeNo; // 노선 번호 추가

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
                        else if(tag.equals("endnodenm")){ // 종점
                            xpp.next();
                            endnm = xpp.getText();
                        }
                        else if(tag.equals("endvehicletime")){ // 막차 시간
                            xpp.next();
                        }
                        else if(tag.equals("routeid")){ // 노선 id
                            xpp.next();
                            routeid=xpp.getText();
                        }
                        else if(tag.equals("routeno")){ // 노선 번호
                            xpp.next();
                            busno=xpp.getText();
                        }
                        else if(tag.equals("routetp")){ // 노선 타입
                            xpp.next();
                        }
                        else if(tag.equals("startnodenm")){ // 기점
                            xpp.next();
                            startnm = xpp.getText();
                        }
                        else if(tag.equals("startvehicletime")) { // 첫차 시간
                            xpp.next();
                        }
                        break;

                    case XmlPullParser.TEXT: // 텍스트라면 스킵
                        break;

                    case XmlPullParser.END_TAG: // 종료 태그라면
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")){ // 하나의 버스 정보가 끝이 났으면
                            adapter.addItem(busno, routeid, startnm, endnm); // 리스트뷰에 버스 정보 추가
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
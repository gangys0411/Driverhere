package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class NextStopSearch extends Activity {

    EditText edit; // 버스번호 텍스트 뷰 변수
    EditText edit2; // 기점 텍스트 뷰 변수

    TextView textview;

    XmlPullParser xpp;
    String key="hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    int cityCode=34010; // 천안 도시 코드
    String routeNo; // 버스 노선 번호
    String startStop;

    String busno; // 버스 번호
    String routeid; // 노선 id
    String startnm; // 기점
    String endnm; // 종점

    String Bus_NO; // 조건에 맞는 버스번호
    String Route_Id; // 조건에 맞는 버스 id
    String Start_Nm; // 조건에 맞는 기점
    String End_Nm; // 조건에 맞는 종점

    String text="파싱결과 : \n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextstopsearch);

        Intent intent = new Intent(getApplicationContext(),NextStopResult.class);
        intent.putExtra("routeId", Route_Id);
        intent.putExtra("busNo", Bus_NO);
        intent.putExtra("startNm", Start_Nm);
        intent.putExtra("endNm", End_Nm);


        edit= (EditText)findViewById(R.id.edit); // 버스번호 텍스트 뷰 연결

        edit2=(EditText)findViewById(R.id.edit2); // 기점 텍스트 뷰 연결

        textview=(TextView) findViewById(R.id.searchtextview);

        textview.setText(text);

    }

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
                                textview.setText(startStop + "\n" + text);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    void getXmlData(){

        routeNo= edit.getText().toString();//EditText에 작성된 노선 번호 얻어오기
        startStop= edit2.getText().toString(); //edit2에 작성된 기점 얻어오기

        String queryUrl="http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteNoList?" + // 요청 URL
                "serviceKey="+ key+ // 서비스 키 추가
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

                        if(tag.equals("item")) { // 하나의 버스 정보가 끝이 났으면
                            if (startnm.equals(startStop)) {
                                text = text + "버스번호: " + busno + "\n" + "기점 :" + startnm + "\n" + "종점 :" + endnm + "\n";
                                Bus_NO = busno;
                                Start_Nm = startnm;
                                Route_Id = routeid;
                                End_Nm = endnm;
                            }
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

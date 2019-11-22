package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class NextStopResult extends Activity {

    TextView result;

    String key="hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    int cityCode=34010; // 천안 도시 코드

    // 리스트 뷰 사용을 위한 변수

    String busno; // 버스 번호
    String routeid; // 노선 ID
    String nodeid; // 정류소 ID
    String nodenm; // 정류소 이름
    String startnm; // 기점 이름
    String endnm; // 종점 이름

    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        routeid = intent.getStringExtra("routeId");
        busno = intent.getStringExtra("busNo");
        startnm = intent.getStringExtra("startNm");
        endnm = intent.getStringExtra("endNm");
    }

    void getXmlData(){

        String queryUrl="http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteAcctoThrghSttnList?" + // 요청 URL
                "serviceKey=" + key+ // 서비스 키 추가
                "&numOfRows=" + "100" + // 한 페이지에 출력될 결과수
                "&pageNo="+ "1" + // 출력할 페이지 번호
                "&cityCode="+ cityCode+ // 도시 코드 추가
                "&routeId="+ routeid;// 노선 ID 추가

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
                        busno=xpp.getText();
                    }
                    else if(tag.equals("nodeord")){ // 정류소 순서
                        xpp.next();
                    }
                    else if(tag.equals("routeid")) { // 노선 ID
                        xpp.next();
                    }
                    break;

                case XmlPullParser.TEXT: // 텍스트라면 스킵
                    break;

                case XmlPullParser.END_TAG: // 종료 태그라면
                    tag= xpp.getName(); //테그 이름 얻어오기

                    if(tag.equals("item")){ // 하나의 버스 정보가 끝이 났으면
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

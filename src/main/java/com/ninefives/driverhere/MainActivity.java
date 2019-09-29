package com.ninefives.driverhere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.os.StrictMode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.enableDefaults();

        TextView status1 = (TextView)findViewById(R.id.result); // 파싱된 결과 확인

        boolean b_item = false;
        boolean b_endnodenm = false;
        boolean b_endvehicletime = false;
        boolean b_routeid = false;
        boolean b_routeno = false;
        boolean b_routetp = false;
        boolean b_startnodenm = false;
        boolean b_startvehicletime = false;

        String endnodenm = null;
        String endvehicletime = null;
        String routeid = null;
        String routeno = null;
        String routetp = null;
        String startnodenm = null;
        String startvehicletime = null;

        try{
            URL url = new URL("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteNoList?serviceKey=hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D&cityCode=34010&routeNo=200");

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱 시작");

            while(parserEvent != XmlPullParser.END_DOCUMENT){
              switch (parserEvent){
                  case XmlPullParser.START_TAG: // 시작 태그를 만나면 실행
                      if(parser.getName().equals("endnodenm")){ // endnodenm 태그를 만나면 내용 저장
                          b_endnodenm = true;
                      }
                      if(parser.getName().equals("endvehicletime")){ // endvehicletime 태그를 만나면 내용 저장
                          b_endvehicletime = true;
                      }
                      if(parser.getName().equals("routeid")){ // routeid 태그를 만나면 내용 저장
                          b_routeid = true;
                      }
                      if(parser.getName().equals("routeno")){ // routeno 태그를 만나면 내용 저장
                          b_routeno = true;
                      }
                      if(parser.getName().equals("routetp")){ // routetp 태그를 만나면 내용 저장
                          b_routetp = true;
                      }
                      if(parser.getName().equals("startnodenm")){ // startnodenm 태그를 만나면 내용 저장
                          b_startnodenm = true;
                      }
                      if(parser.getName().equals("startvehicletime")){ // startvehicletime 태그를 만나면 내용 저장
                          b_startvehicletime = true;
                      }
                      break;
                  case XmlPullParser.TEXT: // 내용에 접근했을때
                      if(b_endnodenm){ // b_endnodenm이 true 일때 내용 저장
                          endnodenm = parser.getText();
                          b_endnodenm = false;
                      }
                      if(b_endvehicletime){ // b_endvehicletime이 true 일때 내용 저장
                          endvehicletime = parser.getText();
                          b_endvehicletime = false;
                      }
                      if(b_routeid){ // b_routeid이 true 일때 내용 저장
                          routeid = parser.getText();
                          b_routeid = false;
                      }
                      if(b_routeno){ // b_routeno이 true 일때 내용 저장
                          routeno = parser.getText();
                          b_routeno = false;
                      }
                      if(b_routetp){ // b_routetp이 true 일때 내용 저장
                          routetp = parser.getText();
                          b_routetp = false;
                      }
                      if(b_startnodenm){ // b_startnodenm이 true 일때 내용 저장
                          startnodenm = parser.getText();
                          b_startnodenm = false;
                      }
                      if(b_startvehicletime){ // b_startvehicletime이 true 일때 내용 저장
                          startvehicletime = parser.getText();
                          b_startvehicletime = false;
                      }
                      break;
                  case XmlPullParser.END_TAG:
                      if(parser.getName().equals("item")){ // 끝 태그가 item이면 저장된 값들을 출력
                          status1.setText(status1.getText()+"종점 : "+ endnodenm +"\n 막차 시간 : "+ endvehicletime +"\n 노선 ID : "+ routeid +"\n 버스 번호 : "+ routeno +"\n 버스 타입 : "+ routetp +"\n 기점 : "+ startnodenm +"\n 첫차 시간 : "+ startvehicletime +"\n");
                          b_item = false;
                      }
                      break;
              }
              parserEvent = parser.next();
            }
        } catch(Exception e){
            status1.setText("에러 발생");
        }
    }
}
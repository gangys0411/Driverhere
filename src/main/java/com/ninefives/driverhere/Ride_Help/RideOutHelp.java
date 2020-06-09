package com.ninefives.driverhere.Ride_Help;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.ninefives.driverhere.Favorite.TinyDB;
import com.ninefives.driverhere.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class RideOutHelp extends AppCompatActivity {

    NotificationManager manager;
    NotificationCompat.Builder builder;

    TextView come_bus;

    String key = "hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    String citycode = "34010"; // 천안 도시 코드

    String busid; // 노선 ID
    String busno; // 버스 번호

    String VehicleNo; // 인탠트 차량 번호

    String stationid; // 검사할 정류소 ID
    String stationnm; // 검사할 정류소 이름

    String nodeid;
    String nodenm;
    String nodeno;
    int nodeord;

    String select_nodeid; // 선택된 정류소 ID
    String select_nodeno; // 선택된 정류소 번호
    String select_nodenm; // 선택된 정류소 이름

    String vehicleno; // 차량 번호

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    HelpListViewAdapter adapter = new HelpListViewAdapter(); // 어뎁터 생성

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_rideouthelp);

        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 노선 ID 저장
        VehicleNo = intent.getStringExtra("VehicleNo"); // 인탠트로 받아온 차량 번호 저장

        come_bus = findViewById(R.id.come_bus);
        come_bus.setText(busno + "번 버스가 진입 중입니다.");

        ListView listview; // 리스트 뷰 변수 선언

        listview=(ListView) findViewById(R.id.result_listview); // 리스트 뷰 연결
        listview.setAdapter(adapter); // 어뎁터 연결
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){ // 리스트 뷰 클릭 이벤트

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){ // 클릭 이벤트 함수
                adapter.change_select(position);
                select_nodeid = adapter.selectnodeid(position); // 선택된 위치의 정류소 ID 저장
                select_nodeno = adapter.selectnodeno(position); // 선택된 위치의 정류소 번호 저장
                select_nodenm = adapter.selectnodenm(position); //  선택된 위치의 정류소 이름 저장
            }
        });

        search();
    }

    public void search() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                GetXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

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

    void GetXmlData(){
        adapter.clearItems(); // 리스트 뷰 초기화

        String queryUrl="http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteAcctoThrghSttnList?" + // 요청 URL
                "serviceKey=" + key+ // 서비스 키 추가
                "&numOfRows=" + "100" + // 한 페이지에 출력될 결과수
                "&pageNo="+ "1" + // 출력할 페이지 번호
                "&cityCode="+ citycode+ // 도시 코드 추가
                "&routeId="+ busid;// 노선 ID 추가

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
                            nodeno=xpp.getText();
                        }
                        else if(tag.equals("nodeord")){ // 정류소 순서
                            xpp.next();
                            nodeord=Integer.parseInt(xpp.getText());
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
                            adapter.addItem(nodenm, nodeid, nodeord, nodeno); // 리스트뷰에 정류소 정보 추가
                        }
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }
    }

    public void ride_out_help(View view){
        Intent intent = new Intent(getApplicationContext(), RideOutHelpAlarm.class); // 인탠트 선언

        intent.putExtra("BusID",busid);
        intent.putExtra("BusNo",busno);
        intent.putExtra("VehicleNo",VehicleNo);
        intent.putExtra("StationID",select_nodeid);

        Toast toast = Toast.makeText(this.getApplicationContext(),"내릴 정류장에 접근하면 알림이 옵니다.", Toast.LENGTH_SHORT);
        toast.show();

        startActivity(intent);
    }
}


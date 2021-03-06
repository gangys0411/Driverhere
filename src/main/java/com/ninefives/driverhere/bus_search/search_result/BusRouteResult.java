package com.ninefives.driverhere.bus_search.search_result;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.ninefives.driverhere.Alarm.AlarmResult;
import com.ninefives.driverhere.Alarm.AlarmSelect;
import com.ninefives.driverhere.GpsTracker;
import com.ninefives.driverhere.R;
import com.ninefives.driverhere.Map.RouteMap;
import com.ninefives.driverhere.Favorite.TinyDB;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class BusRouteResult extends Activity {

    ResultListViewAdapter adapter = new ResultListViewAdapter(); // 어뎁터 생성

    TextView result;

    String key = "hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    String citycode = "34010"; // 천안 도시 코드

    // 리스트 뷰 사용을 위한 변수

    String busno; // 버스 번호
    String routeid; // 노선 ID
    String busdirect; // 버스 방향

    String nodeid; // 정류소 ID
    String nodenm; // 정류소 이름
    int nodeord; // 정류소 순서 번호
    String nodeno; // 정류소 번호

    String select_nodeid; // 선택된 정류소 ID
    String select_nodeno; // 선택된 정류소 번호
    String select_nodenm; // 선택된 정류소 이름

    Button favorite_button;

    TinyDB tinydb;
    ArrayList<String> tiny_busid = new ArrayList<String>();
    ArrayList<String> tiny_busno = new ArrayList<String>();
    ArrayList<String> tiny_busdirect = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busrouteresult);

        tinydb = new TinyDB(getBaseContext());

        result=(TextView) findViewById(R.id.result);

        tiny_busid = tinydb.getListString("busid"); // busid란 키를 가진 문자열 리스트를 불러옴
        tiny_busno = tinydb.getListString("busno"); // busno란 키를 가진 문자열 리스트를 불러옴
        tiny_busdirect = tinydb.getListString("busdirect"); // busdirect란 키를 가진 문자열 리스트를 불러옴

        ListView listview; // 리스트 뷰 변수 선언

        listview=(ListView) findViewById(R.id.result_listview); // 리스트 뷰 연결
        listview.setAdapter(adapter); // 어뎁터 연결
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){ // 리스트 뷰 클릭 이벤트

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){ // 클릭 이벤트 함수
                adapter.change_select(position);
                select_nodeid = adapter.selectnodeid(position); // 선택된 위치의 정류소 ID 저장
                select_nodeno = adapter.selectnodeno(position); // 선택된 위치의 정류소 번호  저장
                select_nodenm = adapter.selectnodenm(position); //  선택된 위치의 정류소 이름 저장
            }
        });

        favorite_button = findViewById(R.id.favorite);

        Intent intent = getIntent();

        routeid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장
        busdirect = intent.getStringExtra("BusDirect"); // 인탠트로 받아온 버스 번호 저장

        result.setText(busno); // 버스 번호 출력

        search();

        adapter.busLocate(routeid); // 버스 도착 정보 갱신
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

        String queryUrl="http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteAcctoThrghSttnList?" + // 요청 URL
                "serviceKey=" + key+ // 서비스 키 추가
                "&numOfRows=" + "100" + // 한 페이지에 출력될 결과수
                "&pageNo="+ "1" + // 출력할 페이지 번호
                "&cityCode="+ citycode+ // 도시 코드 추가
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

    public void routemap(View v){ // 지도 보기
        adapter.stop();

        Intent intent = new Intent(this, RouteMap.class);

        intent.putExtra("BusID", routeid); // 인탠트에 현재 버스 데이터를 전달
        intent.putExtra("BusNo", busno);

        startActivity(intent);
    }

    public void favorite_bus(View v){ // 즐겨찾기

        tiny_busid.add(routeid);
        tiny_busno.add(busno);
        tiny_busdirect.add(adapter.returnDirect());

        tinydb.putListString("busid",tiny_busid); // busid 키를 가진 문자열 리스트 갱신
        tinydb.putListString("busno",tiny_busno); // busno 키를 가진 문자열 리스트 갱신
        tinydb.putListString("busdirect",tiny_busdirect); // busdirect 키를 가진 문자열 리스트 갱신

        Toast toast = Toast.makeText(this.getApplicationContext(),"즐겨찾기에 추가 되었습니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void AlarmSelect(View v){ // 알림 보내기
        adapter.stop();

        Intent intent = new Intent(this, AlarmSelect.class);

        intent.putExtra("BusID", routeid); // 인탠트에 현재 버스 데이터를 전달
        intent.putExtra("BusNo", busno);
        intent.putExtra("NodeID", select_nodeid);
        intent.putExtra("NodeNm", select_nodenm);
        intent.putExtra("NodeNo", select_nodeno);

        startActivity(intent);
    }
}

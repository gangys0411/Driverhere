package com.ninefives.driverhere.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ninefives.driverhere.Favorite.TinyDB;
import com.ninefives.driverhere.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class RouteMap extends AppCompatActivity
        implements OnMapReadyCallback {

    ArrayList<BusMapItem> stationList = new ArrayList<BusMapItem>();
    ArrayList<BusLocateMapItem> buslocate = new ArrayList<BusLocateMapItem>();

    String key = "hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    String citycode = "34010"; // 천안 도시 코드

    String busid;
    String busno;

    String nodeid;
    String nodenm;
    String nodeno;
    String nodeord;

    double nodelati;
    double nodelong;

    double buslati;
    double buslong;

    String vehicleno;

    GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routemap);

        Intent intent = getIntent();

        busid = intent.getStringExtra("BusID"); // 인탠트로 받아온 노선 ID 저장
        busno = intent.getStringExtra("BusNo"); // 인탠트로 받아온 버스 번호 저장

        search();

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void search() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                GetXmlData();

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 ListView 글씨 변경하도록 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();
    }

    void getXmlData(){
        stationList.clear(); // 리스트 초기화

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
                            nodelati=Double.parseDouble(xpp.getText());
                        }
                        else if(tag.equals("gpslong")){ // 정류소 경도
                            xpp.next();
                            nodelong=Double.parseDouble(xpp.getText());
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
                            nodeord=xpp.getText();
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
                            BusMapItem item = new BusMapItem();

                            item.setNodeId(nodeid);
                            item.setNodeNm(nodenm);
                            item.setNodeLati(nodelati);
                            item.setNodeLong(nodelong);

                            stationList.add(item); // 리스트뷰에 정류소 정보 추가
                        }
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }
    }

    public void GetXmlData() {
        buslocate.clear(); // 리스트 초기화

        String queryUrl = "http://openapi.tago.go.kr/openapi/service/BusLcInfoInqireService/getRouteAcctoBusLcList?" + // 요청 URL
                "serviceKey=" + key + // 서비스 키 추가
                "&cityCode=" + citycode + // 도시 코드 추가
                "&routeId=" + busid;// 노선 ID 추가

        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); // 파서 선언
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag; // 태그 변수 선언

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) { // 문서의 끝을 만날때 까지 반복
                Log.d("findpath_parser", String.valueOf(eventType)+" name : "+xpp.getName()+" text : "+xpp.getText());
                switch (eventType) {

                    case XmlPullParser.START_TAG: // 시작 태그 별로 행동
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("item")) ;// 하나의 검색결과
                        else if (tag.equals("gpslati")) { // 차량 위도
                            xpp.next();
                            buslati=Double.parseDouble(xpp.getText());
                        } else if (tag.equals("gpslong")) { // 차량 경도
                            xpp.next();
                            buslati=Double.parseDouble(xpp.getText());
                        } else if (tag.equals("nodeid")) { // 정류소 ID
                            xpp.next();
                        } else if (tag.equals("nodenm")) { // 정류소 이름
                            xpp.next();
                        } else if (tag.equals("nodeord")) { // 정류소 순서
                            xpp.next();
                        } else if (tag.equals("routenm")) { // 노선 번호
                            xpp.next();
                        } else if (tag.equals("routetp")) { // 노선 타입
                            xpp.next();
                        } else if (tag.equals("vehicleno")) { // 차량 번호
                            xpp.next();
                            vehicleno = xpp.getText();
                        }
                        break;

                    case XmlPullParser.TEXT: // 텍스트라면 스킵
                        break;

                    case XmlPullParser.END_TAG: // 종료 태그라면
                        tag = xpp.getName(); //테그 이름 얻어오기

                        if (tag.equals("item")) { // 하나의 버스 정보가 끝이 났으면
                            BusLocateMapItem item2 = new BusLocateMapItem();

                            item2.setBusLati(buslati);
                            item2.setBusLong(buslong);
                            item2.setVehicleNo(vehicleno);

                            buslocate.add(item2);
                        }
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        // 구글 맵 객체를 불러온다.
        mMap = googleMap;

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.station_marker2);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

        BitmapDrawable bitmapdraw2=(BitmapDrawable)getResources().getDrawable(R.drawable.busicon);
        Bitmap b2=bitmapdraw2.getBitmap();
        Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, 50, 50, false);

        for(int i=0; i<stationList.size()-1; i++) {

            // 구글 맵에 표시할 마커에 대한 옵션 설정
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(stationList.get(i).getNodeLati(), stationList.get(i).getNodeLong()))
                    .title(stationList.get(i).getNodeNm())
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            // 마커를 생성한다.
            mMap.addMarker(markerOptions);
        }

        for(int i=0; i<buslocate.size()-1; i++) {

            // 구글 맵에 표시할 마커에 대한 옵션 설정
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(buslocate.get(i).getBusLati(), buslocate.get(i).getBusLong()))
                    .title(buslocate.get(i).getVehicleNo())
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker2));

            // 마커를 생성한다.
            mMap.addMarker(markerOptions);
        }

        //카메라를 기준 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(stationList.get(0).getNodeLati(), stationList.get(0).getNodeLong()))); // 기점으로 시점 이동
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

}
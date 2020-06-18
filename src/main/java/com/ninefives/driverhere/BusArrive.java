package com.ninefives.driverhere;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class BusArrive{
    BusArriveItem busarrive = new BusArriveItem();

    String key = "hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    String citycode = "34010"; // 천안 도시 코드

    String nodenm; // 정류소 이름
    String nodeord; // 정류소 순서

    String routeno;

    String arrivestation = null; // 도착까지 남은 정류장 수
    String arrivetime = null; // 도착까지 남은 시간

    String final_arrivestation = null; // 도착까지 남은 정류장 수
    String final_arrivetime = null; // 도착까지 남은 시간

    public BusArriveItem getXmlData(String nodeid, String routeid) {
        String queryUrl = "http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList?" + // 요청 URL
                "serviceKey=" + key + // 서비스 키 추가
                "&cityCode=" + citycode + // 도시 코드 추가
                "&nodeId=" + nodeid + // 정류소 ID 추가
                "&routeId=" + routeid;// 노선 ID 추가

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
                Log.d("findpath_parser", " name : "+xpp.getName() + "text : "+xpp.getText());
                switch (eventType) {

                    case XmlPullParser.START_TAG: // 시작 태그 별로 행동
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("item")) ;// 하나의 검색결과
                        else if (tag.equals("arrprevstationcnt")) { // 도착까지 남음 정류장 수
                            xpp.next();
                            arrivestation = xpp.getText();
                        } else if (tag.equals("arrtime")) { // 도착까지 남은 시간
                            xpp.next();
                            arrivetime = xpp.getText();
                        } else if (tag.equals("nodeid")) { // 정류소 ID
                            xpp.next();
                        } else if (tag.equals("nodenm")) { // 정류소 이름
                            xpp.next();
                            nodenm = xpp.getText();
                        } else if (tag.equals("routeid")) { // 노선 ID
                            xpp.next();
                            nodeord = xpp.getText();
                        } else if (tag.equals("routeno")) { // 노선 번호
                            xpp.next();
                            routeno = xpp.getText();
                        } else if (tag.equals("routetp")) { // 노선 타입
                            xpp.next();
                        } else if (tag.equals("vehicletp")) { // 차량 타입
                            xpp.next();
                        }
                        break;

                    case XmlPullParser.TEXT: // 텍스트라면 스킵
                        break;

                    case XmlPullParser.END_TAG: // 종료 태그라면
                        tag = xpp.getName(); //테그 이름 얻어오기

                        if (tag.equals("item")) { // 하나의 버스 정보가 끝이 났으면
                            if(final_arrivetime==null){
                                final_arrivetime = arrivetime;
                                final_arrivestation = arrivestation;
                            }else {
                                if(Integer.parseInt(final_arrivestation)>Integer.parseInt(arrivestation)){
                                    final_arrivestation = arrivestation;
                                    final_arrivetime = arrivetime;
                                }
                            }
                        }
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) { // 예외 처리
            e.printStackTrace();
        }

        if(arrivestation == null){
            busarrive.setRemainStation(null);
            busarrive.setArriveTime(null);
        }else {
            busarrive.setRemainStation(final_arrivestation);
            busarrive.setArriveTime(final_arrivetime);
        }

        return busarrive;
    }
}

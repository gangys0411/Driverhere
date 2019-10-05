package com.ninefives.driverhere;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class BusRouteSearch extends Activity {
    private ArrayAdapter<String> adapter;
    EditText edit;
    TextView text;

    XmlPullParser xpp;
    String key="hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    int cityCode=34010; // 천안 도시 코드
    String routeNo; // 버스 노선 번호

    String data;

    String busno; // 리스트 뷰 사용을 위한 변수
    String routeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busroutesearch);

        edit= (EditText)findViewById(R.id.edit);
        text= (TextView)findViewById(R.id.result);

        ListView listview;
        ListViewAdapter adapter;

        adapter = new ListViewAdapter(); // 어뎁터 생성

        listview=(ListView) findViewById(R.id.listview); // 리스트 뷰 참조
        listview.setAdapter(adapter); // 어뎁터 연결


        adapter.addItem("200", "asdf");
        adapter.addItem("200", "asdf");
        adapter.addItem("200", "asdf");
    }

    //Button을 클릭시
    public void BusSearch(View v){
        switch( v.getId() ){
            case R.id.button:

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        data= getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                        //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                        //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                text.setText(data); //TextView에 문자열  data 출력
                            }
                        });
                    }
                }).start();
                break;
        }
    }


    //XmlPullParser를 이용하여 OpenAPI XML 파일 파싱하기(parsing)
    String getXmlData(){

        StringBuffer buffer=new StringBuffer();

        routeNo= edit.getText().toString();//EditText에 작성된 Text얻어오기

        String queryUrl="http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteNoList?serviceKey="//요청 URL
                + key+
                "&cityCode="+ cityCode+
                "&routeNo="+ routeNo;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("endnodenm")){
                            buffer.append("종점 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//endnodenm 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("endvehicletime")){
                            buffer.append("막차 시간 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//endvehicletime 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("routeid")){
                            buffer.append("노선 ID :");
                            xpp.next();
                            routeid=xpp.getText();
                            buffer.append(xpp.getText());//routeid 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("routeno")){
                            buffer.append("노선 번호 :");
                            xpp.next();
                            busno=xpp.getText();
                            buffer.append(xpp.getText());//routeno 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("routetp")){
                            buffer.append("노선 타입 :");
                            xpp.next();
                            buffer.append(xpp.getText());//routetp 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("startnodenm")){
                            buffer.append("기점 :");
                            xpp.next();
                            buffer.append(xpp.getText());//startnodenm 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("startvehicletime")) {
                            buffer.append("첫차 시간 :");
                            xpp.next();
                            buffer.append(xpp.getText());//startvehicletime 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }

}
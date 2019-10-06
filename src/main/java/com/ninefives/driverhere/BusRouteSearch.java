package com.ninefives.driverhere;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class BusRouteSearch extends Activity {
    public String serviceKey = "hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 서비스 키
    private String requestUrl;
    ArrayList<Item> list = null;
    Item bus = null;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busroutesearch);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){

            requestUrl = "http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteNoList?" +
                    "serviceKey=" +serviceKey+
                    "&cityCode=34010" +
                    "&routeNo=200";
            try{
                boolean b_routeno = false;
                boolean b_routeid = false;
                boolean b_startnodenm = false;
                boolean b_endnodenm = false;

                URL url = new URL(requestUrl);
                InputStream is = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch(eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            list = new ArrayList<Item>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item") && bus != null) {
                                list.add(bus);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("item")) {
                                bus = new Item();
                            }
                            if (parser.getName().equals("endnodenm"))
                                b_endnodenm = true;
                            if (parser.getName().equals("routeid"))
                                b_routeid = true;
                            if (parser.getName().equals("routeno"))
                                b_routeno = true;
                            if (parser.getName().equals("startnodenm"))
                                b_startnodenm = true;
                            break;
                        case XmlPullParser.TEXT:
                            if (b_endnodenm) {
                                bus.setEndnodenm(parser.getText());
                                b_endnodenm = false;
                            } else if (b_routeid) {
                                bus.setRouteid(parser.getText());
                                b_routeid = false;
                            } else if (b_routeno) {
                                bus.setRouteno(parser.getText());
                                b_routeno = false;
                            } else if (b_startnodenm) {
                                bus.setStartnodenm(parser.getText());
                                b_startnodenm = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            MyAdapter adapter = new MyAdapter(getApplicationContext(),list);
            recyclerView.setAdapter(adapter);
        }
    }
}
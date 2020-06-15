package com.ninefives.driverhere;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninefives.driverhere.station_search.station_pass_bus.PassListViewItem;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ArriveTimeAdapter extends BaseAdapter {
    String StationID;

    final Handler handler = new Handler() // 메인 스레드가 아닌 곳에서 UI 변경이 일어나면 오류가 발생하므로
    {                                       // 핸들러를 사용해서 호출
        public void handleMessage(Message msg)
        {
            notifyDataSetChanged(); // 데이터 변경을 적용
        }
    };

    private ArrayList<ArriveTimeItem> arriveTimeItems = new ArrayList<ArriveTimeItem>(); // 추가된 데이터 저장을 위한 배열

    ArrayList<BusArriveItem> busarrive = new ArrayList<BusArriveItem>();

    TimerTask refresh = new TimerTask() {
        @Override
        public void run() {

            Message msg = handler.obtainMessage(); // UI 변경을 위한 핸들러 호출
            handler.sendMessage(msg);
        }
    };

    public ArriveTimeAdapter(){ // 생성자

    }

    @Override
    public int getCount(){ // 사용되는 데이터의 개수 반환
        return arriveTimeItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.arrivelistview_item, parent, false);
        }

        TextView RemainStationTextView = (TextView) convertView.findViewById(R.id.remain_station); // 도착까지 남은 정류장 수 출력 텍스트 뷰
        TextView RemainTimeTextView = (TextView) convertView.findViewById(R.id.remain_time); // 도착까지 남은 시간 출력 텍스트 뷰
        TextView RouteNoTextView = (TextView) convertView.findViewById(R.id.routeno); // 도착까지 남은 시간 출력 텍스트 뷰

        ArriveTimeItem arrivetimeitem = arriveTimeItems.get(position);

        RemainStationTextView.setText(arrivetimeitem.getRemain_Station()); // 도착까지 남은 정류장 수 출력
        RemainTimeTextView.setText(arrivetimeitem.getRemain_Time()); // 도착까지 남은 시간 출력
        RouteNoTextView.setText(arrivetimeitem.getRouteNo()); // 도착까지 남은 시간 출력

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return arriveTimeItems.get(position);
    }

    public void addItem(String remain_station, String remain_time, String routeno){ // 리스트 뷰에 아이템 추가
        ArriveTimeItem item=new ArriveTimeItem(); // 배열 선언

        item.setRemain_Station(remain_station); // 기점 추가
        item.setRemain_Time(remain_time); // 종점 추가
        item.setRouteNo(routeno); // 종점 추가

        arriveTimeItems.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        arriveTimeItems.clear();
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("BusID", arriveTimeItems.get(position).getRemain_Station()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("BusNo", arriveTimeItems.get(position).getRemain_Time());

        return intent; // 인탠트 반환
    }

    public void busArrive(String stationID){
        StationID = stationID;

        Timer timer = new Timer();
        timer.schedule(refresh, 0, 10000);
    }
}
package com.ninefives.driverhere.station_search.station_pass_bus;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninefives.driverhere.BusArrive;
import com.ninefives.driverhere.BusArriveItem;
import com.ninefives.driverhere.BusLocate;
import com.ninefives.driverhere.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PassListViewAdapter extends BaseAdapter {
    String StationID;

    final Handler handler = new Handler() // 메인 스레드가 아닌 곳에서 UI 변경이 일어나면 오류가 발생하므로
    {                                       // 핸들러를 사용해서 호출
        public void handleMessage(Message msg)
        {
            notifyDataSetChanged(); // 데이터 변경을 적용
        }
    };

    private ArrayList<PassListViewItem> listViewItemPassList = new ArrayList<PassListViewItem>(); // 추가된 데이터 저장을 위한 배열

    ArrayList<BusArriveItem> busarrive = new ArrayList<BusArriveItem>();

    TimerTask refresh = new TimerTask() {
        @Override
        public void run() {
            BusArrive busArrive = new BusArrive();

            for(int i=0; i<listViewItemPassList.size(); i++) {

                busarrive.add(busArrive.getXmlData(StationID, listViewItemPassList.get(i).getBusId())); // 버스 도착 정보 불러오기

            }
            Message msg = handler.obtainMessage(); // UI 변경을 위한 핸들러 호출
            handler.sendMessage(msg);
        }
    };

    public PassListViewAdapter(){ // 생성자

    }

    @Override
    public int getCount(){ // 사용되는 데이터의 개수 반환
        return listViewItemPassList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();



        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.passlistview_item, parent, false);
        }

        TextView BusNoTextView = (TextView) convertView.findViewById(R.id.pass_busno); // 버스 번호 출력 텍스트 뷰
        TextView DirectionTextView = (TextView) convertView.findViewById(R.id.pass_direction); // 방향 출력 텍스트 뷰
        TextView LocateTextView = (TextView) convertView.findViewById(R.id.crr_locate); // 버스 번호 출력 텍스트 뷰
        TextView RemainTimeTextView = (TextView) convertView.findViewById(R.id.remain_time); // 방향 출력 텍스트 뷰

        PassListViewItem listViewItemPassItem = listViewItemPassList.get(position);

        BusNoTextView.setText(listViewItemPassItem.getBusNo()); // 버스 번호 출력
        DirectionTextView.setText(listViewItemPassItem.getDirection()); // 방향 출력

        if(busarrive.size()>0)
        {
            BusArriveItem arriveItem = busarrive.get(position);

            LocateTextView.setText(arriveItem.getRemainStation()); // 도착까지 남은 정류장 수 출력
            RemainTimeTextView.setText(arriveItem.getArriveTime()); // 도착까지 남은 시간 출력
        }

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemPassList.get(position);
    }

    public void addItem(String busno, String busid, String start_station, String end_station){ // 리스트 뷰에 아이템 추가
        PassListViewItem item=new PassListViewItem(); // 배열 선언

        item.setBusNo(busno); // 버스 번호 추가
        item.setBusId(busid); // 버스 id 추가
        item.setStart_Station(start_station); // 기점 추가
        item.setEnd_Station(end_station); // 종점 추가

        listViewItemPassList.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        listViewItemPassList.clear();
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("BusID", listViewItemPassList.get(position).getBusId()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("BusNo", listViewItemPassList.get(position).getBusNo());

        return intent; // 인탠트 반환
    }

    public void busArrive(String stationID){
        StationID = stationID;

        Timer timer = new Timer();
        timer.schedule(refresh, 0, 10000);
    }
}
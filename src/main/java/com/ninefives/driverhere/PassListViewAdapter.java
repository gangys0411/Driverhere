package com.ninefives.driverhere;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PassListViewAdapter extends BaseAdapter {
    private ArrayList<PassListViewItem> listViewItemPassList = new ArrayList<PassListViewItem>(); // 추가된 데이터 저장을 위한 배열

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

        PassListViewItem listViewItemPassItem = listViewItemPassList.get(position);

        BusNoTextView.setText(listViewItemPassItem.getBusNo()); // 버스 번호 출력
        DirectionTextView.setText(listViewItemPassItem.getDirection()); // 방향 출력

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
}
package com.ninefives.driverhere;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>(); // 추가된 데이터 저장을 위한 배열

    public ListViewAdapter(){ // 생성자

    }

    @Override
    public int getCount(){ // 사용되는 데이터의 개수 반환
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView BusNoTextView = (TextView) convertView.findViewById(R.id.routeno); // 버스 번호 출력 텍스트 뷰
        TextView BusIdTextView = (TextView) convertView.findViewById(R.id.routeid); // 노선 id 출력 텍스트 뷰
        TextView DirectionTextView = (TextView) convertView.findViewById(R.id.direction); // 방향 출력 텍스트 뷰

        ListViewItem listViewItem = listViewItemList.get(position);

        BusNoTextView.setText(listViewItem.getBusNo()); // 버스 번호 출력
        BusIdTextView.setText(listViewItem.getBusId()); // 노선 id 출력
        DirectionTextView.setText(listViewItem.getDirection()); // 방향 출력

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    public void addItem(String routeno, String routeid, String startnodenm, String endnodenm){ // 리스트 뷰에 아이템 추가
        ListViewItem item=new ListViewItem(); // 배열 선언

        item.setBusNo(routeno); // 버스 번호 추가
        item.setBusId(routeid); // 노선 id 추가
        item.setStartNode(startnodenm); // 기점 추가
        item.setEndNode(endnodenm); // 종점 추가

        listViewItemList.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        listViewItemList.clear();
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("BusID", listViewItemList.get(position).getBusId()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("BusNo", listViewItemList.get(position).getBusNo());

        return intent; // 인탠트 반환
    }
}
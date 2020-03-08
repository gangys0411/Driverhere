package com.ninefives.driverhere;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AroundListViewAdapter extends BaseAdapter {
    private ArrayList<AroundListViewItem> listViewItemAroundList = new ArrayList<AroundListViewItem>(); // 추가된 데이터 저장을 위한 배열

    public AroundListViewAdapter(){ // 생성자

    }

    @Override
    public int getCount(){ // 사용되는 데이터의 개수 반환
        return listViewItemAroundList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.aroundlistview_item, parent, false);
        }

        TextView StationNmTextView = (TextView) convertView.findViewById(R.id.stationnm); // 정류소 이름 출력 텍스트 뷰

        AroundListViewItem aroundListViewItem = listViewItemAroundList.get(position);

        StationNmTextView.setText(aroundListViewItem.getNodeNm()); // 정류소 이름 출력

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemAroundList.get(position);
    }

    public void addItem(String nodenm, String nodeid){ // 리스트 뷰에 아이템 추가
        AroundListViewItem item=new AroundListViewItem(); // 배열 선언

        item.setNodeNm(nodenm); // 정류소 이름 추가
        item.setNodeId(nodeid); // 정류소 id 추가

        listViewItemAroundList.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        listViewItemAroundList.clear();
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("NodeID", listViewItemAroundList.get(position).getNodeId()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("NodeNm", listViewItemAroundList.get(position).getNodeNm());

        return intent; // 인탠트 반환
    }
}
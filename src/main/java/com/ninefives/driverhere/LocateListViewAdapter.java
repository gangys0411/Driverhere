package com.ninefives.driverhere;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LocateListViewAdapter extends BaseAdapter {
    private ArrayList<LocateListViewItem> listViewItemLocateList = new ArrayList<LocateListViewItem>(); // 추가된 데이터 저장을 위한 배열

    public LocateListViewAdapter(){ // 생성자

    }

    @Override
    public int getCount(){ // 사용되는 데이터의 개수 반환
        return listViewItemLocateList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.locatelistview_item, parent, false);
        }

        TextView NodeNmTextView = (TextView) convertView.findViewById(R.id.nodenm); // 정류소 이름 출력 텍스트 뷰

        LocateListViewItem locateListViewItem = listViewItemLocateList.get(position);

        NodeNmTextView.setText(locateListViewItem.getNodeNm()); // 정류소 이름 출력

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemLocateList.get(position);
    }

    public void addItem(String nodenm, String nodeid){ // 리스트 뷰에 아이템 추가
        LocateListViewItem item=new LocateListViewItem(); // 배열 선언

        item.setNodeNm(nodenm); // 정류소 이름 추가
        item.setNodeId(nodeid); // 정류소 id 추가

        listViewItemLocateList.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        listViewItemLocateList.clear();
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("NodeID", listViewItemLocateList.get(position).getNodeId()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("NodeNm", listViewItemLocateList.get(position).getNodeNm());

        return intent; // 인탠트 반환
    }
}
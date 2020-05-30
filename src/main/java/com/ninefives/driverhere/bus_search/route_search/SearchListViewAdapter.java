package com.ninefives.driverhere.bus_search.route_search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninefives.driverhere.R;

import java.util.ArrayList;

public class SearchListViewAdapter extends BaseAdapter {
    private ArrayList<SearchListViewItem> listViewItemSearchList = new ArrayList<SearchListViewItem>(); // 추가된 데이터 저장을 위한 배열

    public SearchListViewAdapter(){ // 생성자

    }

    @Override
    public int getCount(){ // 사용되는 데이터의 개수 반환
        return listViewItemSearchList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchlistview_item, parent, false);
        }

        TextView BusNoTextView = (TextView) convertView.findViewById(R.id.routeno); // 버스 번호 출력 텍스트 뷰
        TextView DirectionTextView = (TextView) convertView.findViewById(R.id.direction); // 방향 출력 텍스트 뷰

        SearchListViewItem searchListViewItem = listViewItemSearchList.get(position);

        BusNoTextView.setText(searchListViewItem.getBusNo()); // 버스 번호 출력
        DirectionTextView.setText(searchListViewItem.getDirection()); // 방향 출력

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemSearchList.get(position);
    }

    public void addItem(String routeno, String routeid, String startnodenm, String endnodenm){ // 리스트 뷰에 아이템 추가
        SearchListViewItem item=new SearchListViewItem(); // 배열 선언

        item.setBusNo(routeno); // 버스 번호 추가
        item.setBusId(routeid); // 노선 id 추가
        item.setStartNode(startnodenm); // 기점 추가
        item.setEndNode(endnodenm); // 종점 추가

        listViewItemSearchList.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        listViewItemSearchList.clear();
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("BusID", listViewItemSearchList.get(position).getBusId()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("BusNo", listViewItemSearchList.get(position).getBusNo());
        intent.putExtra("BusDirect", listViewItemSearchList.get(position).getDirection());

        return intent; // 인탠트 반환
    }
}
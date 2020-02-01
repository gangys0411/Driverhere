package com.ninefives.driverhere.station_search.station_search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninefives.driverhere.R;

import java.util.ArrayList;

public class StationListViewAdapter extends BaseAdapter {

    private ArrayList<StationListViewItem> listViewItemStationList = new ArrayList<StationListViewItem>(); // 추가된 데이터 저장을 위한 배열

    public StationListViewAdapter(){ // 생성자

    }

    @Override
    public int getCount(){ // 사용되는 데이터의 개수 반환
        return listViewItemStationList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.stationlistview_item, parent, false);
        }

        TextView NodeNmTextView = (TextView) convertView.findViewById(R.id.nodenm); // 정류소 이름 출력 텍스트 뷰

        StationListViewItem stationListViewItem = listViewItemStationList.get(position);

        NodeNmTextView.setText(stationListViewItem.getNodeNm()); // 정루소 이름 출력

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemStationList.get(position);
    }

    public void addItem(String nodenm, String nodeid){ // 리스트 뷰에 아이템 추가
        StationListViewItem item=new StationListViewItem(); // 배열 선언

        item.setNodeNm(nodenm); // 버스 번호 추가
        item.setNodeId(nodeid); // 노선 id 추가

        listViewItemStationList.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        listViewItemStationList.clear();
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("NodeNm", listViewItemStationList.get(position).getNodeNm()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("NodeID", listViewItemStationList.get(position).getNodeId());

        return intent; // 인탠트 반환
    }
}

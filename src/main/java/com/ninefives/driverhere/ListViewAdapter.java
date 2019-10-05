package com.ninefives.driverhere;

import android.content.Context;
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

        TextView BusNoTextView = (TextView) convertView.findViewById(R.id.routeno);
        TextView BusIdTextView = (TextView) convertView.findViewById(R.id.routeid);

        ListViewItem listViewItem = listViewItemList.get(position);

        BusNoTextView.setText(listViewItem.getBusNo());
        BusIdTextView.setText(listViewItem.getBusId());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    public void addItem(String routeno, String routeid){
        ListViewItem item=new ListViewItem();

        item.setBusNo(routeno);
        item.setBusId(routeid);

        listViewItemList.add(item);
    }
}

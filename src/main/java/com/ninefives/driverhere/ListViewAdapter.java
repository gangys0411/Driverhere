package com.ninefives.driverhere;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ListViewAdapter extends BaseAdapter {
    int number=0;

    final Handler handler = new Handler() // 메인 스레드가 아닌 곳에서 UI 변경이 일어나면 오류가 발생하므로
    {                                       // 핸들러를 사용해서 호출
        public void handleMessage(Message msg)
        {
            notifyDataSetChanged(); // 데이터 변경을 적용
        }
    };

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>(); // 추가된 데이터 저장을 위한 배열

    ArrayList<Integer> list = new ArrayList<Integer>();

    TimerTask refresh = new TimerTask() {
        @Override
        public void run() {
            number++;
            list.clear();
            list.add(number);

            Message msg = handler.obtainMessage(); // UI 변경을 위한 핸들러 호출
            handler.sendMessage(msg);
        }
    };

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

        TextView NodeNmTextView = (TextView) convertView.findViewById(R.id.number); // 정류소 이름 출력 텍스트 뷰

        ListViewItem ListViewItem = listViewItemList.get(position);

        for(int i=0; i<list.size(); i++) { // 조건에 맞을 경우 아이템의 색상을 변경
            if (ListViewItem.getNodeNm() == list.get(i)) {
                NodeNmTextView.setBackgroundColor(Color.YELLOW);
            }
            else
            {
                NodeNmTextView.setBackgroundColor(Color.WHITE);
            }
        }

        NodeNmTextView.setText(""+ListViewItem.getNodeNm()); // 정류소 이름 출력

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

    public void addItem(int nodenm, int nodeid){ // 리스트 뷰에 아이템 추가
        ListViewItem item=new ListViewItem(); // 배열 선언

        item.setNodeNm(nodenm); // 정류소 이름 추가
        item.setNodeId(nodeid); // 정류소 id 추가

        listViewItemList.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        listViewItemList.clear();
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("NodeID", listViewItemList.get(position).getNodeId()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("NodeNm", listViewItemList.get(position).getNodeNm());

        return intent; // 인탠트 반환
    }

    public void listadd()
    {
        Timer timer = new Timer();
        timer.schedule(refresh, 0, 10000);
    }
}
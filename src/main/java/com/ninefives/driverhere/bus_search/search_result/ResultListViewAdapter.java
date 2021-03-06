package com.ninefives.driverhere.bus_search.search_result;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninefives.driverhere.BusLocate;
import com.ninefives.driverhere.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ResultListViewAdapter extends BaseAdapter {
    String RouteId;

    int select_position;
    int select_count = 0;

    final Handler handler = new Handler() // 메인 스레드가 아닌 곳에서 UI 변경이 일어나면 오류가 발생하므로
    {                                       // 핸들러를 사용해서 호출
        public void handleMessage(Message msg) {
            notifyDataSetChanged(); // 데이터 변경을 적용
        }
    };

    private ArrayList<ResultListViewItem> listViewItemResultList = new ArrayList<ResultListViewItem>(); // 추가된 데이터 저장을 위한 배열

    ArrayList<Integer> buslocatelist = new ArrayList<Integer>();

    TimerTask refresh = new TimerTask() {
        @Override
        public void run() {
            BusLocate buslocate = new BusLocate();

            buslocatelist = buslocate.getXmlData(RouteId);

            Message msg = handler.obtainMessage(); // UI 변경을 위한 핸들러 호출
            handler.sendMessage(msg);
        }
    };

    public ResultListViewAdapter() { // 생성자

    }

    @Override
    public int getCount() { // 사용되는 데이터의 개수 반환
        return listViewItemResultList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.resultlistview_item, parent, false);
        }

        TextView NodeNmTextView = (TextView) convertView.findViewById(R.id.nodenm); // 정류소 이름 출력 텍스트 뷰
        ImageView StationStatus = (ImageView) convertView.findViewById(R.id.station_stat); // 정류소 상태 이미지 뷰

        ResultListViewItem resultListViewItem = listViewItemResultList.get(position);

        if (buslocatelist.size() > 0) {
            for (int i = 0; i < buslocatelist.size(); i++) { // 조건에 맞을 경우 아이템의 색상을 변경
                if (resultListViewItem.getNodeOrd() == buslocatelist.get(i)) { // 버스가 해당 정류장에 있을 경우
                    StationStatus.setVisibility(View.VISIBLE);
                    StationStatus.setImageResource(R.drawable.busicon); // 이미지 변경
                    break;
                } else {
                    StationStatus.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            StationStatus.setVisibility(View.INVISIBLE);
        }

        if (select_count > 0) { // 선택한 정류장이 있을 경우
            if (position == select_position) {
                StationStatus.setVisibility(View.VISIBLE);
                StationStatus.setImageResource(R.drawable.check); // 이미지 변경
            }
        }

        NodeNmTextView.setText(resultListViewItem.getNodeNm()); // 정류소 이름 출력

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemResultList.get(position);
    }

    public void addItem(String nodenm, String nodeid, int nodeord, String nodeno) { // 리스트 뷰에 아이템 추가
        ResultListViewItem item = new ResultListViewItem(); // 배열 선언

        item.setNodeNm(nodenm); // 정류소 이름 추가
        item.setNodeId(nodeid); // 정류소 id 추가
        item.setNodeOrd(nodeord); // 정류소 순서 추가
        item.setNodeNo(nodeno); // 정류소 번호 추가

        listViewItemResultList.add(item); // 리스트 뷰에 추가
    }

    public void clearItems() // 리스트 뷰 초기화
    {
        listViewItemResultList.clear();
    }

    public String selectnodeid(int position) {
        return listViewItemResultList.get(position).getNodeId();
    }

    public String selectnodeno(int position) {
        return listViewItemResultList.get(position).getNodeNo();
    }

    public String selectnodenm(int position) {
        return listViewItemResultList.get(position).getNodeNm();
    }

    public void change_select(int position) {
        if (select_count == 0) {
            select_count = 1;
        }
        select_position = position;
        notifyDataSetChanged();
    }

    public String returnDirect(){
        String direct;

        direct = listViewItemResultList.get(0).getNodeNm() + " --> " + listViewItemResultList.get(getCount()-1).getNodeNm();

        return direct;
    }

    public Intent sendIntent(int position, Intent intent) // 화면 전환을 위한 인탠트 함수
    {
        intent.putExtra("NodeID", listViewItemResultList.get(position).getNodeId()); // 인탠트에 선택된 위치의 항목 데이터를 전달
        intent.putExtra("NodeNm", listViewItemResultList.get(position).getNodeNm());
        intent.putExtra("NodeNo", listViewItemResultList.get(position).getNodeNo());

        return intent; // 인탠트 반환
    }

    public void busLocate(String routeid) {
        RouteId = routeid;

        Timer timer = new Timer();
        timer.schedule(refresh, 0, 30000);
    }

    public void stop() {
        refresh.cancel();
    }
}

package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UsersAdapter extends BaseAdapter {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;

    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount(){ // 사용되는 데이터의 개수 반환
        return mList.size();
    }

    @Override
    public Object getItem(int position){
        return mList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // position에 위치한 데이터를 화면에 출력하기 위한 뷰를 반환
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.database_item, parent, false);
        }

        TextView id = (TextView) convertView.findViewById(R.id.textView_list_id); // ID 출력 텍스트 뷰
        TextView name = (TextView) convertView.findViewById(R.id.textView_list_name); // 이름 출력 텍스트 뷰
        TextView country = (TextView) convertView.findViewById(R.id.textView_list_country); // 지역 출력 텍스트 뷰

        PersonalData personalData = mList.get(position);

        id.setText(personalData.getMember_id()); // ID 출력
        name.setText(personalData.getMember_name()); // 이름 출력
        country.setText(personalData.getMember_country()); // 지역 출력

        return convertView; // 뷰에 적용
    }

    @Override
    public long getItemId(int position){
        return position;
    }
}

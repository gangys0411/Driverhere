package com.ninefives.driverhere;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ninefives.driverhere.station_search.station_search.BusStationSearch;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    private ArrayList<String> mData = null;
    Context context;
    TinyDB tinyDB;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        ImageView delete;


        ViewHolder(View itemView) {
            super(itemView) ;
            textView1 = itemView.findViewById(R.id.station_name_tv) ;
            delete = itemView.findViewById(R.id.saved_cancel_btn);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mData.remove(getLayoutPosition());
                    tinyDB.putListString("bus", mData);
                    notifyDataSetChanged();
                }
            });

            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BusStationSearch.class);
                    intent.putExtra("stationName", textView1.getText());
                    context.startActivity(intent);
                    Log.e("보내기","ㅋ");

                }
            });



        }
    }

    StationAdapter(ArrayList<String> list) {
        mData = list;
    }

    @Override
    public StationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        tinyDB = new TinyDB(context);

        View view = inflater.inflate(R.layout.item_station_saved, parent, false) ;
        StationAdapter.ViewHolder vh = new StationAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(StationAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
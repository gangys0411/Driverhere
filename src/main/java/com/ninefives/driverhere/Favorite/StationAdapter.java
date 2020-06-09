package com.ninefives.driverhere.Favorite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ninefives.driverhere.R;
import com.ninefives.driverhere.station_search.station_pass_bus.StationPassBus;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    private ArrayList<String> nodeid;
    private ArrayList<String> nodenm;

    Context context;
    TinyDB tinyDB;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        ImageView delete;
        LinearLayout item;


        ViewHolder(View itemView) {
            super(itemView) ;
            textView1 = itemView.findViewById(R.id.station_name_tv) ;

            delete = itemView.findViewById(R.id.saved_cancel_btn);

            item = itemView.findViewById(R.id.station_item);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nodeid.remove(getLayoutPosition());
                    nodenm.remove(getLayoutPosition());

                    tinyDB.putListString("nodeid", nodeid);
                    tinyDB.putListString("nodenm", nodenm);

                    notifyDataSetChanged();
                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StationPassBus.class);

                    intent.putExtra("NodeID", nodeid.get(getLayoutPosition()));
                    intent.putExtra("NodeNm", nodenm.get(getLayoutPosition()));

                    context.startActivity(intent);
                }
            });



        }
    }

    StationAdapter(ArrayList<String> list, ArrayList<String> list2) {
        nodeid = list;
        nodenm = list2;
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

        holder.textView1.setText(nodenm.get(position)) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return nodeid.size() ;
    }
}
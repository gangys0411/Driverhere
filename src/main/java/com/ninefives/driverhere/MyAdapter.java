package com.ninefives.driverhere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Item> mList;
    private LayoutInflater mInflate;
    private Context mContext;

    public MyAdapter(Context context, ArrayList<Item> items){
        this.mList = items;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = mInflate.inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        holder.routeno.setText(mList.get(position).routeno);
        holder.routeid.setText(mList.get(position).routeid);
        holder.startnodenm.setText(mList.get(position).startnodenm);
        holder.endnodenm.setText(mList.get(position).endnodenm);
    }

    @Override
    public int getItemCount(){
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView routeno;
        public TextView routeid;
        public TextView startnodenm;
        public TextView endnodenm;

        public MyViewHolder(View itemView){
            super(itemView);

            routeno = itemView.findViewById(R.id.tv_routeno);
            routeid = itemView.findViewById(R.id.tv_routeid);
            startnodenm = itemView.findViewById(R.id.tv_startnodenm);
            endnodenm = itemView.findViewById(R.id.tv_endnodenm);
        }
    }
}

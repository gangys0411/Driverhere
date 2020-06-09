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
import com.ninefives.driverhere.bus_search.search_result.BusRouteResult;

import java.util.ArrayList;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private ArrayList<String> busid;
    private ArrayList<String> busno;
    private ArrayList<String> busdirect;

    Context context;

    TinyDB tinyDB;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        ImageView delete;
        LinearLayout item;


        ViewHolder(final View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.bus_name_tv);
            textView2 = itemView.findViewById(R.id.bus_to_go_tv);

            item = itemView.findViewById(R.id.bus_item);

            delete = itemView.findViewById(R.id.saved_cancel_btn);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    busid.remove(getLayoutPosition());
                    busno.remove(getLayoutPosition());
                    busdirect.remove(getLayoutPosition());

                    tinyDB.putListString("busid", busid);
                    tinyDB.putListString("busno", busno);
                    tinyDB.putListString("busdirect", busdirect);

                    notifyDataSetChanged();
                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BusRouteResult.class);

                    intent.putExtra("BusID", busid.get(getLayoutPosition()));
                    intent.putExtra("BusNo", busno.get(getLayoutPosition()));
                    intent.putExtra("BusDirect", busdirect.get(getLayoutPosition()));

                    context.startActivity(intent);

                }
            });


        }
    }

    BusAdapter(ArrayList<String> list, ArrayList<String> list2, ArrayList<String> list3) {
        busid = list;
        busno = list2;
        busdirect = list3;
    }

    @Override
    public BusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        tinyDB = new TinyDB(context);
        View view = inflater.inflate(R.layout.item_bus_saved, parent, false);
        BusAdapter.ViewHolder vh = new BusAdapter.ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(BusAdapter.ViewHolder holder, int position) {

        holder.textView1.setText(busno.get(position));
        holder.textView2.setText(busdirect.get(position));

    }

    @Override
    public int getItemCount() {
        return busid.size();
    }

}
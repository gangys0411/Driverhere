package com.ninefives.driverhere;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ninefives.driverhere.bus_search.search_result.BusRouteResult;

import java.util.ArrayList;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private ArrayList<String> mData2;
    int pos;
    Context context;
    String key="hZamgNLm7reK22wjgIGrV%2Fj1NU6UOQ2LYKM%2FQ9HEfqvmkSF%2FxgPJiUlxuztmy4tSnEr7g12A9Kc%2FLzSJdkdTeQ%3D%3D"; // 오픈 api 서비스 키
    String cityCode="34010"; // 천안 도시 코드
    String routeNo; // 버스 노선 번호
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
                    mData.remove(getLayoutPosition());
                    tinyDB.putListString("busid", mData);
                    notifyDataSetChanged();
                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BusRouteResult.class); // 버스 검색 화면이 아닌 검색 후 버스 노선이 나오는 화면으로 노선 ID를 전달
                    intent.putExtra("BusID", textView1.getText()); // 기존에 있던 정의와 동일하게 사용하기 위해 변경
                    context.startActivity(intent);

                }
            });


        }
    }

    BusAdapter(ArrayList<String> list, ArrayList<String> list2) {
        mData = list;
        mData2 = list2;
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
        pos = position;
        String text = mData.get(position);
        String text2 = mData2.get(position);
        holder.textView1.setText(text);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
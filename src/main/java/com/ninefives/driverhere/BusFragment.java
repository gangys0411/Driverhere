package com.ninefives.driverhere;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BusFragment extends Fragment {
    SavedActivity activity;
    TinyDB tinyDB;
    RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        activity = (SavedActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //이제 더이상 엑티비티 참초가안됨
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bus_saved, container, false);

        tinyDB = new TinyDB(getContext());

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> sub = new ArrayList<>();
        list = tinyDB.getListString("busid");
        list2 = tinyDB.getListString("busno");

        /*for (int i = 0; i < sub.size(); i++) {
            list.add(sub.get(i));
            list2.add(sub.get(i));
        }*/

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = rootView.findViewById(R.id.bus_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        BusAdapter adapter = new BusAdapter(list, list2);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

}


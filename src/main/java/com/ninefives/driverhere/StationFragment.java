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

public class StationFragment extends Fragment {
    SavedActivity activity;
    TinyDB tinyDB;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SavedActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_station_saved, container, false);
        tinyDB = new TinyDB(getContext());

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> sub = new ArrayList<>();
        sub = tinyDB.getListString("station");

        for(int i=0; i<sub.size(); i++){
            list.add(sub.get(i));
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.station_rv) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;
        StationAdapter adapter = new StationAdapter(list) ;
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
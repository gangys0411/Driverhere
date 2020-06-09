package com.ninefives.driverhere.Favorite;

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

import com.ninefives.driverhere.R;

import java.util.ArrayList;

public class StationFragment extends Fragment {
    SavedActivity activity;
    TinyDB tinyDB;
    RecyclerView recyclerView;

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
        ArrayList<String> list2 = new ArrayList<>();

        list = tinyDB.getListString("nodeid");
        list2 = tinyDB.getListString("nodenm");

        recyclerView = rootView.findViewById(R.id.station_rv) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;

        StationAdapter adapter = new StationAdapter(list, list2) ;
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
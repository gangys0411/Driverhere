package com.ninefives.driverhere;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SavedActivity extends AppCompatActivity {

    TextView busBtn, stationBtn;
    FrameLayout container;
    View v1, v2;
    StationFragment stationFragment;
    BusFragment busFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        stationFragment = new StationFragment();
        busFragment = new BusFragment();

        busBtn = findViewById(R.id.bus_btn);
        stationBtn = findViewById(R.id.station_btn);
        container = findViewById(R.id.container_saved);
        v1 = findViewById(R.id.back_bus);
        v2 = findViewById(R.id.back_station);
        v2.setVisibility(View.INVISIBLE);

        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_saved, busFragment).commit();

        busBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_saved, busFragment).commit();
            }
        });
        stationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1.setVisibility(View.INVISIBLE);
                v2.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_saved, stationFragment).commit();
            }
        });
    }
}

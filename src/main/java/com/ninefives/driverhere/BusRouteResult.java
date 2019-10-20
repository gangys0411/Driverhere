package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BusRouteResult extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_busrouteresult);

        Intent intent = getIntent();

        TextView result=(TextView) findViewById(R.id.result);

        result.setText(intent.getStringExtra("BusID"));
    }
}

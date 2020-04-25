package com.ninefives.driverhere;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AlarmReserve extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmreserve);

        Spinner minuteSpinner = (Spinner)findViewById(R.id.select_time);
        ArrayAdapter minuteAdapter = ArrayAdapter.createFromResource(this,
                R.array.reserve_minute, android.R.layout.simple_spinner_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);
    }
}

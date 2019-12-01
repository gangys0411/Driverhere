package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void MainActivityClick (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void NextStopSearchClick (View view){
        Intent intent = new Intent(this, NextStopSearch.class);
        startActivity(intent);
    }
}

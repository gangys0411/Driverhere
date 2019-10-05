package com.ninefives.driverhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void BusSearchClick(View view){ // 버스 검색 버튼 클릭 이벤트
        Intent intent = new Intent(this, BusRouteSearch.class);
        startActivity(intent);
        finish();
    }
}
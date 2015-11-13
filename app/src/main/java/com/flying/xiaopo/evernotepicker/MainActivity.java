package com.flying.xiaopo.evernotepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TimeBar mTimeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimeBar = (TimeBar) findViewById(R.id.timeBar);
    }

    public void showTime(View v) {
        Toast.makeText(this, "Time->" + mTimeBar.getTime() + "\nHour->" + mTimeBar.getHour() + "\nMinute->" + mTimeBar.getMinute(), Toast.LENGTH_SHORT).show();
    }
}

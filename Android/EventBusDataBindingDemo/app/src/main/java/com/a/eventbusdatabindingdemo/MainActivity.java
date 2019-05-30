package com.a.eventbusdatabindingdemo;

import android.os.Bundle;
import android.os.Handler;

import com.a.dao.MainData;
import com.a.eventbusdatabindingdemo.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding dataBinding;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainData mainData = new MainData();
        mainData.engName = "AngleBaby";
        mainData.name = "翠花";
        dataBinding.setMainData(mainData);
        postDelay(mainData);

        setFragment();
    }

    private void setFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.rl, new MainFragment()).commit();
    }

    private void postDelay(final MainData mainData) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainData.engName = "AngleBaby" + Math.random();
                dataBinding.setMainData(mainData);
                postDelay(mainData);
            }
        }, 1000);
    }
}

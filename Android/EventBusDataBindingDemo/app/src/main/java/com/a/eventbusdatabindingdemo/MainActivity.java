package com.a.eventbusdatabindingdemo;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

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
        mainData.setEngName("AngleBaby");
        mainData.setName("翠花");
        dataBinding.setMainData(mainData);
        dataBinding.setClickEvent(new ClickEvent());
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
                mainData.engName.set("AngleBaby" + Math.random());
                postDelay(mainData);
            }
        }, 1000);
    }


    public class ClickEvent {
        public void clickEvent() {
            Toast.makeText(MainActivity.this, "click me", Toast.LENGTH_SHORT).show();
        }
    }
}

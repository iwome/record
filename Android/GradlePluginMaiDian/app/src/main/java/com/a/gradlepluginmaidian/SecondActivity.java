package com.a.gradlepluginmaidian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.a.lib.DataContent;
import com.a.lib.DataName;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @DataName(value = "onClickA")
    @DataContent(value = "666666666")
    public void onClickA(View view) {
    }

    @DataName(value = "onClickB")
    @DataContent(value = "9999999999")
    public void onClickB(View view) {
    }
}

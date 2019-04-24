package com.a.gradlepluginmaidian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.a.lib.DataContent;
import com.a.lib.DataName;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String htmlText = "有效期02.32 12:30<font color='#D7063B'>（仅剩三天）</font>";
        TextView tv = findViewById(R.id.tv_content);
        tv.setText(Html.fromHtml(htmlText));
    }

    @DataName(value = "goToSecond")
    @DataContent(value = "sss")
    public void onClick(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

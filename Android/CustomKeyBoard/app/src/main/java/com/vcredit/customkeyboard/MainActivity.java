package com.vcredit.customkeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnPopus;
    private KeyBoardViewHelper viewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPopus = (Button) findViewById(R.id.btn_popus);
        viewHelper = new KeyBoardViewHelper(this);
        final View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        btnPopus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHelper.showKeyboard(rootview);
            }
        });

        viewHelper.eventBind(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_close:
                        viewHelper.closeKeyboard();
                        break;
                    case R.id.btn_confirm:
                        Toast.makeText(MainActivity.this, "btn_confirm", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_recharge:
                        Toast.makeText(MainActivity.this, "btn_recharge", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if(kv.getVisibility()== View.VISIBLE){
//            helper.hideKeyboard();
//        }
        return super.onTouchEvent(event);
    }
}

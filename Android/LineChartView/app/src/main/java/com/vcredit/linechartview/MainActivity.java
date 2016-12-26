package com.vcredit.linechartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LineChartView lineChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChartView = (LineChartView) findViewById(R.id.my_account_line_chart);
        initLineChartDataTest();

    }

    private void initLineChartDataTest() {
        String tempNearlyRevenue = "201501:3600.0,201502:600.0,201503:3600.0,201504:15.0,201505:16.0,201506:1800.0";
        String[] arrays = tempNearlyRevenue.split(",");


        int size = arrays.length;

        String[] XLabels = new String[size];//{}; // X轴刻度
        int[] YLabels = new int[4]; // Y轴刻度
        String[] AllData = new String[size];//{"00.00", "00.00", "00.00", "00.00", "00.00", "00.00"}; // 数据
        float[] AllDataFloat = new float[size];//{00.00f,00.00f,00.00f,00.00f,00.00f,00.00f};

        int index ;
        for (int i = 0; i < arrays.length; i++) {
            index = arrays[i].indexOf(":");
            String tmp = arrays[i].substring(index + 1, arrays[i].length());
            AllData[i] = String.valueOf(Double.parseDouble(tmp));
            AllDataFloat[i] = CommonUtils.changeStringToFlost(AllData[i]);

            XLabels[i] = intercept(arrays[i].substring(0, index));
        }
        boolean isVoid = false;

        float[] tempFloat = sortScore(AllDataFloat);
        int maxIndex = (int) (tempFloat[0]);
        maxIndex = Math.round(maxIndex / 3);
        int remainder = maxIndex % 10;
        maxIndex = maxIndex - remainder + 10;
        //Y轴刻度
        if (maxIndex == 0) {
            YLabels[0] = 0;
            YLabels[1] = 10;
            YLabels[2] = 20;
            YLabels[3] = 30;
        } else {
            YLabels[0] = 0;
            YLabels[1] = maxIndex;
            YLabels[2] = maxIndex * 2;
            YLabels[3] = maxIndex * 3;
        }
        // 为曲线图控件设置X轴刻度、Y轴刻度及业务数据
        lineChartView.SetInfo(XLabels, YLabels, AllData, arrays.length - 1, isVoid);
        lineChartView.setVisibility(View.VISIBLE);

    }

    private String intercept(String input) {
        String temp = "";
        int size = input.length();
        if (size > 4) {
            temp = input.substring(2, 4);
            temp += "/";
            temp += input.substring(4, size);
        }
        return temp;
    }

    //数组排序
    public float[] sortScore(float[] score) {
        float temp = 0f;
        //大->小
        for (int i = 0; i < score.length - 1; i++) {

            for (int j = i + 1; j < score.length; j++) {

                if (score[j] > score[i]) {
                    temp = score[i];
                    score[i] = score[j];
                    score[j] = temp;
                }
            }
        }
        return score;

    }
}

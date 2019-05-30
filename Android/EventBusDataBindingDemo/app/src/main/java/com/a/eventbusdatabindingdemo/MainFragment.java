package com.a.eventbusdatabindingdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.a.dao.MainData;
import com.a.dao.MainItemData;
import com.a.eventbusdatabindingdemo.databinding.FragmentMainBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * balabala..
 * Created by bangbang.qiu on 2019/5/30.
 */
public class MainFragment extends Fragment {

    private FragmentMainBinding dataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainData mainData = new MainData();
        mainData.sonEngName = "AngelBabyBay";
        mainData.sonName = "小翠花";
        dataBinding.setMainData(mainData);

        MainAdapter mainAdapter = new MainAdapter(requestData());
        dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataBinding.recyclerView.setAdapter(mainAdapter);

    }

    private ArrayList<MainItemData> requestData() {
        ArrayList<MainItemData> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new MainItemData("选项" + i, Math.random() > 0.5));
        }
        return list;
    }
}

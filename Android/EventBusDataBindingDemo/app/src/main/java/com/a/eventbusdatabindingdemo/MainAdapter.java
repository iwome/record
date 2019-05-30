package com.a.eventbusdatabindingdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a.dao.MainItemData;
import com.a.eventbusdatabindingdemo.databinding.ItemMainBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * balabala..
 * Created by bangbang.qiu on 2019/5/30.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.VHolder> {

    ArrayList<MainItemData> list;

    public MainAdapter(ArrayList<MainItemData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.dataBinding.setMainData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VHolder extends RecyclerView.ViewHolder {
        ItemMainBinding dataBinding;

        VHolder(@NonNull View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}

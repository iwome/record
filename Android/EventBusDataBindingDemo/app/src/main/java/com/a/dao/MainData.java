package com.a.dao;

import androidx.databinding.ObservableField;

/**
 * balabala..
 * Created by bangbang.qiu on 2019/5/30.
 */
public class MainData {
    public ObservableField<String> name;
    public ObservableField<String> engName;
    public String sonName;
    public String sonEngName;

    public void setName(String name){
        if(this.name == null){
            this.name = new ObservableField<>(name);
        }
        this.name.set(name);
    }

    public void setEngName(String engName){
        if(this.engName == null){
            this.engName = new ObservableField<>(engName);
        }
        this.engName.set(engName);
    }
}

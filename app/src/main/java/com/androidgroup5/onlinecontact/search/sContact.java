package com.androidgroup5.onlinecontact.search;

import com.androidgroup5.onlinecontact.cn.CN;


public class sContact implements CN {

    public final String name;
    public final int imgUrl;
    public sContact(String name,String Number, int imgUrl) {
        this.name = name+"  "+Number;
        this.imgUrl = imgUrl;
    }

    @Override
    public String chinese() {
        return name;
    }
}
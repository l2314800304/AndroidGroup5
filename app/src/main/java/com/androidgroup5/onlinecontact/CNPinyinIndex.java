package com.androidgroup5.onlinecontact.cn;

import java.io.Serializable;


public class CNPinyinIndex <T extends CN> implements Serializable {

    public final CNPinyin<T> cnPinyin;

    public final int start;

    public final int end;

    CNPinyinIndex(CNPinyin cnPinyin, int start, int end) {
        this.cnPinyin = cnPinyin;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return cnPinyin.toString()+"  start " + start+"  end " + end;
    }
}

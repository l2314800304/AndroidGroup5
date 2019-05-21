package com.androidgroup5.onlinecontact;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<Model> data;
    private Context context;
    private Delete.AllCheckListener allCheckListener;

    public MyAdapter(List<Model> data, Context context, Delete.AllCheckListener allCheckListener) {
        this.data = data;
        this.context = context;
        this.allCheckListener = allCheckListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHoder hd;
        if (view == null) {
            hd = new ViewHoder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.item, null);
            hd.textView = (TextView) view.findViewById(R.id.text_title);
            hd.checkBox = (CheckBox) view.findViewById(R.id.ckb);
            view.setTag(hd);
        }
        Model mModel = data.get(i);
        hd = (ViewHoder) view.getTag();
        hd.textView.setText(mModel.getSt());

        Log.e("myadapter", mModel.getSt() + "------" + mModel.ischeck());
        final ViewHoder hdFinal = hd;
        hd.checkBox.setChecked(mModel.ischeck());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = hdFinal.checkBox;
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    data.get(i).setIscheck(false);
                } else {
                    checkBox.setChecked(true);
                    data.get(i).setIscheck(true);
                }
                //监听每个item，若所有checkbox都为选中状态则更改main的全选checkbox状态
                for (Model model : data) {
                    if (!model.ischeck()) {
                        allCheckListener.onCheckedChanged(false);
                        return;
                    }
                }
                allCheckListener.onCheckedChanged(true);


            }
        });


        return view;
    }

    class ViewHoder {
        TextView textView;
        CheckBox checkBox;
    }

}


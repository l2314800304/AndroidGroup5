package com.androidgroup5.onlinecontact.callLog.adapter;

/**
 * Created by 刘祎锦
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.androidgroup5.onlinecontact.R;

public class callLogAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;

    public callLogAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Holder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_call_log, parent, false);
            holder = new Holder();
            holder.tvName = convertView.findViewById(R.id.tv_item_name);
            holder.tvDuration = convertView.findViewById(R.id.tv_item_duration);
            holder.tvTimeLead = convertView.findViewById(R.id.tv_item_time_lead);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        /**
         * 通话类型 此处可以根据类型进行逻辑处理，这里忽略处理
         */
        if (TextUtils.equals((list.get(position).get("type") + ""), "打入")) { //"打入"
            holder.tvDuration.setText(list.get(position).get("duration") + "");// 通话时长
        } else if (TextUtils.equals((list.get(position).get("type") + ""), "打出")) {  //"打出"
            holder.tvDuration.setText(list.get(position).get("duration") + "");// 通话时长
        } else if (TextUtils.equals((list.get(position).get("type") + ""), "未接")) { //"未接"
            holder.tvDuration.setText("未接通");// 通话时长
        } else {
            holder.tvDuration.setText("未接通");// 通话时长
        }
        /**
         * 通话记录的联系人
         */
        if (TextUtils.equals((list.get(position).get("name") + ""), "未备注联系人")) {// 通话记录的联系人
            holder.tvName.setText(list.get(position).get("number"));// 通话记录的联系人
        } else {
            holder.tvName.setText(list.get(position).get("name") + "(" + list.get(position).get("number") + ")");// 通话记录的联系人
        }
        holder.tvTimeLead.setText(list.get(position).get("date") + "  " + list.get(position).get("duration"));// 通话距离
        return convertView;
    }

    class Holder {
        private TextView tvName,//名字
                tvDuration,//时长
                tvTimeLead;//时间差
    }
}

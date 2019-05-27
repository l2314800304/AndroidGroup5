package com.androidgroup5.onlinecontact.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.androidgroup5.onlinecontact.R;
import com.androidgroup5.onlinecontact.cn.CNPinyinIndex;
import com.androidgroup5.onlinecontact.search.Contact;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<ContactHolder> {

    private final List<CNPinyinIndex<Contact>> contactList;

    public SearchAdapter(List<CNPinyinIndex<Contact>> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        CNPinyinIndex<Contact> index = contactList.get(position);
        Contact contact = index.cnPinyin.data;
        holder.iv_header.setImageResource(contact.imgUrl);

        SpannableStringBuilder ssb = new SpannableStringBuilder(contact.chinese());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
        ssb.setSpan(span, index.start, index.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_name.setText(ssb);
    }

    public void setNewDatas(List<CNPinyinIndex<Contact>> newDatas) {
        this.contactList.clear();
        if (newDatas != null && !newDatas.isEmpty()) {
            this.contactList.addAll(newDatas);
        }
        notifyDataSetChanged();
    }

}

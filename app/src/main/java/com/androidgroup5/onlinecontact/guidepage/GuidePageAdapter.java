package com.androidgroup5.onlinecontact.guidepage;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;



public class GuidePageAdapter extends PagerAdapter {
    private ArrayList<View> pageList;

    /**
     * 构造方法
     * @param pageList 传入将要显示的控件的集合
     */
    public GuidePageAdapter(ArrayList<View> pageList) {
        this.pageList = pageList;
    }

    /**
     * 返回数据长度
     * @return
     */
    @Override
    public int getCount() {
        return pageList.size();
    }

    /**
     * 返回界面
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 加载视图
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pageList.get(position));
        return pageList.get(position);
    }

    /**
     * 清除视图
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pageList.get(position));
    }
}

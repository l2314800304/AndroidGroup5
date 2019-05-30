package com.androidgroup5.onlinecontact.guidepage;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Author LYJ
 * Created on 2016/11/29.
 * Time 13:41
 */

public class GuideImageLayout extends FrameLayout {
    private static final String TAG = GuideImageLayout.class.getSimpleName();
    private static ArrayList<ImageView> showItems;//显示图像的集合
    private static int itemCounts;//item的数量
    private ViewPager viewPager;//选项卡
    private int imagePosition;//位置
    public GuideImageLayout(Context context) {
        this(context,null);
    }
    public GuideImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    /**
     * 初始化
     */
    private void init() {
        showItems = new ArrayList<>();//数据集合用来存放ImageView
    }

    /**
     * 设置显示的数量
     * @param resID
     * @return true为添加成功，false为添加失败
     */
    public boolean addItems(@DrawableRes int[] resID){
        itemCounts = resID.length;
        for (int i = 0; i < itemCounts; i++) {
            ImageView imageView = new ImageView(getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(resID[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addView(imageView);
            showItems.add(imageView);
        }
        showItemPosition(0);
        return showItems.size() == itemCounts;
    }

    /**
     * 设置ViewPager
     * @param pager
     */
    public void setViewPager(ViewPager pager){
        this.viewPager = pager;
        if (viewPager == null){
            throw new NullPointerException("viewPager is null,Please setViewPager(viewPager)");
        }
        viewPager.addOnPageChangeListener(pagerListener);
    }

    private ViewPager.SimpleOnPageChangeListener pagerListener = new ViewPager.SimpleOnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (position == imagePosition && position < itemCounts - 1) {
                showItems.get(position + 1).setAlpha(positionOffset);
                Log.i(TAG, "onPageScrolled: 向左滑动");
            } else if (position == imagePosition - 1) {
                showItems.get(position).setAlpha(positionOffset);
                Log.i(TAG, "onPageScrolled: 向右滑动");
            }
            imagePosition = position;
            Log.i(TAG, "onPageScrolled: " + position);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            showItemPosition(position);
        }
    };
    /**
     * 显示Item
     * @param position
     */
    private void showItemPosition(int position){
        for (int i = 0; i < itemCounts; i++) {
            if (position == i){
                showItems.get(i).setAlpha(1.0f);
            }else {
                showItems.get(i).setAlpha(0.0f);
            }
        }
    }
}

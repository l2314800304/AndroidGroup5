package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.androidgroup5.onlinecontact.guidepage.GuideImageLayout;
import com.androidgroup5.onlinecontact.guidepage.GuidePageAdapter;

import java.util.ArrayList;


public class GuidePageActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager guidePage;
    private ArrayList<View> pageList;
    private GuidePageAdapter guideAdapter;
    private CircleIndicator circleIndicator;
    private int[] fontImages = new int[]{R.drawable.guide_front1, R.drawable.guide_front2, R.drawable.guide_front3, R.drawable.guide_front4};
    private int[] backImages = new int[]{R.drawable.guide_back1, R.drawable.guide_back2, R.drawable.guide_back3, R.drawable.guide_back4};
    private GuideImageLayout guideImage;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        initData();//初始化数据
        initView();//初始化控件
        initPageListener(); //ciewPage的滑动监听事件

    }

    private void initPageListener(  ) {
        guidePage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当viewpage是最后一页时让按钮显示，否则隐藏
                if (position == 3) {
                    startBtn.setVisibility(View.VISIBLE);
                } else {
                    startBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        /*
        设置ViewPager
         */
        guidePage = (ViewPager) findViewById(R.id.guide_page);
        guidePage.setAdapter(guideAdapter);
        /*
        设置指示器
         */
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(guidePage);
        /*
        设置显示图片
         */
        guideImage = (GuideImageLayout) findViewById(R.id.images);
        guideImage.addItems(backImages);
        guideImage.setViewPager(guidePage);

        startBtn = (Button) findViewById(R.id.start_project);
        startBtn.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        pageList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View page = getLayoutInflater().inflate(R.layout.guide_page_item, null);
            page.setBackgroundResource(fontImages[i]);
            pageList.add(page);
        }
        guideAdapter = new GuidePageAdapter(pageList);
    }


    /**
     * 当点击开启按钮时进行界面的跳转
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_project:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}

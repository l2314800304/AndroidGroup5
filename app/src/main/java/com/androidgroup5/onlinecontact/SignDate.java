package com.androidgroup5.onlinecontact;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SignDate extends AppCompatActivity {

        // 设置默认选中的日期
        private String date = null;
        private TextView popupwindow_calendar_month;
        private SignCalendar calendar;
        private Button btn_signIn;
        private List<String> list = new ArrayList<String>(); //设置标记列表
        DBManager dbManager;
        boolean isinput = false;
        private String date1 = null;//单天日期


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signdate);
            // 初始化DBManager
            dbManager = new DBManager(this);
            //日期格式   格式为 “2019-05-03” 标准DATE格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            //获取当前时间
            Date curDate = new Date(System.currentTimeMillis());
            //设置时间格式为设置格式
            date1 = formatter.format(curDate);
            //初始化显示年月控件
            popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
            //签到按钮
            btn_signIn = (Button) findViewById(R.id.btn_signIn);
            //日期控件
            calendar = (SignCalendar) findViewById(R.id.popupwindow_calendar);
            //设置当前年月
            popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
                    + calendar.getCalendarMonth() + "月");
            if (null != date) {
                int years = Integer.parseInt(date.substring(0,
                        date.indexOf("-")));
                int month = Integer.parseInt(date.substring(
                        date.indexOf("-") + 1, date.lastIndexOf("-")));
                popupwindow_calendar_month.setText(years + "年" + month + "月");
                calendar.showCalendar(years, month);
                calendar.setCalendarDayBgColor(date, R.color.white);
            }
            //添加标记
            add("2018-11-04");
            add("2018-11-05");
            add("2018-11-07");
            add("2018-11-10");
            add("2018-11-12");
            add("2018-12-02");
            // 添加日期标注
            query();
            if (isinput) {
                //设置签到按钮文字
                btn_signIn.setText("今日已签，明日继续");
                //设置签到按钮背景
                btn_signIn.setBackgroundResource(R.drawable.button_gray);
                //禁止按钮点击
                btn_signIn.setEnabled(false);
            }
            btn_signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date today = calendar.getThisday();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//           /* calendar.removeAllMarks();
//           list.add(df.format(today));
//           calendar.addMarks(list, 0);*/
                    //将当前日期标示出来
                    add(df.format(today));
                    // 添加日期标注
                    query();
                    HashMap<String, Integer> bg = new HashMap<String, Integer>();
                    //设置当天日期背景颜色
                    calendar.setCalendarDayBgColor(today, R.drawable.bg_sign_today);
                    //设置签到按钮文字
                    btn_signIn.setText("今日已签，明日继续");
                    //设置签到按钮背景
                    btn_signIn.setBackgroundResource(R.drawable.button_gray);
                    //禁止按钮点击
                    btn_signIn.setEnabled(false);
                }
            });
//		//拓展监听所选中的日期
//		calendar.setOnCalendarClickListener(new SignCalendar.OnCalendarClickListener() {
//
//			public void onCalendarClick(int row, int col, String dateFormat) {
//				int month = Integer.parseInt(dateFormat.substring(
//						dateFormat.indexOf("-") + 1,
//						dateFormat.lastIndexOf("-")));
//
//				if (calendar.getCalendarMonth() - month == 1//跨年跳转
//						|| calendar.getCalendarMonth() - month == -11) {
//					calendar.lastMonth();
//
//				} else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
//						|| month - calendar.getCalendarMonth() == -11) {
//					calendar.nextMonth();
//
//				} else {
//					list.add(dateFormat);
//					calendar.addMarks(list, 0);
//					calendar.removeAllBgColor();
//					calendar.setCalendarDayBgColor(dateFormat,
//							R.drawable.calendar_date_focused);
//					date = dateFormat;//最后返回给全局 date
//				}
//			}
//		});
            //监听当前月份
            calendar.setOnCalendarDateChangedListener(new SignCalendar.OnCalendarDateChangedListener() {
                public void onCalendarDateChanged(int year, int month) {
                    popupwindow_calendar_month
                            .setText(year + "年" + month + "月");
                }
            });
        }

        //添加签到日期
        public void add(String date) {
            ArrayList<sqlit> persons = new ArrayList<sqlit>();
            sqlit person1 = new sqlit(date, "true");
            persons.add(person1);
            dbManager.add(persons);
        }
        //签到日期显示签到图案
        public void query() {
            List<sqlit> persons = dbManager.query();
            for (sqlit person : persons) {
                list.add(person.date);
                if (date1.equals(person.getDate())) {
                    isinput = true;
                }
            }
            calendar.addMarks(list, 0);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            // 释放数据库资源
            dbManager.closeDB();
        }

        //前一个月日历
        public void onLeft(View view) {
            calendar.lastMonth();
        }

        //进入下一月
        public void onRight(View view) {
            calendar.nextMonth();
        }
    }


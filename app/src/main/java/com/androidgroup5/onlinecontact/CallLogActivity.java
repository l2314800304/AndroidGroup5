package com.androidgroup5.onlinecontact;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.User;
import com.androidgroup5.onlinecontact.callLog.adapter.callLogAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.icu.text.DateTimePatternGenerator.DAY;

public class CallLogActivity extends AppCompatActivity {
    private ListView listView;
    private List<Map<String, String>> dataList;
    private ContentResolver resolver;


    private callLogAdapter adapter;
    private String mobile;//被授权人电话号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonelist);
        initView();
        getPersimmionInfo();

    }

    private void initView() {
        listView = findViewById(R.id.list_view);
    }

    //授权信息
    private void getPersimmionInfo() {
        if (Build.VERSION.SDK_INT >= 23) {
            //1. 检测是否添加权限   PERMISSION_GRANTED  表示已经授权并可以使用
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                //手机为Android6.0的版本,未授权则动态请求授权
                //2. 申请请求授权权限
                //1. Activity
                // 2. 申请的权限名称
                // 3. 申请权限的 请求码
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.READ_CALL_LOG//通话记录
                        }, 1005);
            } else {//手机为Android6.0的版本,权限已授权可以使用
                // 执行下一步
                initContacts();
            }
        } else {//手机为Android6.0以前的版本，可以使用
            initContacts();
        }

    }

    private void initContacts() {
        dataList = getDataList();
        adapter = new callLogAdapter(this, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mobile = dataList.get(position).get("number")
                        .replaceAll(" ", "")
                        .replaceAll("\\+", "")
                        .replaceAll("-", "");
                String nameStr = dataList.get(position).get("name");
                Toast.makeText(CallLogActivity.this, "=mobile:" + mobile + "=nameStr:" + nameStr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 读取数据
     *
     * @return 读取到的数据
     */
    private List<Map<String, String>> getDataList() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
        }

        User u=((UserParameter)getApplication()).getLocal();

        List<Map<String, String>> list = new ArrayList<>();


        for (int i = 0; i<u.getRecord().size(); i++) {
            String[] columns = {
                    u.getRecord().get(i).getNumber(),// 通话记录的电话号码
                    u.getRecord().get(i).getDate(),// 通话记录的日期
                    u.getRecord().get(i).getDuration(),// 通话时长
                    u.getRecord().get(i).getType() // 通话类型
            };

            String name = u.getContact().get(0).getName();
            String number = columns[0];
            long dateLong = Long.parseLong(columns[1]);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));

            int duration = Integer.parseInt(columns[2]);
            int type = Integer.parseInt(columns[3]);

            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
            String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));
            String typeString = "";

            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    //"打入"
                    typeString = "打入";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    //"打出"
                    typeString = "打出";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    //"未接"
                    typeString = "未接";
                    break;
                default:
                    break;
            }

            if (isMobileNO(number)) {
                String dayString = "";
                if ((Integer.parseInt(dayCurrent)) == (Integer.parseInt(dayRecord))) {
                    //今天
                    dayString = "今天";
                } else if ((Integer.parseInt(dayCurrent) - 1) == (Integer.parseInt(dayRecord))) {
                    //昨天
                    dayString = "昨天";
                } else {
                    //前天
                    dayString = "更久之前";
                }
                long day_lead =getTimeDistance(new Date(dateLong),new Date());
                if (day_lead < 2) {//只显示48小时以内通话记录，防止通话记录数据过多影响加载速度
                    Map<String, String> map = new HashMap<>();
                    //"未备注联系人"
                    map.put("name", (name == null) ? "未备注联系人" : name);//姓名
                    map.put("number", number);//手机号
                    map.put("date", date);//通话日期
                    // "分钟"
                    map.put("duration", (duration / 60) + "分钟");//时长
                    map.put("type", typeString);//类型
                    map.put("time", time);//通话时间
                    map.put("day", dayString);//
                    // map.put("time_lead", TimeStampUtil.compareTime(date));
                    list.add(map);
                }else {
                    return list;
                }

            }
        }
        return list;
    }
    //验证手机号是否正确ֻ
    public static boolean isMobileNO(String s) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
    /**
     * 获得两个日期间距多少天
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getTimeDistance(Date beginDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(beginDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        fromCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        fromCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        fromCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        toCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        toCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        toCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        long dayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / DAY;
        dayDistance = Math.abs(dayDistance);

        return dayDistance;
    }

}

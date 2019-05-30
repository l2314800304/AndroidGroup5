package com.androidgroup5.onlinecontact;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidgroup5.onlinecontact.EntityClass.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * listView中嵌入复选框实现单选、全选删除列表条目
 * 注意事项：
 *1.子布局中的checkBox以下两个属性（会导致onItemClickListener（即单个条目的点击事件）失效）：
 * android:clickable="false"
 * android:focusable="false"
 *2.在删除过程中，首先应该把勾选的删除内容添加到一个新的删除集合中，不能直接在数据源中进行删除，否则会报异常
 *3.在删除之后，并把删除集合中的内容清空
 */
public class Delete extends AppCompatActivity {
    User u;
    //定义TextView
    private TextView tv_show;
    //定义listview
    private ListView lv_data;
    //定义控件
    private Button btn_delete;
    private CheckBox che_all;
    //声明一个集合（数据源）
    private List<Item> data;
    private List<Integer> index;
    //定义自定义适配器
    private MyAdapter myAdapter;
    //给数据源添加数据
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(Delete.this, "删除成功！返回主页面...", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(Delete.this, "删除失败，请检查网络连接！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
    private void initdata(){

        data=new ArrayList<>();
        for (int i = 0; i < u.getContact().size(); i++) {
            Random random = new Random(System.currentTimeMillis());
            int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};
            int urlIndex = random.nextInt(URLS.length - 1);
            int url = URLS[urlIndex];
            data.add(new Item(u.getContact().get(i).getName(),u.getContact().get(i).getContactInfos().get(0).getNumber(),false,url));
        }
    }
    //返回数据给MyAdapter使用
    public  List<Item> getData(){
        return this.data;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        u=((UserParameter) getApplication()).getUser();
        index=new ArrayList<>();

        setContentView(R.layout.activity_delete);
        //获取listView
        lv_data= (ListView) findViewById(R.id.lv_data);
        //获取控件
        btn_delete= (Button) findViewById(R.id.btn_delete);
        che_all= (CheckBox) findViewById(R.id.che_all);
        tv_show=(TextView)findViewById(R.id.tv);
        //初始化数据源
        initdata();
        //实例化自定义适配器，把listview传到自定义适配器中
        myAdapter =new MyAdapter(this,lv_data);
        //绑定适配器
        lv_data.setAdapter(myAdapter);
        //初始化事件监听
        initlistener();
    }

    /**
     * 初始化事件监听方法
     */
    private void initlistener() {
        /**
         * 全选复选框设置事件监听
         */
        che_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (data.size()!=0) {//判断列表中是否有数据
                    if (isChecked) {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setChecked(true);

                        }
                        //通知适配器更新UI
                        myAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setChecked(false);
                        }
                        //通知适配器更新UI
                        myAdapter.notifyDataSetChanged();
                    }
                }else {//若列表中没有数据则隐藏全选复选框
                    che_all.setVisibility(View.GONE);
                }
            }
        });
        //删除按钮点击事件
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建一个要删除内容的集合，不能直接在数据源data集合中直接进行操作，否则会报异常
                List<Item> deleSelect = new ArrayList<Item>();

                //把选中的条目要删除的条目放在deleSelect这个集合中
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getChecked()) {
                        deleSelect.add(data.get(i));
                        index.add(i);
                    }
                }
                //判断用户是否选中要删除的数据及是否有数据
                if (deleSelect.size() != 0 && data.size() != 0) {
                    //从数据源data中删除数据
                    data.removeAll(deleSelect);
                    //把deleSelect集合中的数据清空
                    deleSelect.clear();
                    //把全选复选框设置为false
                    che_all.setChecked(false);
                    //通知适配器更新UI
                    myAdapter.notifyDataSetChanged();
                    delete();
                } else if (data.size() == 0) {
                    Toast.makeText(Delete.this, "没有要删除的数据", Toast.LENGTH_SHORT).show();
                } else if (deleSelect.size() == 0) {
                    Toast.makeText(Delete.this, "请选中要删除的数据", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void delete() {
        String id="";
        for(int i=0;i<index.size();i++){
            if(i==0)id=""+i;
            else  id=id+","+i;
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/Delete.ashx?Contact_ID="+ id)
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){//回调的方法执行在子线程。
                    if (response.body().string().equals("OK")) {
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }else{
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        });
    }
}


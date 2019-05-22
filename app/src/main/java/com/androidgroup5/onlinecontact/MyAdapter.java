package com.androidgroup5.onlinecontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private ListView lv_data;
    //定义一个数据源的引用
    private List<Item> data;
    private Context context;
    public MyAdapter(Context context,ListView lv_data) {
        if (context instanceof Delete) {
            this.context = context;
            this.lv_data=lv_data;
            data=((Delete)this.context).getData();
        }
    }

    /**
     * 获取当前子view的id（就是listview中的每一个条目的位置）
     * @param position
     * @return   返回当前id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取当前子view对应的值
     * @param position  当前子view（条目）的id（位置）
     * @return   返回当前对应的值 该值为object类型
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 定义coverView的Recyler(缓存)，该类名自定义的
     */
    class ViewHodler{
        TextView tvName;
        TextView tvPhone;
        CheckBox ch_delete;
    }
    /**
     * 核心代码
     * @param position  当前子view的id
     * @param convertView   缓存布局（该view与子view保持一致）
     * @param parent    父容器（即当前listview）
     * @return  返回当前子view（包含布局及具体的数据）
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //布局生成器(抽象类)
        LayoutInflater layoutInflater=LayoutInflater.from(this.context);
        //声明缓存
        ViewHodler viewHodler=null;
        //重新创建布局及缓存
        if (convertView==null){
            //创建缓存布局
            convertView=layoutInflater.inflate(R.layout.item_layout,parent,false);
            //产生缓存
            viewHodler=new ViewHodler();
            viewHodler.tvName=(TextView)convertView.findViewById(R.id.tv_name);
            viewHodler.tvPhone=(TextView)convertView.findViewById(R.id.tv_phone);
            viewHodler.ch_delete= (CheckBox) convertView.findViewById(R.id.ch_delete);
            //把缓存的布局放在converview中，避免重复获取布局，提升效率
            convertView.setTag(viewHodler);
        }else{
            //使用缓存的中的布局
            viewHodler= (ViewHodler) convertView.getTag();
        }
        //为缓存的布局ViewHodler控件设置新的数据
        Item currItem=data.get(position);
        viewHodler.tvName.setText(currItem.getName());
        viewHodler.tvPhone.setText(currItem.getPhone());
        viewHodler.ch_delete.setChecked(currItem.getChecked());

        //listView单个条目事件监听
        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHodler viewHodler= (ViewHodler) view.getTag();
                //切换条目上复选框的选中状态
                viewHodler.ch_delete.toggle();
                data.get(position).setChecked(viewHodler.ch_delete.isChecked());
                parent.getItemAtPosition(position);
            }
        });
        //返回多次生成的子View给适配器
        return convertView;
    }

    /**
     * 获取数据中要在listview中显示的条目
     * @return  返回数据的条目
     */
    @Override
    public int getCount() {
        return this.data!=null?this.data.size():0;
    }
}


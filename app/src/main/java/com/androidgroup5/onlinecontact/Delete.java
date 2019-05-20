package com.androidgroup5.onlinecontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Delete extends AppCompatActivity {
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        final Button btn = (Button) findViewById(R.id.button1);
        btn.setText("全选");
        btn.setTag(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = (boolean) btn.getTag();
                if (!flag) {
                    btn.setText("全选");
                    btn.setTag(true);
                } else {
                    btn.setText("全不选");
                    btn.setTag(false);
                }
            }
        });
        lv = (ListView) findViewById(R.id.list);
        String[] data = {
                "1",
                "2",
                "3",
                "4",
        };
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, data));
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}


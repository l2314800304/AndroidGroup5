package com.androidgroup5.onlinecontact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     * 
     * @param persons
     */
    public void add(List<sqlit> persons)
    {
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            for (sqlit person : persons)
            {
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(?, ?)", new Object[] { person.date,
                        person.isselct});
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }


    /**
     * query all persons, return list
     * 
     * @return List<Person>
     */
    public List<sqlit> query()
    {
        ArrayList<sqlit> persons = new ArrayList<sqlit>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
        	sqlit person = new sqlit();
            person.date = c.getString(c.getColumnIndex("date1"));
            person.isselct = c.getString(c.getColumnIndex("isselct"));
            persons.add(person);
        }
        c.close();
        return persons;
    }

    /**
     * 查询数据库中所有内容
     * 
     * @return Cursor
     */
    public Cursor queryTheCursor()
    {
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME,
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        // 释放数据库资源
        db.close();
    }

}

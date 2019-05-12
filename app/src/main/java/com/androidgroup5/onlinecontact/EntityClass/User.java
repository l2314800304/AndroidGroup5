package com.androidgroup5.onlinecontact.EntityClass;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int ID;
    private String UserName,Password,Sex,Location,Remark;
    private List<Contact> Contacts=new ArrayList<Contact>();
    private List<Record> Records=new ArrayList<Record>();

    public void User(String UserName,String Password){

    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public List<Contact> getContacts() {
        return Contacts;
    }

    public void setContacts(List<Contact> contacts) {
        Contacts = contacts;
    }

    public List<Record> getRecords() {
        return Records;
    }
    public List<Record> getRecordByContact(Contact recordContact) {
        List<Record> list=new ArrayList<Record>();
        for(int i=0;i<Records.size();i++){
            if (Records.get(i).getContact().equals(recordContact)){
                list.add(Records.get(i));
            }
        }
        return list;
    }

    public void setRecords(List<Record> records) {
        Records = records;
    }

}

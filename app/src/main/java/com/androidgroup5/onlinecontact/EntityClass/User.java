package com.androidgroup5.onlinecontact.EntityClass;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int ID;
    private String UserName,Password,Sex,Location,Remark;
    private List<Contact> Contact;
    private List<Record> Record;
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

    public List<Contact> getContact() {
        return Contact;
    }

    public void setContact(List<Contact> contacts) {
        Contact = contacts;
    }
    public List<Record> getRecord() {
        return Record;
    }
    public void setRecord(List<Record> records) {
        Record = records;
    }

}

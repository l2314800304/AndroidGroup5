package com.androidgroup5.onlinecontact.EntityClass;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Contact {
    private int ID;
    private String Name,Birthday;
    private String TelNum;
    private List<ContactInfo> ContactInfos=new ArrayList<ContactInfo>();


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public void setTelNum(String TelNum) {
        this.TelNum = TelNum;
    }

    public String getTelNum() {
        return TelNum;
    }

    public List<ContactInfo> getContactInfos() {
        return ContactInfos;
    }

    public void setContactInfos(List<ContactInfo> contactInfos) {
        ContactInfos = contactInfos;
    }
}

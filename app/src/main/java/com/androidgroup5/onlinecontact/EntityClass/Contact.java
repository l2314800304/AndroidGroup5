package com.androidgroup5.onlinecontact.EntityClass;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Contact {
    private int ID;
    private String Name,Birthday;
    private List<ContactInfo> Contact_Info=new ArrayList<ContactInfo>();


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

    public List<ContactInfo> getContact_Info() {
        return Contact_Info;
    }

    public void setContact_Info(List<ContactInfo> contactInfos) {
        Contact_Info = contactInfos;
    }
}

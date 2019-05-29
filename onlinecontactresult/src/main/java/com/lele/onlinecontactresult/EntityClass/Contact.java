package com.lele.onlinecontactresult.EntityClass;

import java.util.ArrayList;
import java.util.List;

public class Contact {
    private int ID;
    private String Name,Birthday;
    private List<ContactInfos> ContactInfos=new ArrayList<ContactInfos>();


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

    public List<ContactInfos> getContactInfos() {
        return ContactInfos;
    }

    public void setContactInfos(List<ContactInfos> contactInfos) {
        ContactInfos = contactInfos;
    }
}

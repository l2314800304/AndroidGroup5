package com.androidgroup5.onlinecontact.EntityClass;

public class ContactInfos {

    private int ID;
    private int EmailOrNumber;
    private String Number,Type;

    public int getEmailOrNumber() {
        return EmailOrNumber;
    }

    public void setEmailOrNumber(int emailOrNumber) {
        EmailOrNumber = emailOrNumber;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}

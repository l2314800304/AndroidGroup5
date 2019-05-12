package com.androidgroup5.onlinecontact.EntityClass;

public class ContactInfo {

    private int ID;
    private String Data,Type;

    public ContactInfo(int ID, String data, String type) {
        this.ID = ID;
        Data = data;
        Type = type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}

package com.androidgroup5.onlinecontact.EntityClass;

public class Record {
    private int ID;
    private int Type;
    private String Number;
    private String Time;
    private String Duration;
    private Contact RecordContact;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }


    public String getDuration() {
        return Duration;
    }
    public void setDuration(String duration) {
        Duration = duration;
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Contact getContact() {
        return RecordContact;
    }

    public void setContact(Contact recordContact) {
        RecordContact = recordContact;
    }
}

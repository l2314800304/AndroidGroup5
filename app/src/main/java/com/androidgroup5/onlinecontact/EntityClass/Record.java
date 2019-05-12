package com.androidgroup5.onlinecontact.EntityClass;

public class Record {
    private int ID;
    private String Number,Time;
    private Contact RecordContact;

    public Record(int ID, String number, String time) {
        this.ID = ID;
        Number = number;
        Time = time;
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

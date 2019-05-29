package com.lele.onlinecontactresult.EntityClass;

public class Record {
    private int ID;
    private String Type;
    private String Number;
    private String Date;
    private String Duration;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
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

    public String getDate() {
        return Date;
    }

    public void setDate(String time) {
        Date = time;
    }
}

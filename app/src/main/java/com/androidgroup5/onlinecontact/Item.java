package com.androidgroup5.onlinecontact;

public class Item {
    private String name;
    private String phone;
    private Boolean checked;

    public Item(String name, String phone,Boolean checked) {
        this.name = name;
        this.phone = phone;
        this.checked = checked;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}

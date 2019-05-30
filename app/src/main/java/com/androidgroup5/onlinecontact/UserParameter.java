package com.androidgroup5.onlinecontact;

import android.app.Application;

import com.androidgroup5.onlinecontact.EntityClass.User;

public class UserParameter extends Application {
    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        this.user = u;
    }

    User user;

    public User getLocal() {
        return local;
    }

    public void setLocal(User local) {
        this.local = local;
    }

    User local;

}

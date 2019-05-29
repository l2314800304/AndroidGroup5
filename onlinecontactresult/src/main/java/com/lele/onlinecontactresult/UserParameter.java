package com.lele.onlinecontactresult;

import android.app.Application;

import com.lele.onlinecontactresult.EntityClass.User;

public class UserParameter extends Application {
    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        this.user = u;
    }

    User user;
}

package com.hotel.models;

import javax.servlet.http.Cookie;

public class Session {
    public User user;
    public Cookie cookie;

    public Session(User user, Cookie cookie) {
        this.user = user;
        this.cookie = cookie;
    }
}

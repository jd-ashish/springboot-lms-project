package com.user.app.commons.utils;

import javax.servlet.http.Cookie;

import static com.user.app.commons.utils.Utils.getCurrentHttpRequest;
import static com.user.app.commons.utils.Utils.getCurrentHttpResponse;

public class Cookies {
    public static void makeCookies(String... cookies){

        Cookie loginCookie=new Cookie(cookies[0],
                EncodeDecode.encode(cookies[1]));
        loginCookie.setMaxAge(60 * 60 * 24); // 24 hrs

        getCurrentHttpResponse().addCookie(loginCookie);
    }
    public static Cookie getCookies(String... cookies){
        for (Cookie cookie : getCurrentHttpRequest().getCookies()) {
            if (cookie.getName().equals(cookies[0]))
                return cookie;
        }
        return null;
    }
}

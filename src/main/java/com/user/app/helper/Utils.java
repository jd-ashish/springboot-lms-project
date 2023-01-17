package com.user.app.helper;

public class Utils {
    public static String OTP(int max) {
        String otp = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        int len = otp.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = (int) (Math.random()*len);
            sb.append(otp.charAt(index));
        }
        return sb.substring(0,max);
    }
}

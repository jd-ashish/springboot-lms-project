package com.user.app.commons.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.user.app.commons.constants.AppConstants.OTPLength;

public class Utils {

    public static String OTP() {
        String OTP = "1234567890";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < OTPLength; i++) {
            int index = (int) (Math.random() * OTP.length());
            sb.append(OTP.charAt(index));
        }
        return sb.toString();
    }


    public static Map<String, String> ByteToMegabyte(String bytes, double max_kb) {
        double kilobytes = (Double.parseDouble(bytes) / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);
        Map<String, String> map = new HashMap<>();
        map.put("kb", String.valueOf(kilobytes));
        map.put("mb", String.valueOf(megabytes));
        map.put("gb", String.valueOf(gigabytes));
        System.out.println();

        if (max_kb == 0 || kilobytes <= max_kb) {
            map.put("message", null);
        } else {
            map.put("message", "Maximum file size is " + max_kb + " KB or " + (max_kb / 1024) + " MB");
        }
        return map;

    }

    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    public static HttpServletResponse getCurrentHttpResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getResponse();
        }
        return null;
    }

    public static String getAuthUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
//        anonymousUser ==>  default username
    }

    public static String ucFirst(String string) {
        String firstCharacterOfString = String.valueOf(string.charAt(0)).toUpperCase();
        return firstCharacterOfString.concat(string.substring(1));
    }

    public static String uri(String str, boolean isUpperCase) {
        String result = "";
        if (isUpperCase) {
            result += str.replace(" ", "-").replace("/","").toUpperCase();
        } else {
            result += str.replace(" ", "-").replace("/","").toLowerCase();
        }
        return result;
    }

    public static String backHeading(String title) {

        return "<ul class=\"list-inline\">" +
                "<li class=\"list-inline-item\">\n" +
                "                    <span data-feather=\"arrow-left\" onclick=\"history.back()\" class=\"c-p\"\n" +
                "        style=\"margin-top: -15px; width: 40px ; height: 40px;\"></span></li>" +
                "<li class=\"list-inline-item\"><h1>" +
                ucFirst(title) + "</h1></li>" +
                "</ul>";

    }

    public static Long isNullToX(Long num, Long x) {
        if (num == null) return x;
        return num;
    }

    public static int ABCDto1234(String s) {
        if ("a".equals(s)) {
            return 1;
        } else if ("b".equals(s)) {
            return 2;
        } else if ("c".equals(s)) {
            return 3;
        } else if ("d".equals(s)) {
            return 4;
        }
        return 5;
    }
    public static String[] split(String s, String split){
        return s.split(split);
    }

    public static int isWrong(String response  , String position, String correctAnswer){
        if(position.equals(response)){
            if(position.equals(correctAnswer)){
                return 1;
            }else{
                return 2;
            }
        }else {
            return 3;
        }
    }
    public static String timeAgo(Date currentDate, Date pastDate) {

        long milliSecPerMinute = 60 * 1000; //Milliseconds Per Minute
        long milliSecPerHour = milliSecPerMinute * 60; //Milliseconds Per Hour
        long milliSecPerDay = milliSecPerHour * 24; //Milliseconds Per Day
        long milliSecPerMonth = milliSecPerDay * 30; //Milliseconds Per Month
        long milliSecPerYear = milliSecPerDay * 365; //Milliseconds Per Year
        //Difference in Milliseconds between two dates
        long msExpired = currentDate.getTime() - pastDate.getTime();

        //Second or Seconds ago calculation
        if (msExpired < milliSecPerMinute) {
            if (Math.round(msExpired / 1000) == 1) {
                return String.valueOf(Math.round(msExpired / 1000)) + " second ago... ";
            } else {
                return String.valueOf(Math.round(msExpired / 1000) + " seconds ago...");
            }
        }

        //Minute or Minutes ago calculation
        else if (msExpired < milliSecPerHour) {
            String s = String.valueOf(Math.round(msExpired / milliSecPerMinute));
            if (Math.round(msExpired / milliSecPerMinute) == 1) {
                return s + " minute ago... ";
            } else {
                return s + " minutes ago... ";
            }
        }

        //Hour or Hours ago calculation
        else if (msExpired < milliSecPerDay) {
            String s = String.valueOf(Math.round(msExpired / milliSecPerHour));
            if (Math.round(msExpired / milliSecPerHour) == 1) {
                return s + " hour ago... ";
            } else {
                return s + " hours ago... ";
            }
        }

        //Day or Days ago calculation
        else if (msExpired < milliSecPerMonth) {
            String s = String.valueOf(Math.round(msExpired / milliSecPerDay));
            if (Math.round(msExpired / milliSecPerDay) == 1) {
                return s + " day ago... ";
            } else {
                return s + " days ago... ";
            }
        }
        //Month or Months ago calculation
        else if (msExpired < milliSecPerYear) {
            String s = String.valueOf(Math.round(msExpired / milliSecPerMonth));
            if (Math.round(msExpired / milliSecPerMonth) == 1) {
                return s + "  month ago... ";
            } else {
                return s + "  months ago... ";
            }
        }
        //Year or Years ago calculation
        else {
            String s = String.valueOf(Math.round(msExpired / milliSecPerYear));
            if (Math.round(msExpired / milliSecPerYear) == 1) {
                return s + " year ago...";
            } else {
                return s + " years ago...";
            }
        }

    }

    public static int isCurrentDate(Date date1 , Date date2) {
        if(date2==null) return 99;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date1).compareTo(formatter.format(date2));
    }
    public static String Role(String role){
        return String.valueOf(role.split("_")[1].charAt(0)).toUpperCase()
                + role.split("_")[1].substring(1).toLowerCase();
    }
}

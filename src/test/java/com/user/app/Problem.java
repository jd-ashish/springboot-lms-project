package com.user.app;

public class Problem {
    public static void main(String[] args) {
        String message = "myNameIsAshishKumar";
        for (String s : message.split("[A-Z]")) {
            System.out.println(s);
        }
    }
}

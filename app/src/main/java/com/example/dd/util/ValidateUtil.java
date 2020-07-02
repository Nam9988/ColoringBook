package com.example.dd.util;

import java.util.regex.Pattern;

public class ValidateUtil {
    public static boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean checkPassword(String password) {
        int length = password.length();
        boolean check = false;
        if (length >= 8 && length <= 16) {
            if (password.matches("(?=.*[0-9]).*")
                    && password.matches("(?=.*[a-z]).*")
                    && password.matches("(?=.*[A-Z]).*")) {
                check = true;
            }
        }
        return check;
    }
}

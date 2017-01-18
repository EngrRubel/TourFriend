package com.jkkniugmail.rubel.tourfriend.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by islan on 12/24/2016.
 */

public class Validation {

    public boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean isValidPhone(String phone) {
        Pattern pattern = Patterns.PHONE;
        return pattern.matcher(phone).matches();
    }

    public boolean isValidPassword(String password) {
        if (password.length() < 6)
            return false;
        else
            return true;
    }


}

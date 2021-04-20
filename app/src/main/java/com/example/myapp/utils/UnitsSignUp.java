package com.example.myapp.utils;

import android.text.TextUtils;

public class UnitsSignUp {

    public static boolean emailValidation(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean passwordValidation(CharSequence password) {
        return !TextUtils.isEmpty(password);
    }
}

package com.example.dd.util;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class TextUtil {
    public static String getValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getValue(TextInputEditText textInputEditText) {
        return Objects.requireNonNull(textInputEditText.getText()).toString().trim();
    }

    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager manager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(editText.getApplicationWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void handleError(TextView txtError, @StringRes int message, long timeDelay) {
        txtError.setText(message);
        txtError.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> txtError.setVisibility(View.GONE), timeDelay);
    }

    public static char getFirstCharUserName(String userName) {
        char result;
        String[] strings = userName.split(" ");
        if (strings.length == 1) {
            result = strings[0].toUpperCase().charAt(0);
        } else {
            result = strings[strings.length - 1].toUpperCase().charAt(0);
        }
        return result;
    }

    public static String displayDouble(double value){
        String sValue =  value + "";
        sValue = sValue.replace(".",",");
        if (!sValue.contains(",")){
            return sValue;
        }
        String[] arr = sValue.split(",");
        if (Integer.parseInt(arr[1]) == 0){
            return arr[0];
        }
        return sValue.replace(",",".");
    }
}




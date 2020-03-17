package com.appleto.findteacher.utils;

import android.content.Context;
import android.widget.EditText;


public class Validator {


    /*global field validator for this app*/
    public static final String EMAIL_VERIFICATION = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";


    /*this function validates a array of TextInputEditText */
    public static boolean validateInputField(EditText[] array, Context context) {
        int count = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i].getText().toString().length() == 0) {
                Utils.showToast(context, array[i].getTag().toString()+ " is empty!");
                break;
            } else {
                if ((array[i].getTag().toString().equals("Email"))) {
                    if (array[i].getText().toString().trim().matches(EMAIL_VERIFICATION)) {
                        count++;
                    } else {
                        Utils.showToast(context, array[i].getTag().toString() + " is invalid");
                        break;
                    }
                } else {
                    count++;
                }
            }
        }
        return array.length == count;
    }
}
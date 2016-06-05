package com.whale.nangua.pumpkingobang.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by nangua on 2016/6/5.
 */
public class AlertUtils {

    /**
     * @param context
     * @param mess
     */
    public static void toastMess(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
    }
}
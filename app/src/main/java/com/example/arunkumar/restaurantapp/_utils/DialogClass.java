package com.example.arunkumar.restaurantapp._utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Arun.Kumar on 3/27/2018.
 */

public class DialogClass {
    private static ProgressDialog progressDialog;

    public static void showDailog(Context mContext, String dialogMsg) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(dialogMsg);
        progressDialog.setTitle("Please Wait...");
//        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissDailog() {
        progressDialog.dismiss();
    }
}

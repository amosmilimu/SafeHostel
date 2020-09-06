package com.example.safehostel.constants;

import android.app.ProgressDialog;
import android.content.Context;

public class Constants {
    private static ProgressDialog dialog;

    public static void showProgressDialog(Context co){
        dialog = new ProgressDialog(co);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait...");
        dialog.show();
    }

    public static void cancelDialog(){
        dialog.dismiss();
    }
}

package com.example.safehostel.constants;

import android.app.ProgressDialog;
import android.content.Context;

public class Constants {
    private static ProgressDialog dialog;

    public static void showProgressDialog(Context co,String message){
        dialog = new ProgressDialog(co);
        dialog.setTitle(message);
        dialog.setMessage("Please wait...");
        dialog.show();
    }

    public static void cancelDialog(){
        dialog.dismiss();
    }
}

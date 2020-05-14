package com.example.servicedo.Config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import com.example.servicedo.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogConfig {

    private Context context;
    private int type;
    SweetAlertDialog pDialog;

    public DialogConfig(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void showDialog(String message){
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, type);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismiss();
            }
        });
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void showProgress(String title){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismissProgress(){
        if(pDialog != null){
            pDialog.dismiss();
        }
    }
}

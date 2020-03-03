package com.example.servicedo.Config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogConfig {

    private Context context;

    public DialogConfig(Context context) {
        this.context = context;
    }

    public void showAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

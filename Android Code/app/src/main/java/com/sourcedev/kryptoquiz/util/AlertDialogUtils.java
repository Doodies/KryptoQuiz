package com.sourcedev.kryptoquiz.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sourcedev.kryptoquiz.R;

public class AlertDialogUtils {

    public static void showTextDialogForOkay(Activity activity, String headerMessage, String footerMessage, final OkayDialog okayDialog) {
        LayoutInflater inflater = activity.getLayoutInflater();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        final View view = inflater.inflate(R.layout.dialog_text_okay, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        TextView tvHeader = view.findViewById(R.id.tvHeader);
        tvHeader.setText(headerMessage);
        TextView tvFooter = view.findViewById(R.id.tvFooter);
        tvFooter.setText(footerMessage);
        view.findViewById(R.id.okayTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(okayDialog!=null) {
                    okayDialog.onOkayClick();
                }
            }
        });
        alertDialog.show();
    }

    public interface OkayDialog {
        void onOkayClick();
    }
}

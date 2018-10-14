package com.sourcedev.kryptoquiz.util;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class myToast {
    /**
     *
     * @param context   Where the toast is going to be shown
     * @param text      Shouldn't be null, I'm avoiding type check
     * @param duration  Note duration is either Duration.SHORT or LONG, not in seconds
     */
    public static void makeText(Context context, String text, int duration){

        // Best way to resolve this custom font issue
        if(context != null) {
            Toast toast = Toast.makeText(context, "    " + text + "    ", duration);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setGravity(Gravity.CENTER);
            messageTextView.setTextSize(18); // 18 sp is the standard size
            toast.show();
        }
    }
}
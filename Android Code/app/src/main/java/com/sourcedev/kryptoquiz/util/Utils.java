package com.sourcedev.kryptoquiz.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sourcedev.kryptoquiz.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {

    public static Snackbar snackbar;
    public static void darkenStatusBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            activity.getWindow().setStatusBarColor(darkenColor(ContextCompat.getColor(activity, color)));
    }

    /**
     * Code to darken the color supplied (mostly color of toolbar)
     * Use hsv[2] *= 0.8f to darken status bar a bit
     */
    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return Color.HSVToColor(hsv);
    }

    public static Boolean isValidString(String string) {
        if(string == null || string.trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }
    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        String token = prefs.getString(Constants.ACCESS_TOKEN, null);
        return token;
    }

    public static Boolean isUserLoggedIn(Context context) {
        String token = SharedPrefrencesHelper.getInstance().getString(Constants.USER_TOKEN);
        if (isValidString(token)) {
            return true;
        }
        return false;
    }

    public static void animateView(final View view, final int toVisibility, float toAlpha, int duration) {
        boolean show = toVisibility == View.VISIBLE;
        if (show) {
            view.setAlpha(0);
        }
        view.setVisibility(View.VISIBLE);
        view.animate()
                .setDuration(duration)
                .alpha(show ? toAlpha : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(toVisibility);
                    }
                });
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSnackBarForError(final Context ctx ,String message , View view) {
        snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        setSnackBarColor(ctx, R.color.snackbar_message);
        snackbar.show();
    }

    public static void setSnackBarColor(Context ctx, int snackBarBgColorId){
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);

        // Changing snackbar background color
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(ContextCompat.getColor(ctx, snackBarBgColorId));

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        TextView snackbarActionTextView = (TextView) sbView.findViewById( android.support.design.R.id.snackbar_action );
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

    }

    public static void nextFragmentWithLeftRightAnim(FragmentManager fragmentManager, Fragment fragment, int fragContainerResID) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        fragmentTransaction.replace(fragContainerResID, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    public static Boolean isValidEmail(String email) {
        if (!isValidString(email)) {
            return false;
        }
        email = email.trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Boolean result = email.matches(emailPattern) ? true : false;
        String emailPattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
        Boolean result1 = email.matches(emailPattern1) ? true : false;
        return (result || result1);
    }
}

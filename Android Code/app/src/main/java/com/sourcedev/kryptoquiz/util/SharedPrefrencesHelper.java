package com.sourcedev.kryptoquiz.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrencesHelper {

    private static final String SETTINGS_NAME = "quizPrefs";
    private static SharedPrefrencesHelper sSharedPrefs;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private boolean mBulkUpdate = false;

    private SharedPrefrencesHelper(Context context) {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }


    public static SharedPrefrencesHelper getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new SharedPrefrencesHelper(context.getApplicationContext());
        }
        return sSharedPrefs;
    }

    public static SharedPrefrencesHelper getInstance() {
        if (sSharedPrefs != null) {
            return sSharedPrefs;
        }
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");
    }

    public void put(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommit();
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public String getString(String key) {
        return mPref.getString(key, null);
    }

    private void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }

}

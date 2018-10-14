package com.sourcedev.kryptoquiz.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sourcedev.kryptoquiz.activities.QuizHomeActivity;
import com.sourcedev.kryptoquiz.util.Constants;

import org.json.JSONObject;

import java.util.Map;

public class quizFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = quizFirebaseMessagingService.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void onNewToken(String token) {
        pref = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(Constants.ACCESS_TOKEN, token);
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            //        //Getting the message from the bundle
            String quizId = object.getString("quizId");
            String quizStartedAt = object.getString("quizStartDT");
            Intent intent = new Intent(this, QuizHomeActivity.class);
            intent.putExtra("quizId", quizId);
            intent.putExtra("quizStartDT", quizStartedAt);
            Log.e(TAG, "onMessageReceived: " + quizId + "  " + quizStartedAt);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "onMessageReceived: exception raise");
        }
        Log.e(TAG, "onMessageReceived: " + remoteMessage );
    }
}

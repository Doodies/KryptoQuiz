package com.sourcedev.kryptoquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sourcedev.kryptoquiz.activities.BaseActivity;
import com.sourcedev.kryptoquiz.activities.OnBoardingActivity;
import com.sourcedev.kryptoquiz.activities.QuizActivity;
import com.sourcedev.kryptoquiz.util.Constants;
import com.sourcedev.kryptoquiz.util.SharedPrefrencesHelper;
import com.sourcedev.kryptoquiz.util.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPrefrencesHelper sharedPrefrencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefrencesHelper = SharedPrefrencesHelper.getInstance(this);
        Log.e(TAG, "onCreate: "+Utils.getToken(this) );
        pref = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        if(!pref.getBoolean("token", false)){
            storeFirebaseToken();
        }
        checkLogin();
    }

    private void storeFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        editor = pref.edit();
                        editor.putString(Constants.ACCESS_TOKEN, token);
                        editor.putBoolean("token", true);
                        editor.apply();

                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkLogin() {
        Intent intent;
        if (Utils.isUserLoggedIn(this)) {
//            intent = new Intent(this, BaseActivity.class);
            intent = new Intent(this, QuizActivity.class);
//            intent.putExtra("quizId", "2");
//
        } else {
            intent = new Intent(this, QuizActivity.class);
        }
        startActivity(intent);
    }
}

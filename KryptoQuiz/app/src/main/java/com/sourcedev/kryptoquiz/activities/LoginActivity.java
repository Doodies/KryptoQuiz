package com.sourcedev.kryptoquiz.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.UI.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.add(R.id.loginFragmentContainer, loginFragment);
        ft.addToBackStack(loginFragment.toString());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (fm != null) {
            int count = fm.getBackStackEntryCount();
            if (count != 1) {
                fm.popBackStack();
            } else {
                startActivity(new Intent(LoginActivity.this, OnBoardingActivity.class));
                finish();
            }
        }
    }
}
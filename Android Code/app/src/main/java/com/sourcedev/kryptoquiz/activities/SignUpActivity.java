package com.sourcedev.kryptoquiz.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.UI.LoginFragment;
import com.sourcedev.kryptoquiz.UI.SignUpFragment;

public class SignUpActivity extends AppCompatActivity {

    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SignUpFragment signupFragment = new SignUpFragment();
        ft.add(R.id.signupFragmentContainer, signupFragment);
        ft.addToBackStack(signupFragment.toString());
        ft.commit();
    }
}

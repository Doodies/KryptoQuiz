package com.sourcedev.kryptoquiz.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.activities.BaseActivity;
import com.sourcedev.kryptoquiz.models.GeneralResponse;
import com.sourcedev.kryptoquiz.network.ApiCalls;
import com.sourcedev.kryptoquiz.util.Constants;
import com.sourcedev.kryptoquiz.util.Utils;
import com.sourcedev.kryptoquiz.util.myToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    @BindView(R.id.heading_signup)
    TextView headingSignup;
    @BindView(R.id.first_name)
    AppCompatEditText firstName;
    @BindView(R.id.last_name)
    AppCompatEditText lastName;
    @BindView(R.id.user_email)
    AppCompatEditText userEmail;
    @BindView(R.id.linear_layout)
    LinearLayout llMain;

    private View rootView;
    private String mobileNumber;

    public static SignUpFragment newInstance(String mobileNumber) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("number", mobileNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, rootView);
//        mobileNumber = getArguments().getString("number");
        showKeyboard();
        return rootView;
    }


    private void showKeyboard() {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        firstName.requestFocus();
    }

    @OnClick(R.id.button_sign_up)
    public void onSignUpButtonClick() {
        onClickSignUpButtonView();
    }


    public void onClickSignUpButtonView() {
        String first= firstName.getText().toString();
        String last = lastName.getText().toString();
        String email = userEmail.getText().toString().trim();
        if (first.length() < 3) {
            myToast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT);
        } else if (last.length() < 3) {
            myToast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT);
        } else if (!Utils.isValidEmail(email)) {
            if (email == null || email.equals("")) {
                myToast.makeText(getContext(), "Empty email address", Toast.LENGTH_LONG);
            } else {
                myToast.makeText(getContext(), "Invalid email address", Toast.LENGTH_LONG);
            }
        } else {
            signUpApiCall(first, last, email);
        }
    }

    private void signUpApiCall(String first, String last, String email) {
        ApiCalls.postSignUpDetail(first, last, email).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(final Call<GeneralResponse> call, final Response<GeneralResponse> response) {
                if (!response.isSuccessful()) {
                    Utils.showSnackBarForError(getActivity(), "Error ", llMain);
                } else {
                    handleTheResponse(response.body());
                }
            }

            @Override
            public void onFailure(final Call<GeneralResponse> call, final Throwable t) {
                try {
                    if (t instanceof IOException) {
                        Utils.showSnackBarForError(getActivity(), getResources().getString(R.string.no_data_found_internet_failed), llMain);
                    } else {
                        Utils.showSnackBarForError(getActivity(), getResources().getString(R.string.something_went_wrong), llMain);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleTheResponse(GeneralResponse body) {
        if (body.getStatus()) {
            Intent intent = new Intent(getActivity(), BaseActivity.class);
            startActivity(intent);
        }
    }

}



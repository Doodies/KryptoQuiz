package com.sourcedev.kryptoquiz.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcedev.kryptoquiz.MainActivity;
import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.activities.BaseActivity;
import com.sourcedev.kryptoquiz.activities.SignUpActivity;
import com.sourcedev.kryptoquiz.models.GeneralResponse;
import com.sourcedev.kryptoquiz.models.VerifyOtpResponse;
import com.sourcedev.kryptoquiz.network.ApiCalls;
import com.sourcedev.kryptoquiz.util.Constants;
import com.sourcedev.kryptoquiz.util.SharedPrefrencesHelper;
import com.sourcedev.kryptoquiz.util.Utils;
import com.sourcedev.kryptoquiz.util.myToast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpFragment extends Fragment implements AppCompatEditText.OnEditorActionListener{
    @BindView(R.id.editTextOtp)
    AppCompatEditText etOtp;
    @BindView(R.id.progress_overlay)
    View progressOverlay;
    @BindView(R.id.progress_text)
    TextView progressText;
    @BindView(R.id.progressView)
    ProgressBar progressView;
    @BindView(R.id.rlMain)
    RelativeLayout rlMain;
    @BindView(R.id.otp_layout)
    RelativeLayout otpLayout;
    @BindView(R.id.headingLogin)
    TextView headingOtp;
    @BindView(R.id.verificationFragment)
    LinearLayout verificationFragmentLayout;

    private HashMap<String, String> otpDetailsMap = new HashMap<>();
    private String mobileNumber;
    private boolean isFromSignup = false;
    String otp;

    public OtpFragment() {
    }

    public static OtpFragment newInstance(HashMap<String, String> signUpDetailsMap , String mobileNumber , boolean isFromSignup) {
        OtpFragment fragment = new OtpFragment();
        Bundle args = new Bundle();
        args.putSerializable("map", signUpDetailsMap);
        args.putString("number", mobileNumber);
        args.putBoolean("is_from_signup", isFromSignup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        ButterKnife.bind(this, view);
        mobileNumber = getArguments().getString("number");
        isFromSignup = getArguments().getBoolean("is_from_signup");
        otpDetailsMap = (HashMap<String, String>) getArguments().getSerializable("map");
        headingOtp.setText("Login using the OTP sent to "+ mobileNumber);
        showKeyboard();
        return view;
    }

    @OnClick(R.id.buttonLogin)
    public void onButtonLoginClick() {
        Utils.hideKeyboard(getActivity());
        otp = etOtp.getText().toString();
        if (otp.isEmpty()) {
            myToast.makeText(getActivity(), "Number field empty!", Toast.LENGTH_LONG);
        } else {
            showProgress("Logging In...");
            verifyOTPApiCall(otpDetailsMap);
        }
    }

    @OnClick(R.id.loginFailure)
    public void onResendOtpEvent() {
        showProgress("Requesting OTP...");
        requestOTPApiCall(mobileNumber);
    }

    private HashMap<String, String> makeOtpDetailsMap(String otp) {
        if (otpDetailsMap == null) {
            otpDetailsMap = new HashMap<>();
        }
        otpDetailsMap.put("phone", mobileNumber);
        otpDetailsMap.put("otp", otp);
        return otpDetailsMap;
    }


    private void verifyOTPApiCall(HashMap<String, String> otpDetailsMap) {
        ApiCalls.postVerifyOTPCall(mobileNumber, otp).enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(final Call<VerifyOtpResponse> call, final Response<VerifyOtpResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    System.out.println("Verify OTP Successful");
                    setDataOnSuccessfulResponse(response.body());
                } else {
                    try {
                        String resp = response.errorBody().string();
                        JSONObject jsonObject = null;
                        jsonObject = (new JSONObject(resp)).getJSONObject("error");
                        String error = jsonObject.getString("message");
                        myToast.makeText(getContext(), error, Toast.LENGTH_SHORT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(final Call<VerifyOtpResponse> call, final Throwable t) {
                hideProgress();
                try {
                    if (t instanceof IOException) {
                        Utils.showSnackBarForError(getActivity(), getResources().getString(R.string.no_data_found_internet_failed), rlMain);
                    } else {
                        Utils.showSnackBarForError(getActivity(), getResources().getString(R.string.something_went_wrong), rlMain);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDataOnSuccessfulResponse(VerifyOtpResponse body) {
        SharedPrefrencesHelper.getInstance().put(Constants.USER_TOKEN, body.getUserToken());
        Log.e("setDataResponse: ", body.getUserToken());
        Intent intent;
        if (body.getNewUser()) {
            intent = new Intent(getActivity(), SignUpActivity.class);
        } else {
            intent = new Intent(getActivity(), BaseActivity.class);
        }
        startActivity(intent);
    }

    private void requestOTPApiCall(String mobileNumber) {
        ApiCalls.postGenerateOTPCall(mobileNumber, Utils.getToken(getContext())).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(final Call<GeneralResponse> call, final Response<GeneralResponse> response) {
                if (!response.isSuccessful()){
                    Utils.showSnackBarForError(getActivity(), "Error ",  rlMain);
                }
                hideProgress();
            }

            @Override
            public void onFailure(final Call<GeneralResponse> call, final Throwable t) {
                hideProgress();
                try {
                    if (t instanceof IOException) {
                        Utils.showSnackBarForError(getActivity(), getResources().getString(R.string.no_data_found_internet_failed), rlMain);
                    } else {
                        Utils.showSnackBarForError(getActivity(), getResources().getString(R.string.something_went_wrong), rlMain);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void navigateToMainActivity(){
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    private void showKeyboard() {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        etOtp.requestFocus();
    }

    public void showProgress(String message) {
        Utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
        progressText.setText(message);
    }

    public void hideProgress() {
        Utils.animateView(progressOverlay, View.GONE, 0, 200);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}

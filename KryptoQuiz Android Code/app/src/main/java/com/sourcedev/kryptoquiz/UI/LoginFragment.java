package com.sourcedev.kryptoquiz.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.models.GeneralResponse;
import com.sourcedev.kryptoquiz.network.ApiCalls;
import com.sourcedev.kryptoquiz.util.Utils;
import com.sourcedev.kryptoquiz.util.myToast;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements AppCompatEditText.OnEditorActionListener {

    @BindView(R.id.buttonLogin)
    AppCompatButton buttonLogin;
    @BindView(R.id.editTextNumber)
    AppCompatEditText etMobileNumber;
    @BindView(R.id.progress_overlay)
    View progressOverlay;
    @BindView(R.id.progress_text)
    TextView progressText;
    @BindView(R.id.progressView)
    ProgressBar progressView;
    @BindView(R.id.rlMain)
    LinearLayout llMain;

    private View rootView;
    private static final String TAG = "###";

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);
        etMobileNumber.setOnEditorActionListener(this);
        return rootView;
    }

    @OnClick(R.id.buttonLogin)
    public void onLoginButtonClick() {
        Utils.hideKeyboard(getActivity());
        String number = etMobileNumber.getText().toString();
        if (number.isEmpty())
            myToast.makeText(getContext(), "Number field empty!", Toast.LENGTH_SHORT);
        else {
            if (number.length() != 10)
                myToast.makeText(getContext(), "Invalid Number", Toast.LENGTH_LONG);
            else {
                showProgress("Requesting OTP...");
                requestOTPApiCall(number);
            }
        }
    }

    private void requestOTPApiCall(final String mobileNumber) {
        ApiCalls.postGenerateOTPCall(mobileNumber, Utils.getToken(getContext())).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(final Call<GeneralResponse> call, final Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    boolean isExistingUser = response.body().getStatus();
                    if (getActivity() != null && isAdded()) {
                        if (isExistingUser) {
                            Utils.nextFragmentWithLeftRightAnim(getFragmentManager(), OtpFragment.newInstance(null, mobileNumber, false), R.id.loginFragmentContainer);
                        } else {
                            Utils.nextFragmentWithLeftRightAnim(getFragmentManager(), SignUpFragment.newInstance(mobileNumber), R.id.loginFragmentContainer);
                        }
                    }
                } else {
                    try {
                        String resp = response.errorBody().string();
                        JSONObject jsonObject = (new JSONObject(resp)).getJSONObject("error");
                        String error = jsonObject.getString("message");
                        myToast.makeText(getContext(), error, Toast.LENGTH_SHORT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                hideProgress();
            }

            @Override
            public void onFailure(final Call<GeneralResponse> call, final Throwable t) {
                hideProgress();
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            buttonLogin.performClick();
            Utils.hideKeyboard(getActivity());
            return true;
        }
        return false;
    }

    public void showProgress(String message) {
        Utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
        Utils.animateView(progressView, View.VISIBLE, 0.9f, 200);
        Utils.animateView(progressText, View.VISIBLE, 0.9f, 200);
        progressText.setText(message);
    }

    public void hideProgress() {
        Utils.animateView(progressOverlay, View.GONE, 0, 200);
        Utils.animateView(progressView, View.GONE, 0, 200);
        Utils.animateView(progressText, View.GONE, 0, 200);
    }
}
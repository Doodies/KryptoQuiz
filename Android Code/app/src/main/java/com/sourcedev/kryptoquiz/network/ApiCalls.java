package com.sourcedev.kryptoquiz.network;

import com.sourcedev.kryptoquiz.models.GeneralResponse;
import com.sourcedev.kryptoquiz.models.QuestionResponse;
import com.sourcedev.kryptoquiz.models.User;
import com.sourcedev.kryptoquiz.models.VerifyOtpResponse;

import java.util.List;

import retrofit2.Call;

public class ApiCalls {

    public static Call<GeneralResponse> postGenerateOTPCall(String phone, String firebaseId) {
        return ApiServices.getSparsApiServices().postGenerateOTP(phone, firebaseId);
    }

    public static Call<VerifyOtpResponse> postVerifyOTPCall(String phone, String otp) {
        return ApiServices.getSparsApiServices().postVerifyOTP(phone, otp);
    }

    public static Call<User> getUserDetail() {
        return ApiServices.getApiServiceWithToken().getUserDetails();
    }

    public static Call<GeneralResponse> postSignUpDetail(String first, String last, String email) {
        return ApiServices.getApiServiceWithToken().postSignUp(first, last, email);
    }

    public static Call<List<QuestionResponse> > getQuestions(String quizId) {
        return ApiServices.getApiServiceWithToken().getQuestion(quizId);
    }



}

package com.sourcedev.kryptoquiz.network;

import com.sourcedev.kryptoquiz.models.GeneralResponse;
import com.sourcedev.kryptoquiz.models.QuestionResponse;
import com.sourcedev.kryptoquiz.models.User;
import com.sourcedev.kryptoquiz.models.VerifyOtpResponse;
import com.sourcedev.kryptoquiz.models.submitQuizResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

class ApiServices {

    private static KrpytoService kryptoService;
    private static KrpytoService kryptoService1;


    static synchronized KrpytoService getSparsApiServices() {
        if (kryptoService == null) {
            kryptoService = ApiClients.getApiClient().create(KrpytoService.class);
        }
        return kryptoService;
    }

    static synchronized KrpytoService getApiServiceWithToken() {
        if (kryptoService1 == null) {
            kryptoService1 = ApiClients.getApiClientToken().create(KrpytoService.class);
        }
        return kryptoService1;
    }

    public interface KrpytoService {

        @FormUrlEncoded
        @POST("/api/auth/sendOtp")
        Call<GeneralResponse> postGenerateOTP(@Field("phone") String phoneNumber, @Field("firebaseId") String firebaseId);

        @FormUrlEncoded
        @POST("/api/auth/login")
        Call<VerifyOtpResponse> postVerifyOTP(@Field("phone") String phoneNumber, @Field("otp") String otp);

        @FormUrlEncoded
        @POST("/api/secure/submitDetails")
        Call<GeneralResponse> postSignUp(@Field("firstName") String first, @Field("lastName") String last,
                                         @Field("email") String email);

        @GET("/api/secure/getDetails")
        Call<User> getUserDetails();


        @GET("/api/secure/getQuizQues")
        Call< List<QuestionResponse> > getQuestion(@Query("quizId") String quizId);

        @FormUrlEncoded
        @POST("/api/secure/submitQuiz")
        Call<submitQuizResponse> postSumbitQuiz(@Field("quizId") String quizId, @Field("correctCount") Integer count);

    }
}
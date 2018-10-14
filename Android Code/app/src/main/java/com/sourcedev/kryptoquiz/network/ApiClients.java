package com.sourcedev.kryptoquiz.network;

import com.sourcedev.kryptoquiz.adapters.GeneralOTPJsonResponseAdapter;
import com.sourcedev.kryptoquiz.adapters.QuestionResponseAdapter;
import com.sourcedev.kryptoquiz.adapters.UserDetailJsonAdapter;
import com.sourcedev.kryptoquiz.adapters.VerifyOtpResponseJsonAdapter;
import com.sourcedev.kryptoquiz.models.GeneralResponse;
import com.sourcedev.kryptoquiz.util.Constants;
import com.sourcedev.kryptoquiz.util.SharedPrefrencesHelper;
import com.sourcedev.kryptoquiz.util.Utils;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.sourcedev.kryptoquiz.util.Constants.API_BASE_URL;

public class ApiClients {

    private static Retrofit apiClient;
    private static Boolean shouldUpdateHeader = false;

    public static synchronized Retrofit getApiClient() {
        if (apiClient == null || shouldUpdateHeader) {
            shouldUpdateHeader = false;
            OkHttpClient client = getOKHttpBuilder().build();
            Moshi.Builder builder = new Moshi.Builder();
            registerMoshiAdapters(builder);
            Moshi moshi = builder.build();
            apiClient = new Retrofit
                    .Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .client(client)
                    .build();
        }
        return apiClient;
    }

    public static synchronized Retrofit getApiClientToken() {
        Retrofit apiClient2;
        OkHttpClient client = getOKHttpBuilderToken().build();
        Moshi.Builder builder = new Moshi.Builder();
        registerMoshiAdapters(builder);
        Moshi moshi = builder.build();
        apiClient2 = new Retrofit
                .Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(client)
                .build();
        return apiClient2;
    }

    private static void registerMoshiAdapters(Moshi.Builder builder) {
        builder.add(new VerifyOtpResponseJsonAdapter());
        builder.add(new GeneralOTPJsonResponseAdapter());
        builder.add(new UserDetailJsonAdapter());
        builder.add(new QuestionResponseAdapter());
    }

    private static OkHttpClient.Builder getOKHttpBuilder() {
        OkHttpClient.Builder builder;
        builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

        // adding http interceptor for logging

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        final String token = SharedPrefrencesHelper.getInstance().getString(Constants.USER_TOKEN, null);

        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request;
                if (token != null) {
                    request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("jwt_token", token)
                            .addHeader("app_version", String.valueOf(Constants.appVersion))
                            .build();
                } else {
                    request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("app_version", String.valueOf(Constants.appVersion))
                            .build();
                }
                return chain.proceed(request);
            }
        });

        return builder;
    }


    private static OkHttpClient.Builder getOKHttpBuilderToken() {
        OkHttpClient.Builder builder;
        builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

        // adding http interceptor for logging

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        final String token = SharedPrefrencesHelper.getInstance().getString(Constants.USER_TOKEN, null);

        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request;
                request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("jwt-token", token)
                            .addHeader("app_version", String.valueOf(Constants.appVersion))
                            .build();
                return chain.proceed(request);
            }
        });

        return builder;
    }
}

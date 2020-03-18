package com.anotherdev.firebase.auth.rest.api;

import android.content.Context;

import com.anotherdev.firebase.auth.data.Data;
import com.f2prateek.rx.preferences2.Preference;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class RestAuthInterceptor implements Interceptor {

    private final Preference<String> apiKey;


    RestAuthInterceptor(Context context) {
        apiKey = Data.from(context).getApiKey();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        HttpUrl newUrl = originalRequest.url()
                .newBuilder()
                .addQueryParameter("key", apiKey.get())
                .build();

        Request newRequest = originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept-Encoding", "identity")
                .url(newUrl)
                .build();

        return chain.proceed(newRequest);
    }
}

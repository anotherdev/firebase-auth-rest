package com.anotherdev.firebase.auth.rest.api;

import com.anotherdev.firebase.auth.init.AppContext;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.anotherdev.firebase.auth.core.BuildConfig.DEBUG;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BASIC;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public final class RestAuthApi {

    private final IdentityToolkitApi auth;
    private final SecureTokenApi token;


    private RestAuthApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new RestAuthInterceptor(AppContext.get()))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(DEBUG ? BODY : BASIC))
                .build();

        auth = buildApi(okHttpClient, IdentityToolkitApi.BASE_URL, IdentityToolkitApi.class);
        token = buildApi(okHttpClient, SecureTokenApi.BASE_URL, SecureTokenApi.class);
    }

    private <T> T buildApi(OkHttpClient client, String url, Class<T> apiClass) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(apiClass);
    }


    private static class RestAuthApiHolder {
        private static final RestAuthApi INSTANCE = new RestAuthApi();
    }

    public static IdentityToolkitApi auth() {
        return RestAuthApiHolder.INSTANCE.auth;
    }

    public static SecureTokenApi token() {
        return RestAuthApiHolder.INSTANCE.token;
    }
}

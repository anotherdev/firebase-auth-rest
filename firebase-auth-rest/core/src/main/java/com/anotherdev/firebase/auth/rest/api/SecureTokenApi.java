package com.anotherdev.firebase.auth.rest.api;

import com.anotherdev.firebase.auth.rest.api.model.ExchangeTokenRequest;
import com.anotherdev.firebase.auth.rest.api.model.ExchangeTokenResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SecureTokenApi {

    // Workaround colon issue. https://github.com/square/retrofit/issues/3080
    String BASE_URL = "https://securetoken.googleapis.com/";


    @POST("v1/token")
    Single<ExchangeTokenResponse> exchangeToken(@Body ExchangeTokenRequest request);
}

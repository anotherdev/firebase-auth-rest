package com.anotherdev.firebase.auth.rest.api;

import com.anotherdev.firebase.auth.rest.api.model.SignInAnonymouslyRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInAnonymouslyResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IdentityToolkitApi {

    // Workaround colon issue. https://github.com/square/retrofit/issues/3080
    String BASE_URL = "https://identitytoolkit.googleapis.com/";


    @POST("v1/accounts:signUp")
    Single<SignInAnonymouslyResponse> signInAnonymously(@Body SignInAnonymouslyRequest request);
}
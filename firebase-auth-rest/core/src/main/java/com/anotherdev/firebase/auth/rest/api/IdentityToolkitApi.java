package com.anotherdev.firebase.auth.rest.api;

import com.anotherdev.firebase.auth.rest.api.model.IdTokenRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInAnonymouslyRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInResponse;
import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IdentityToolkitApi {

    // Workaround colon issue. https://github.com/square/retrofit/issues/3080
    String BASE_URL = "https://identitytoolkit.googleapis.com/";


    @POST("v1/accounts:signUp")
    Single<SignInResponse> signInAnonymously(@Body SignInAnonymouslyRequest request);

    @POST("v1/accounts:signInWithIdp")
    Single<SignInResponse> signInWithCredential(@Body JsonObject request);

    @POST("v1/accounts:lookup")
    Single<JsonObject> getAccounts(@Body IdTokenRequest request);
}

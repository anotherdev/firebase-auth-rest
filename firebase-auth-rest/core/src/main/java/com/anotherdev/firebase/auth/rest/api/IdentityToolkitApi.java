package com.anotherdev.firebase.auth.rest.api;

import com.anotherdev.firebase.auth.SignInResponse;
import com.anotherdev.firebase.auth.UserProfileChangeRequest;
import com.anotherdev.firebase.auth.rest.api.model.IdTokenRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithEmailPasswordRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithIdpRequest;
import com.anotherdev.firebase.auth.rest.api.model.UserPasswordChangeRequest;
import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IdentityToolkitApi {

    // Workaround colon issue. https://github.com/square/retrofit/issues/3080
    String BASE_URL = "https://identitytoolkit.googleapis.com/";


    @POST("v1/accounts:signUp")
    Single<SignInResponse> signInAnonymously(@Body SignInRequest request);

    @POST("v1/accounts:signUp")
    Single<SignInResponse> createUserWithEmailAndPassword(@Body SignInWithEmailPasswordRequest request);

    @POST("v1/accounts:signInWithPassword")
    Single<SignInResponse> signInWithEmailAndPassword(@Body SignInWithEmailPasswordRequest request);

    @POST("v1/accounts:signInWithIdp")
    Single<SignInResponse> signInWithCredential(@Body SignInWithIdpRequest request);

    @POST("v1/accounts:signInWithCustomToken")
    Single<SignInResponse> signInWithCustomToken(@Body SignInRequest request);

    @POST("v1/accounts:lookup")
    Single<JsonObject> getAccounts(@Body IdTokenRequest request);

    @POST("v1/accounts:update")
    Single<SignInResponse> updateProfile(@Body UserProfileChangeRequest request);

    @POST("v1/accounts:update")
    Single<SignInResponse> updatePassword(@Body UserPasswordChangeRequest request);
}

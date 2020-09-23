package com.anotherdev.firebase.auth.rest.api;

import com.anotherdev.firebase.auth.SignInResponse;
import com.anotherdev.firebase.auth.UserProfileChangeRequest;
import com.anotherdev.firebase.auth.UserProfileChangeResponse;
import com.anotherdev.firebase.auth.rest.api.model.GetAccountInfoRequest;
import com.anotherdev.firebase.auth.rest.api.model.GetAccountInfoResponse;
import com.anotherdev.firebase.auth.rest.api.model.SendPasswordResetEmailRequest;
import com.anotherdev.firebase.auth.rest.api.model.SendPasswordResetEmailResponse;
import com.anotherdev.firebase.auth.rest.api.model.SignInRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithCustomTokenRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithEmailPasswordRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithIdpRequest;
import com.anotherdev.firebase.auth.rest.api.model.UnlinkProviderRequest;
import com.anotherdev.firebase.auth.rest.api.model.UserEmailChangeRequest;
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
    Single<SignInResponse> signInWithCustomToken(@Body SignInWithCustomTokenRequest request);

    @POST("v1/accounts:sendOobCode")
    Single<SendPasswordResetEmailResponse> sendPasswordResetEmail(@Body SendPasswordResetEmailRequest request);

    @POST("v1/accounts:lookup")
    Single<GetAccountInfoResponse> getAccounts(@Body GetAccountInfoRequest request);

    @POST("v1/accounts:update")
    Single<UserProfileChangeResponse> updateProfile(@Body UserProfileChangeRequest request);

    @POST("v1/accounts:update")
    Single<SignInResponse> updateEmail(@Body UserEmailChangeRequest request);

    @POST("v1/accounts:update")
    Single<SignInResponse> updatePassword(@Body UserPasswordChangeRequest request);

    @POST("v1/accounts:update")
    Single<JsonObject> unlink(@Body UnlinkProviderRequest request);
}

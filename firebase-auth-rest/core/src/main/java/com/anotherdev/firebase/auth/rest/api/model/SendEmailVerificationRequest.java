package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SendEmailVerificationRequest extends IdTokenRequest {

    @Value.Default
    @SerializedName("requestType")
    default String getRequestType() {
        return "VERIFY_EMAIL";
    }


    static ImmutableSendEmailVerificationRequest.Builder builder() {
        return ImmutableSendEmailVerificationRequest.builder();
    }
}

package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.value.Value;

public interface SignInRequest {

    @Value.Default
    @SerializedName("returnSecureToken")
    default boolean returnSecureToken() {
        return true;
    }
}

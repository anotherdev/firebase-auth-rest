package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

public interface IdTokenRequest {

    @SerializedName("idToken")
    String getIdToken();
}

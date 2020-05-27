package com.anotherdev.firebase.auth.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
class FirebaseAuthData {

    @SerializedName("identities")
    FirebaseAuthIdentities identities = new FirebaseAuthIdentities();

    @SerializedName("sign_in_provider")
    String signInProvider = "unknown";


    FirebaseAuthData() {
    }

    @NonNull
    public FirebaseAuthIdentities getIdentities() {
        return identities;
    }

    @NonNull
    public String getSignInProvider() {
        return signInProvider;
    }
}

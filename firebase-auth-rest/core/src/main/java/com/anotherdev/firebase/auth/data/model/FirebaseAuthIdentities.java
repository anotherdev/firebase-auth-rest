package com.anotherdev.firebase.auth.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("WeakerAccess")
class FirebaseAuthIdentities {

    @SerializedName("google.com")
    List<String> google = Collections.emptyList();

    @SerializedName("facebook.com")
    List<String> facebook = Collections.emptyList();

    @SerializedName("email")
    List<String> email = Collections.emptyList();


    @NonNull
    public List<String> getGoogle() {
        return google;
    }

    @NonNull
    public List<String> getFacebook() {
        return facebook;
    }

    @NonNull
    public List<String> getEmail() {
        return email;
    }

    public boolean isEmpty() {
        return google.isEmpty()
                && facebook.isEmpty()
                && email.isEmpty();
    }
}

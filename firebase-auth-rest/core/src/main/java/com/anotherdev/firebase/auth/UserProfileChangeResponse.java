package com.anotherdev.firebase.auth;

@SuppressWarnings("WeakerAccess")
public class UserProfileChangeResponse {

    String localId;
    String email;
    String displayName;


    public String getLocalId() {
        return localId;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }
}

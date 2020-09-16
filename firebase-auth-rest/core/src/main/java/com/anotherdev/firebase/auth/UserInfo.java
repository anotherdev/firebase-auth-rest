package com.anotherdev.firebase.auth;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface UserInfo {

    @SerializedName("rawId")
    String getUid();

    @Value.Default
    @SerializedName("displayName")
    default String getDisplayName() {
        return "";
    }

    @Value.Default
    @SerializedName("email")
    default String getEmail() {
        return "";
    }

    @Value.Default
    @SerializedName("photoUrl")
    default String getPhotoUrl() {
        return "";
    }

    @Value.Default
    @SerializedName("providerId")
    default String getProviderId() {
        return "";
    }

    @Value.Default
    @SerializedName("isEmailVerified")
    default boolean isEmailVerified() {
        return true;
    }
}

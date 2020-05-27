package com.anotherdev.firebase.auth.data.model;

import com.anotherdev.firebase.auth.UserInfo;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface UserProfile {

    @SerializedName("localId")
    String getLocalId();

    @SerializedName("email")
    String getEmail();

    @SerializedName("emailVerified")
    boolean isEmailVerified();

    @SerializedName("disabled")
    boolean isDisabled();

    @SerializedName("customAuth")
    boolean isCustomAuth();

    @SerializedName("displayName")
    String getDisplayName();

    @SerializedName("providerUserInfo")
    List<UserInfo> providerUserInfo();

    @SerializedName("photoUrl")
    String getPhotoUrl();

    @SerializedName("validSince")
    String getValidSince();

    @SerializedName("lastLoginAt")
    String getLastLoginAt();

    @SerializedName("createdAt")
    String getCreatedAt();

    @SerializedName("lastRefreshAt")
    String getLastRefreshAt();
}

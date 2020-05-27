package com.anotherdev.firebase.auth.data.model;

import com.anotherdev.firebase.auth.UserInfo;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.Collections;
import java.util.List;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface UserProfile {

    @Value.Default
    @SerializedName("localId")
    default String getLocalId() {
        return "";
    }

    @Value.Default
    @SerializedName("email")
    default String getEmail() {
        return "";
    }

    @Value.Default
    @SerializedName("emailVerified")
    default boolean isEmailVerified() {
        return false;
    }

    @Value.Default
    @SerializedName("disabled")
    default boolean isDisabled() {
        return false;
    }

    @Value.Default
    @SerializedName("customAuth")
    default boolean isCustomAuth() {
        return false;
    }

    @Value.Default
    @SerializedName("displayName")
    default String getDisplayName() {
        return "";
    }

    @Value.Default
    @SerializedName("providerUserInfo")
    default List<UserInfo> providerUserInfo() {
        return Collections.emptyList();
    }

    @Value.Default
    @SerializedName("photoUrl")
    default String getPhotoUrl() {
        return "";
    }

    @Value.Default
    @SerializedName("validSince")
    default String getValidSince() {
        return "";
    }

    @Value.Default
    @SerializedName("lastLoginAt")
    default String getLastLoginAt() {
        return "";
    }

    @Value.Default
    @SerializedName("createdAt")
    default String getCreatedAt() {
        return "";
    }

    @Value.Default
    @SerializedName("lastRefreshAt")
    default String getLastRefreshAt() {
        return "";
    }


    static ImmutableUserProfile.Builder builder() {
        return ImmutableUserProfile.builder();
    }
}

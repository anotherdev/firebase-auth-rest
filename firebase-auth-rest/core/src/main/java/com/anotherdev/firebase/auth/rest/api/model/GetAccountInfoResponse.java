package com.anotherdev.firebase.auth.rest.api.model;

import com.anotherdev.firebase.auth.data.model.UserProfile;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface GetAccountInfoResponse {

    @SerializedName("users")
    List<UserProfile> getUsers();
}

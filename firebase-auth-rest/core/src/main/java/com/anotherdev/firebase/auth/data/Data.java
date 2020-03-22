package com.anotherdev.firebase.auth.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.util.FarGson;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;

public class Data {

    private static final Gson GSON = FarGson.get();

    private final SharedPreferences sharedPreferences;
    private final RxSharedPreferences rxSharedPreferences;


    private Data(Context context) {
        sharedPreferences = context.getSharedPreferences(Data.class.getName(), Context.MODE_PRIVATE);
        rxSharedPreferences = RxSharedPreferences.create(sharedPreferences);
    }

    public Preference<String> getApiKey() {
        return rxSharedPreferences.getString("google_api_key", "");
    }

    public Preference<FirebaseUser> getCurrentUser(@NonNull FirebaseUser defaultValue) {
        return rxSharedPreferences.getObject(
                "current_user_info",
                defaultValue,
                new Preference.Converter<FirebaseUser>() {
                    @NonNull
                    @Override
                    public FirebaseUser deserialize(@NonNull String json) {
                        return GSON.fromJson(json, FirebaseUser.class);
                    }

                    @NonNull
                    @Override
                    public String serialize(@NonNull FirebaseUser user) {
                        return GSON.toJson(user);
                    }
                });
    }

    public static Data from(Context context) {
        return new Data(context);
    }
}

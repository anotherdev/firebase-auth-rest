package com.anotherdev.firebase.auth.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.data.model.FirebaseUserImpl;
import com.anotherdev.firebase.auth.data.model.UserProfile;
import com.anotherdev.firebase.auth.init.AppContext;
import com.anotherdev.firebase.auth.util.FarGson;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;

public class Data {

    private static final Gson GSON = FarGson.get();

    private static class DataHolder {
        private static final SharedPreferences SHARED_PREFERENCES;
        private static final RxSharedPreferences RX_SHARED_PREFERENCES;

        static {
            Context context = AppContext.get();
            SHARED_PREFERENCES = context.getSharedPreferences(Data.class.getName(), Context.MODE_PRIVATE);
            RX_SHARED_PREFERENCES = RxSharedPreferences.create(SHARED_PREFERENCES);
        }
    }


    private Data(Context context) {
    }

    public Preference<String> getApiKey() {
        return DataHolder.RX_SHARED_PREFERENCES.getString("google_api_key", "");
    }

    public Preference<FirebaseUserImpl> getCurrentUser(@NonNull FirebaseUserImpl defaultValue) {
        return DataHolder.RX_SHARED_PREFERENCES.getObject(
                "current_user_info",
                defaultValue,
                new RxPreferenceGsonConverter<>(FirebaseUserImpl.class));
    }

    public Preference<UserProfile> getUserProfile(@NonNull final String uid) {
        return DataHolder.RX_SHARED_PREFERENCES.getObject(
                uid,
                UserProfile.builder().localId(uid).build(),
                new RxPreferenceGsonConverter<>(UserProfile.class));
    }


    public static Data from(Context context) {
        return new Data(context);
    }
}

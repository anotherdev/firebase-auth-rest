package com.anotherdev.firebase.auth.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

public class Data {

    private final SharedPreferences sharedPreferences;
    private final RxSharedPreferences rxSharedPreferences;


    private Data(Context context) {
        sharedPreferences = context.getSharedPreferences(Data.class.getName(), Context.MODE_PRIVATE);
        rxSharedPreferences = RxSharedPreferences.create(sharedPreferences);
    }

    public Preference<String> getApiKey() {
        return rxSharedPreferences.getString("google_api_key", "");
    }

    public static Data from(Context context) {
        return new Data(context);
    }
}

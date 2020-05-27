package com.anotherdev.firebase.auth.data;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.util.FarGson;
import com.f2prateek.rx.preferences2.Preference;
import com.google.gson.Gson;

class RxPreferenceGsonConverter<T> implements Preference.Converter<T> {

    private final Gson GSON = FarGson.get();
    private final Class<T> valueType;


    RxPreferenceGsonConverter(Class<T> valueType) {
        this.valueType = valueType;
    }

    @NonNull
    @Override
    public T deserialize(@NonNull String json) {
        return GSON.fromJson(json, valueType);
    }

    @NonNull
    @Override
    public String serialize(@NonNull T value) {
        return GSON.toJson(value);
    }
}

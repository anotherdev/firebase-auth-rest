package com.anotherdev.firebase.auth.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import java.util.ServiceLoader;

import timber.log.Timber;

public class FarGson {

    private static final Gson GSON = builder().create();


    public static GsonBuilder builder() {
        GsonBuilder builder = new GsonBuilder();
        for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
            Timber.d("registerTypeAdapterFactory: %s", factory);
            builder.registerTypeAdapterFactory(factory);
        }
        return builder;
    }

    public static Gson get() {
        return GSON;
    }
}

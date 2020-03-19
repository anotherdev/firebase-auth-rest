package com.anotherdev.firebase.auth.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FarGson {

    private static final Gson GSON = builder().create();


    public static GsonBuilder builder() {
        return new GsonBuilder();
    }

    public static Gson get() {
        return GSON;
    }
}

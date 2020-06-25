package com.anotherdev.firebase.auth.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Strings {

    private Strings() {}


    @NonNull
    public static String nullToEmpty(@Nullable String string) {
        return (string == null) ? "" : string;
    }
}

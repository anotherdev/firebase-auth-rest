package com.anotherdev.sample.firebase.auth.intent;

import android.content.Context;
import android.content.Intent;

import com.anotherdev.sample.firebase.auth.BuildConfig;

public abstract class BaseIntent extends Intent {

    public static final String ACTION = BuildConfig.APPLICATION_ID + ".intent.action.";
    public static final String EXTRA = BuildConfig.APPLICATION_ID + ".intent.extra.";


    protected BaseIntent(String action) {
        super(action);
    }

    protected BaseIntent(Context context, Class<?> cls) {
        super(context, cls);
    }
}

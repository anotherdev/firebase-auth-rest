package com.anotherdev.sample.firebase.auth;

import android.util.Log;

import io.reactivex.functions.Consumer;

class Rx2Util {

    private static final String TAG = Rx2Util.class.getSimpleName();


    static final Consumer<Throwable> ON_ERROR_LOG = t -> Log.e(TAG, t.getMessage(), t);
}

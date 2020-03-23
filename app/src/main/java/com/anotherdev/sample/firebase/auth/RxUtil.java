package com.anotherdev.sample.firebase.auth;

import android.util.Log;

class RxUtil {

    private static final String TAG = RxUtil.class.getSimpleName();


    static final io.reactivex.rxjava3.functions.Consumer<Throwable> ON_ERROR_LOG_V3 = t -> Log.e(TAG, t.getMessage(), t);

    static final io.reactivex.functions.Consumer<Throwable> ON_ERROR_LOG_V2 = t -> Log.e(TAG, t.getMessage(), t);
}

package com.anotherdev.firebase.auth.util;

import timber.log.Timber;

public interface RxUtil {

    io.reactivex.rxjava3.functions.Consumer<Throwable> ON_ERROR_LOG_V3 = Timber::e;

    io.reactivex.functions.Consumer<Throwable> ON_ERROR_LOG_V2 = Timber::e;
}

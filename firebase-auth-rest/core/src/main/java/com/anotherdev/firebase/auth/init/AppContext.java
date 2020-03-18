package com.anotherdev.firebase.auth.init;

import android.content.Context;

import com.google.firebase.components.Preconditions;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.AsyncProcessor;

public class AppContext {

    private static final AsyncProcessor<Context> APP_CONTEXT = AsyncProcessor.create();


    public static synchronized void init(Context context) {
        Preconditions.checkNotNull(context, "context is null");
        APP_CONTEXT.onNext(context.getApplicationContext());
        APP_CONTEXT.onComplete();
    }

    public static Flowable<Context> getFlowable() {
        return APP_CONTEXT.hide();
    }

    public static Context get() {
        return APP_CONTEXT.getValue();
    }


    private AppContext() { /* NO INSTANCE */ }
}

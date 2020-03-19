package com.anotherdev.firebase.auth.init;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.anotherdev.firebase.auth.core.BaseInitProvider;
import com.anotherdev.firebase.auth.core.R;
import com.anotherdev.firebase.auth.data.Data;

import timber.log.Timber;

public class InternalFirebaseAuthRestInitProvider extends BaseInitProvider {

    @SuppressLint("LogNotTimber")
    @Override
    public boolean onCreate() {
        try {
            Timber.plant(new Timber.DebugTree());
            Context appContext = getAppContext();
            AppContext.init(appContext);

            final String googleApiKey = appContext.getString(R.string.google_api_key);
            Timber.i("google_api_key: %s", googleApiKey);

            Data.from(appContext)
                    .getApiKey()
                    .set(googleApiKey);
            return true;
        } catch (Exception e) {
            Log.e(InternalFirebaseAuthRestInitProvider.class.getSimpleName(), e.getMessage(), e);
            return false;
        }
    }
}

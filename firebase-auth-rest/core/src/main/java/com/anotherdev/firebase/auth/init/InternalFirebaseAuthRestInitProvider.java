package com.anotherdev.firebase.auth.init;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.RestAuthTokenRefresher;
import com.anotherdev.firebase.auth.core.BaseActivityLifecycleCallbacks;
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

            registerTokenRefresher(appContext);
            return true;
        } catch (Exception e) {
            Log.e(InternalFirebaseAuthRestInitProvider.class.getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    private void registerTokenRefresher(Context context) {
        if (context instanceof Application) {
            ((Application) context).registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                    if (activity instanceof LifecycleOwner) {
                        LifecycleOwner owner = (LifecycleOwner) activity;
                        for (RestAuthTokenRefresher ratr : FirebaseAuthRest.getTokenRefreshers()) {
                            ratr.bindTo(owner);
                        }
                    }
                }
            });
        }
    }
}

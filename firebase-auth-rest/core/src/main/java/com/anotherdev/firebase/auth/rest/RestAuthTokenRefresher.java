package com.anotherdev.firebase.auth.rest;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.anotherdev.firebase.auth.data.model.FirebaseUserImpl;
import com.google.firebase.auth.internal.IdTokenListener;
import com.google.firebase.internal.InternalTokenResult;

import timber.log.Timber;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class RestAuthTokenRefresher implements IdTokenListener, LifecycleObserver {

    private static final long REFRESH_PERIOD = MILLISECONDS.convert(10, MINUTES);
    private static final long MIN_RETRY_BACKOFF = MILLISECONDS.convert(30, SECONDS);
    private static final long MAX_RETRY_BACKOFF = MILLISECONDS.convert(5, MINUTES);

    private final Handler handler = new Handler();
    @NonNull
    private final RestAuthProvider auth;

    private String ownerName;

    private long failureRetryTimeMillis = MIN_RETRY_BACKOFF;
    private String lastToken;


    public RestAuthTokenRefresher(@NonNull RestAuthProvider auth) {
        this.auth = auth;
    }

    @NonNull
    RestAuthProvider getAuth() {
        return auth;
    }

    @Override
    public void onIdTokenChanged(@NonNull InternalTokenResult result) {
        final String newToken = result.getToken();
        if (newToken != null && lastToken == null) {
            // We are now signed in, time to start refreshing
            Timber.d("Token changed from null --> non-null, starting refreshing");
            handler.post(refreshRunnable);
        }

        if (newToken == null && lastToken != null) {
            // The user signed out, stop all refreshing
            Timber.d("Signed out, canceling refreshes.");
            handler.removeCallbacksAndMessages(null);
        }

        lastToken = newToken;
    }

    public void bindTo(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);
        ownerName = owner.toString();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onLifecycleStarted() {
        Timber.d("onLifecycleStarted(): %s", ownerName);
        auth.addIdTokenListener(this);
        handler.post(refreshRunnable);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onLifecycleStopped() {
        Timber.d("onLifecycleStopped(): %s", ownerName);
        this.auth.removeIdTokenListener(this);
        this.handler.removeCallbacksAndMessages(null);
        this.lastToken = null;
    }


    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            FirebaseUserImpl user = auth.getCurrentUser();
            if (user == null) {
                Timber.d("User signed out, nothing to refresh.");
                return;
            }

            final long timeRemainingMillis = 1000 * user.expiresInSeconds();
            final long diff = timeRemainingMillis - REFRESH_PERIOD;

            if (diff > 0) {
                Timber.d("Token expires in %s, scheduling refresh in %s seconds.",
                        timeRemainingMillis / 1000, diff / 1000);
                handler.postDelayed(refreshRunnable, diff);
                return;
            }

            // Time to refresh the token now
            Timber.d("Token expires in %s, refreshing token now!", timeRemainingMillis / 1000);
            auth.getAccessToken(true)
                    .addOnSuccessListener(result -> {
                        Timber.d("Token refreshed successfully.");
                        handler.post(refreshRunnable);
                        // Clear the failure backoff
                        failureRetryTimeMillis = MIN_RETRY_BACKOFF;
                    })
                    .addOnFailureListener(e -> {
                        Timber.e(e, "Failed to refresh token.");
                        Timber.d("Retrying in %s...", failureRetryTimeMillis);
                        // Retry and double the backoff time (up to the max)
                        handler.postDelayed(refreshRunnable, failureRetryTimeMillis);
                        failureRetryTimeMillis = Math.min(failureRetryTimeMillis * 2, MAX_RETRY_BACKOFF);
                    });
        }
    };
}

package com.anotherdev.firebase.auth;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.google.android.gms.tasks.OnFailureListener;
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
    private final FirebaseAuth auth;

    private long failureRetryTimeMillis = MIN_RETRY_BACKOFF;
    private String lastToken;


    public RestAuthTokenRefresher(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void onIdTokenChanged(@NonNull InternalTokenResult result) {
    }

    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            FirebaseUser user = auth.getCurrentUser();
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
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Timber.e(e, "Failed to refresh token.");
                            Timber.d("Retrying in %s...", failureRetryTimeMillis);
                            // Retry and double the backoff time (up to the max)
                            handler.postDelayed(refreshRunnable, failureRetryTimeMillis);
                            failureRetryTimeMillis = Math.min(failureRetryTimeMillis * 2, MAX_RETRY_BACKOFF);
                        }
                    });
        }
    };
}

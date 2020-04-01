package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.facebook.CallbackManager;
import com.google.firebase.FirebaseApp;

import butterknife.BindView;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.internal.functions.Functions;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.auth_sign_in_anonymously_button) Button signInAnonymouslyButton;
    @BindView(R.id.auth_facebook_button) Button facebookLoginButton;
    @BindView(R.id.auth_sign_in_with_facebook_button) Button signInWithFacebookButton;
    @BindView(R.id.auth_logout_button) Button logoutButton;

    private final CallbackManager facebookCallbackManager = CallbackManager.Factory.create();


    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setResult(RESULT_OK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth firebaseAuth = FirebaseAuthRest.getInstance(FirebaseApp.getInstance());
        setupSignInAnonymouslyButton(firebaseAuth);
        setupSignInWithFacebookButton(firebaseAuth);
        setupLogoutButton(firebaseAuth);
    }

    private void setupSignInAnonymouslyButton(FirebaseAuth firebaseAuth) {
        setupButton(firebaseAuth,
                signInAnonymouslyButton,
                v -> {
                    v.setEnabled(false);
                    onDestroy.add(firebaseAuth.signInAnonymously()
                            .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3));
                },
                auth -> signInAnonymouslyButton.setEnabled(!auth.isSignedIn()));
    }

    private void setupSignInWithFacebookButton(FirebaseAuth firebaseAuth) {
        setupButton(firebaseAuth,
                signInWithFacebookButton,
                v -> {
                    v.setEnabled(false);
                    facebookLoginButton.performClick();
                },
                auth -> signInWithFacebookButton.setEnabled(!auth.isSignedIn()));
    }

    private void setupLogoutButton(FirebaseAuth firebaseAuth) {
        setupButton(firebaseAuth,
                logoutButton,
                v -> firebaseAuth.signOut(),
                auth -> logoutButton.setEnabled(auth.isSignedIn()));
    }

    private void setupButton(@NonNull FirebaseAuth firebaseAuth,
                             @NonNull Button button,
                             @NonNull View.OnClickListener onClick,
                             @NonNull Consumer<FirebaseAuth> authStateConsumer) {
        button.setOnClickListener(onClick);
        onDestroy.add(firebaseAuth.authStateChanges()
                .subscribe(authStateConsumer, RxUtil.ON_ERROR_LOG_V3));
    }


    private final Consumer<?> ON_NEXT_FINISH = new Consumer<Object>() {
        @Override
        public void accept(Object o) throws Throwable {
            finish();
        }
    };
}

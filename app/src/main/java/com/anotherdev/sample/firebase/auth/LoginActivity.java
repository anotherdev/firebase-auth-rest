package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.google.firebase.FirebaseApp;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.auth_sign_in_anonymously_button) Button signInAnonymouslyButton;
    @BindView(R.id.auth_logout_button) Button logoutButton;


    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuthRest.getInstance(FirebaseApp.getInstance());

        setupLogoutButton(auth);
    }


    private void setupLogoutButton(FirebaseAuth auth) {
        logoutButton.setOnClickListener(v -> auth.signOut());

        onDestroy.add(auth.authStateChanges()
                .subscribe());
    }
}

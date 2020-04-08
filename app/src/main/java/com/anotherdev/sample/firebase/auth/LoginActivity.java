package com.anotherdev.sample.firebase.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.anotherdev.firebase.auth.AuthError;
import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.anotherdev.firebase.auth.provider.FacebookAuthProvider;
import com.anotherdev.firebase.auth.provider.GoogleAuthProvider;
import com.anotherdev.firebase.auth.util.RxUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.florent37.inlineactivityresult.rx.RxInlineActivityResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import butterknife.BindView;
import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.internal.functions.Functions;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.auth_sign_in_anonymously_button) Button signInAnonymouslyButton;
    @BindView(R.id.auth_register_email_button) Button registerEmailButton;
    @BindView(R.id.auth_facebook_button) LoginButton facebookLoginButton;
    @BindView(R.id.auth_sign_in_with_facebook_button) Button signInWithFacebookButton;
    @BindView(R.id.auth_sign_in_with_google_button) Button signInWithGoogleButton;
    @BindView(R.id.auth_logout_button) Button logoutButton;

    private final CallbackManager facebookCallbackManager = CallbackManager.Factory.create();
    private GoogleSignInClient googleSignInClient;


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

        googleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth firebaseAuth = FirebaseAuthRest.getInstance(FirebaseApp.getInstance());
        setupSignInAnonymouslyButton(firebaseAuth);
        setupRegisterEmailButton(firebaseAuth);
        setupSignInWithFacebookButton(firebaseAuth);
        setupSignInWithGoogleButton(firebaseAuth);
        setupLogoutButton(firebaseAuth);
    }

    private void setupSignInAnonymouslyButton(FirebaseAuth firebaseAuth) {
        setupButton(firebaseAuth,
                signInAnonymouslyButton,
                v -> onDestroy.add(firebaseAuth.signInAnonymously()
                        .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3)),
                auth -> signInAnonymouslyButton.setEnabled(!auth.isSignedIn()));
    }

    private void setupRegisterEmailButton(FirebaseAuth firebaseAuth) {
        setupButton(firebaseAuth,
                registerEmailButton,
                v ->  new EmailPasswordDialog(this)
                        .onConfirm((email, password) -> onDestroy.add(firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnError(e -> {
                                    dialog(e);
                                    registerEmailButton.setEnabled(true);
                                })
                                .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3)))
                        .show(),
                auth -> registerEmailButton.setEnabled(!auth.isSignedIn()));
    }

    private void setupSignInWithFacebookButton(FirebaseAuth firebaseAuth) {
        facebookLoginButton.setPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                String oAuthRedirectUri = "https://huawei-dtse.firebaseapp.com/__/auth/handler";
                AuthCredential credential = FacebookAuthProvider.getCredential(token, oAuthRedirectUri);
                onDestroy.add(firebaseAuth.signInWithCredential(credential)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(e -> {
                            toast(e.getMessage());
                            LoginManager.getInstance().logOut();
                        })
                        .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3));
            }
            @Override public void onCancel() { toast("Facebook Login canceled"); }
            @Override public void onError(FacebookException error) { toast(error.getMessage()); }
        });
        setupButton(firebaseAuth,
                signInWithFacebookButton,
                v -> facebookLoginButton.performClick(),
                auth -> {
                    boolean isLoggedOut = !auth.isSignedIn();
                    signInWithFacebookButton.setEnabled(isLoggedOut);
                    if (isLoggedOut) {
                        LoginManager.getInstance().logOut();
                    }
                });
    }

    private void setupSignInWithGoogleButton(FirebaseAuth firebaseAuth) {
        setupButton(firebaseAuth,
                signInWithGoogleButton,
                v -> onDestroy.add(RxJavaBridge
                        .toV3Disposable(new RxInlineActivityResult(this)
                                .request(googleSignInClient.getSignInIntent())
                                .map(result -> {
                                    Intent data = result.getData();
                                    return GoogleSignIn.getSignedInAccountFromIntent(data)
                                            .getResult();
                                })
                                .flatMapSingle(account -> {
                                    String token = account.getIdToken();
                                    AuthCredential credential = GoogleAuthProvider.getCredential(token);
                                    return RxJavaBridge.toV2Single(firebaseAuth.signInWithCredential(credential));
                                })
                                .doOnError(e -> {
                                    toast(e.getMessage());
                                    googleSignInClient.signOut();
                                })
                                .subscribe(io.reactivex.internal.functions.Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V2))),
                auth -> {
                    boolean isLoggedOut = !auth.isSignedIn();
                    signInWithGoogleButton.setEnabled(isLoggedOut);
                    if (isLoggedOut) {
                        googleSignInClient.signOut();
                    }
                });
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
        button.setOnClickListener(v -> {
            v.setEnabled(false);
            onClick.onClick(v);
        });
        onDestroy.add(firebaseAuth.authStateChanges()
                .subscribe(authStateConsumer, RxUtil.ON_ERROR_LOG_V3));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void dialog(Throwable e) {
        AuthError ae = AuthError.fromThrowable(e);
        new LovelyInfoDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setTopTitle("Error")
                .setTopTitleColor(getResources().getColor(android.R.color.white))
                .setMessage(String.format("code: %s\nmessage: %s\ncause: %s", ae.getCode(), ae.getMessage(), ae.getCause()))
                .show();
    }

    private void toast(Throwable e) {
        toast(AuthError.fromThrowable(e).toString());
    }

    private void toast(String text) {
        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_LONG).show();
    }
}

package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.util.FarGson;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class PhoneLoginActivity extends BaseActivity {

    @BindView(R.id.phone_number_tfb) TextFieldBoxes phoneNumberTextFieldBoxes;
    @BindView(R.id.phone_number_eet) ExtendedEditText phoneNumberEditText;

    @BindView(R.id.sms_code_tfb) TextFieldBoxes smsCodeTextFieldBoxes;
    @BindView(R.id.sms_code_eet) ExtendedEditText smsCodeEditText;

    String verificationId;

    FirebaseAuthJsSdk firebaseAuthJsSdk;

    @BindView(R.id.webview_container) ViewGroup webViewContainer;


    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_phone_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Phone Login");

        firebaseAuthJsSdk = new FirebaseAuthJsSdk(this);

        webViewContainer.addView(firebaseAuthJsSdk.getWebView());

        phoneNumberTextFieldBoxes.getEndIconImageButton()
                .setOnClickListener(v -> {
                    String phoneNumber = phoneNumberEditText.getText().toString();
                    PhoneAuthProvider.getInstance()
                            .verifyPhoneNumber(phoneNumber, 2, TimeUnit.MINUTES, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential c) {
                                    String msg = String.format("Provider: %s, method: %s, sms code: %s", c.getProvider(), c.getSignInMethod(), c.getSmsCode());
                                    Log.d(TAG, msg);
                                    originalSignInWithCredential(c);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    verificationId = null;
                                    Log.e(TAG, e.getMessage(), e);
                                    dialog(e);
                                }

                                @Override
                                public void onCodeSent(@NonNull String vid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    verificationId = vid;
                                    String msg = String.format("verificationId: %s, forceResendingToken: %s", vid, forceResendingToken);
                                    Log.d(TAG, msg);
                                    dialog(TAG, msg);
                                }
                            });
                });

        smsCodeTextFieldBoxes.getEndIconImageButton()
                .setOnClickListener(v -> {
                    String vid = verificationId;
                    String smsCode = smsCodeEditText.getText().toString();
                    if (!TextUtils.isEmpty(vid) && !TextUtils.isEmpty(smsCode)) {
                        originalSignInWithCredential(PhoneAuthProvider.getCredential(vid, smsCode));
                    } else {
                        String error = String.format("Verification Id and SMS Code cannot be null or empty.\nvid: %s\nsms code: %s", vid, smsCode);
                        Log.e(TAG, error);
                        dialog(TAG, error);
                    }
                });
    }

    private void originalSignInWithCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> Log.e(TAG, FarGson.get().toJson(authResult)))
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, e.getMessage(), e);
                    dialog(e);
                });
    }

    private interface VipPhoneAuth {
    }
}

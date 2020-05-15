package com.anotherdev.sample.firebase.auth;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.anotherdev.firebase.auth.SignInResponse;

import butterknife.BindView;
import io.reactivex.rxjava3.core.Single;

@SuppressWarnings("WeakerAccess")
public class EmailPasswordDialog extends MultipleTextInputDialog {

    public interface OnConfirmClickListener {
        Single<SignInResponse> onConfirmClick(String email, String password);
    }

    @BindView(R.id.email_edittext) EditText emailEditText;
    @BindView(R.id.password_edittext) EditText passwordEditText;

    private OnConfirmClickListener confirmClickListener;


    public EmailPasswordDialog(Context context) {
        super(context);
        setIcon(android.R.drawable.ic_dialog_email);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.view_email_password_input;
    }

    @Override
    public EmailPasswordDialog setListener(int viewId, boolean dismissOnClick, View.OnClickListener listener) {
        super.setListener(viewId, dismissOnClick, listener);
        return this;
    }

    @Override
    public EmailPasswordDialog setConfirmButtonText(int confirmTextRes) {
        super.setConfirmButtonText(confirmTextRes);
        return this;
    }

    public EmailPasswordDialog onConfirm(OnConfirmClickListener listener) {
        confirmClickListener = listener;
        return this;
    }

    @Override
    protected void dispatchOnConfirmClick() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (confirmClickListener != null) {
            confirmClickListener.onConfirmClick(email, password);
        }
    }
}

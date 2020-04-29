package com.anotherdev.sample.firebase.auth;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.anotherdev.firebase.auth.SignInResponse;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import io.reactivex.rxjava3.core.Single;

@SuppressWarnings("WeakerAccess")
public class EmailPasswordDialog extends LovelyCustomDialog {

    public interface OnConfirmClickListener {
        Single<SignInResponse> onConfirmClick(String email, String password);
    }

    private TextView positiveButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    private OnConfirmClickListener confirmClickListener;


    public EmailPasswordDialog(Context context) {
        super(context);
        setTopColorRes(R.color.colorPrimary);
        setIcon(android.R.drawable.ic_dialog_email);
        setView(R.layout.view_email_password_input);
        configureView(dialog -> {
            positiveButton = dialog.findViewById(R.id.positive_button);
            emailEditText = dialog.findViewById(R.id.email_edittext);
            passwordEditText = dialog.findViewById(R.id.password_edittext);
        });
        super.setListener(R.id.positive_button, true, button -> dispatchOnConfirmClick());
    }

    @Override
    public EmailPasswordDialog setListener(int viewId, boolean dismissOnClick, View.OnClickListener listener) {
        super.setListener(viewId, dismissOnClick, v -> {
            dispatchOnConfirmClick();
            listener.onClick(v);
        });
        return this;
    }

    public EmailPasswordDialog setConfirmButtonText(@StringRes int confirmTextRes) {
        positiveButton.setText(confirmTextRes);
        return this;
    }

    public EmailPasswordDialog onConfirm(OnConfirmClickListener listener) {
        confirmClickListener = listener;
        return this;
    }

    private void dispatchOnConfirmClick() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (confirmClickListener != null) {
            confirmClickListener.onConfirmClick(email, password);
        }
    }
}

package com.anotherdev.sample.firebase.auth;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yarolegovich.lovelydialog.LovelyCustomDialog;

public class EmailPasswordDialog extends LovelyCustomDialog {

    public interface OnConfirmClickListener {
        void onConfirmClick(String email, String password);
    }

    private EditText emailEditText;
    private EditText passwordEditText;

    private OnConfirmClickListener confirmClickListener;


    public EmailPasswordDialog(Context context) {
        super(context);
        setTopColorRes(R.color.colorPrimary);
        setTitle(R.string.register_email_password);
        setIcon(android.R.drawable.ic_dialog_email);
        setView(R.layout.view_email_password_input);
        configureView(dialog -> {
            TextView ok = dialog.findViewById(R.id.positive_button);
            ok.setText(R.string.register);
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

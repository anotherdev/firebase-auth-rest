package com.anotherdev.sample.firebase.auth;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

abstract class MultipleTextInputDialog extends LovelyCustomDialog {

    @BindView(R.id.positive_button) TextView positiveButton;


    MultipleTextInputDialog(Context context) {
        super(context);
        setTopColorRes(R.color.colorPrimary);
        setView(getLayoutRes());
        configureView(this::onConfigureView);
        super.setListener(R.id.positive_button, true, button -> dispatchOnConfirmClick());
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @CallSuper
    protected void onConfigureView(View view) {
        ButterKnife.bind(this, view);
    }

    @CallSuper
    @Override
    public MultipleTextInputDialog setListener(int viewId, boolean dismissOnClick, View.OnClickListener listener) {
        super.setListener(viewId, dismissOnClick, v -> {
            dispatchOnConfirmClick();
            listener.onClick(v);
        });
        return this;
    }

    @CallSuper
    public MultipleTextInputDialog setConfirmButtonText(@StringRes int confirmTextRes) {
        positiveButton.setText(confirmTextRes);
        return this;
    }

    protected void dispatchOnConfirmClick() {
    }
}

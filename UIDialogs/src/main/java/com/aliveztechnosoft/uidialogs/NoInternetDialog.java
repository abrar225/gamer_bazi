package com.aliveztechnosoft.uidialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class NoInternetDialog extends Dialog {

    private RetryBtnClickListener retryBtnClickListener;

    public NoInternetDialog(@NonNull Context context) {
        super(context);
        this.retryBtnClickListener = null;
    }

    public void setRetryBtnClickListener(RetryBtnClickListener retryBtnClickListener) {
        this.retryBtnClickListener = retryBtnClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        super.onCreate(savedInstanceState);

        setContentView(R.layout.no_internet_dialog_layout);

        final AppCompatButton retryBtn = findViewById(R.id.retryBtn);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (retryBtnClickListener != null) {
                    retryBtnClickListener.onRetryBtnClick();
                }
            }
        });
    }
}

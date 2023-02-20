package com.aliveztechnosoft.gamerbaazi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

public final class AccountBlockDialog extends Dialog {

    private final String getMessage;

    public AccountBlockDialog(@NonNull Context context, String getMessage) {
        super(context);
        this.getMessage = getMessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_block_dialog_layout);

        final TextView messageTV = findViewById(R.id.messageTV);
        messageTV.setText(getMessage);
    }
}

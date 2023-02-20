package com.aliveztechnosoft.gamerbaazi.cash;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.aliveztechnosoft.gamerbaazi.R;

public class ResponseDialog extends Dialog {

    private final String title, message;
    private final int status;

    public ResponseDialog(@NonNull Context context, String title, String message, int status) {
        super(context);
        this.title = title;
        this.message = message;
        this.status = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.response_dialog_layout);

        final TextView titleTV = findViewById(R.id.titleTV);
        final TextView messageTv = findViewById(R.id.messageTV);
        final AppCompatButton gotItBtn = findViewById(R.id.gotItBtn);

        if (status == 0) {
            titleTV.setTextColor(Color.RED);
        } else {
            titleTV.setTextColor(Color.GREEN);
        }

        titleTV.setText(title);
        messageTv.setText(message);

        gotItBtn.setOnClickListener(view -> dismiss());

    }
}

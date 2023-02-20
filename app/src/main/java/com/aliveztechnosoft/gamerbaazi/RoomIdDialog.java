package com.aliveztechnosoft.gamerbaazi;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public final class RoomIdDialog extends Dialog {

    private final Context context;
    private final String roomId;
    private final String message;

    public RoomIdDialog(@NonNull Context context, String roomId, String message) {
        super(context);
        this.context = context;
        this.roomId = roomId;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_id_dialog_layout);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView messageTV = findViewById(R.id.messageTV);
        final TextView roomIdTV = findViewById(R.id.roomIdTV);
        final ImageView copyBtn = findViewById(R.id.copyBtn);
        final AppCompatButton gotItBtn = findViewById(R.id.gotItBtn);

        messageTV.setText(message);
        roomIdTV.setText(roomId);

        copyBtn.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("room_id", roomId);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context, "Room Id copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        backBtn.setOnClickListener(view -> dismiss());
        gotItBtn.setOnClickListener(view -> dismiss());
    }
}

package com.aliveztechnosoft.uidialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpdateDialog extends Dialog {

    private final List<String> updateDetailsLists = new ArrayList<>();
    private UpdateDetailsAdapter updateDetailsAdapter;
    private String versionName;
    private TextView versionNameTV;
    private UpdateBtnClickListener updateBtnClickListener;
    private Theme dialogTheme;

    public UpdateDialog(@NonNull Context context) {
        super(context);
        this.updateDetailsAdapter = null;
        this.versionNameTV = null;
        this.versionName = "";
        this.updateBtnClickListener = null;
        this.dialogTheme = null;
    }

    public void addUpdateDetails(String[] updateDetails) {
        Collections.addAll(updateDetailsLists, updateDetails);

        if (updateDetailsAdapter != null) {
            updateDetailsAdapter.refreshAdapter(updateDetailsLists);
        }
    }

    public void setDialogTheme(Theme dialogTheme) {
        this.dialogTheme = dialogTheme;
    }

    public void setUpdateBtnClickListener(UpdateBtnClickListener updateBtnClickListener) {
        this.updateBtnClickListener = updateBtnClickListener;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;

        if (versionNameTV != null) {
            versionNameTV.setText(versionName);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.update_dialog_layout);

        final ImageView view1 = findViewById(R.id.view1);
        final ImageView view2 = findViewById(R.id.view2);
        final RecyclerView updateDetails = findViewById(R.id.updateDetailsRecyclerView);
        final AppCompatButton updateNowBtn = findViewById(R.id.updateNowBtn);
        final CardView cardView = findViewById(R.id.cardView);
        final TextView updateTV = findViewById(R.id.updateTV);
        versionNameTV = findViewById(R.id.updateVersionName);

        // setting update version to the TextView
        versionNameTV.setText(versionName);

        // configure recyclerview
        updateDetails.setHasFixedSize(true);
        updateDetails.setLayoutManager(new LinearLayoutManager(getContext()));

        // set adapter to RecyclerView
        updateDetailsAdapter = new UpdateDetailsAdapter(updateDetailsLists);

        if (dialogTheme == Theme.Dark) {
            view1.setImageDrawable(generateImages(Color.parseColor("#11C9BB"), R.drawable.layer_1));
            view2.setImageDrawable(generateImages(Color.parseColor("#0DF5E3"), R.drawable.layer_2));
            cardView.setCardBackgroundColor(Color.parseColor("#262630"));
            updateNowBtn.setBackgroundDrawable(generateRoundBack(Color.parseColor("#11C9BB")));
            updateTV.setTextColor(Color.WHITE);
            versionNameTV.setTextColor(Color.parseColor("#11C9BB"));
            updateDetailsAdapter.setTextColor(Color.parseColor("#99FFFFFF"));
        }

        updateDetails.setAdapter(updateDetailsAdapter);

        updateNowBtn.setOnClickListener(v -> {
            if (updateBtnClickListener != null) {
                updateBtnClickListener.onUpdateBtnClick();
            }
        });
    }

    private Drawable generateImages(int color, int resourceId) {

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), resourceId);

        if (unwrappedDrawable != null) {
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, color);
        }
        return unwrappedDrawable;
    }

    private Drawable generateRoundBack(int color) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(10);
        drawable.setColor(color);

        return drawable;
    }

    public enum Theme {
        Dark, Light
    }
}

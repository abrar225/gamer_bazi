package com.aliveztechnosoft.tablayout;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TabItem {

    private final String itemName;
    private final Fragment fragment;
    private TextView textView;

    public TabItem(String itemName, Fragment fragment) {
        this.itemName = itemName;
        this.fragment = fragment;
        this.textView = null;
    }

    public String getItemName() {
        return itemName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}

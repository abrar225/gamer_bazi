package com.aliveztechnosoft.gamerbaazi;

import android.graphics.Bitmap;

public interface BitmapLoadListener {

    void onBitmapLoaded(Bitmap bitmap);
    void onFailed(String error);
}

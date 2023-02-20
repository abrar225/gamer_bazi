package com.aliveztechnosoft.gamerbaazi;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapFunctions {

    public static String bitmapToBase64(Bitmap bitmap) {

        if (bitmap == null) {
            return "";
        } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
        }

    }

    public static Bitmap base64ToBitmap(String base64String) {
        byte[] bytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static void uriToBitmap(ContentResolver contentResolver, Uri uri, BitmapLoadListener bitmapLoadListener) {
        try {
            if (uri.toString().contains("http://") || uri.toString().contains("www.") || uri.toString().contains("https://")) {
                new Thread(() -> {
                    try {
                        URL url = new URL(uri.toString());

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();

                        InputStream inputStream = connection.getInputStream();

                        byte[] buffer = new byte[1024];
                        int totalDownloadedBytes = 0;
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        while (true) {

                            int downloadedBytes = inputStream.read(buffer);

                            if (downloadedBytes > 0) {
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
                                byteArrayOutputStream.close();
                                inputStream.close();
                                Looper.prepare();
                                bitmapLoadListener.onBitmapLoaded(bitmap);
                                break;
                            } else {
                                byteArrayOutputStream.write(buffer, 0, downloadedBytes);
                                totalDownloadedBytes = totalDownloadedBytes + downloadedBytes;
                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                        Looper.prepare();
                        bitmapLoadListener.onFailed(e.getMessage());
                    }
                }).start();
            } else {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                Looper.prepare();
                bitmapLoadListener.onBitmapLoaded(bitmap);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
            Looper.prepare();
            bitmapLoadListener.onFailed(exception.getMessage());
        }
    }

    public static String convertBitmapToBase64String(Bitmap bitmap) {

        String encodedImage;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        encodedImage = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);

        return encodedImage;

    }

    public static void saveBitmapToInternalStorage(Context context, Bitmap bitmapImage, String fileName) {

        final File shareImagePath = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/" + fileName);

        try {
            FileOutputStream fos = new FileOutputStream(shareImagePath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

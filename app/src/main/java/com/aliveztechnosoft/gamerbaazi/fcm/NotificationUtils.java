package com.aliveztechnosoft.gamerbaazi.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;

import androidx.core.app.NotificationCompat;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.login_register.Login;
import com.aliveztechnosoft.gamerbaazi.utilities.NotificationData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


class NotificationUtils {
    private static final String CHANNEL_ID = "myChannel";
    private static final String CHANNEL_NAME = "myChannelName";
    private final Context mContext;

    NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    void displayNotification(NotificationVO notificationVO) {
        {
            String message = notificationVO.getMessage();
            String title = notificationVO.getTitle();
            String iconUrl = notificationVO.getIconUrl();
            String action = notificationVO.getAction();
            String data = notificationVO.getData();
            String destination = notificationVO.getActionDestination();
            String campaignId = notificationVO.getCampaignId();
            Intent resultIntent;

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("campaign_id", campaignId);
                jsonObject.put("destination", destination);
                jsonObject.put("payload_data", data);

                NotificationData.get(mContext, "").appendJSONObject(jsonObject);

            } catch (JSONException ignored) {
            }

            // Acknowledge Notification
            MyConstants.acknowledgeNotification(mContext, "received");

            Bitmap iconBitMap = null;

            if (iconUrl != null) {
                iconBitMap = getBitmapFromURL(iconUrl);
            }

            PendingIntent resultPendingIntent;

            if (action != null && action.equals("url")) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                } else {
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                }

            } else {

                resultIntent = new Intent(mContext, Login.class);
                resultIntent.putExtra("campaign_id", campaignId);

                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_MUTABLE);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
                } else {
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, 0);
                }

            }

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID);

            Notification notification;

            if (iconBitMap == null) {

                //When Inbox Style is applied, user can expand the notification
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(message);
                notification = mBuilder.setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setSmallIcon(R.drawable.gamer_bazi_logo)
                        .setContentText(message)
                        .build();

            } else {

                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                bigPictureStyle.bigPicture(iconBitMap);
                notification = mBuilder.setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(bigPictureStyle)
                        .setSmallIcon(R.drawable.gamer_bazi_logo)
                        .setContentText(message)
                        .build();
            }

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            //All notifications should go through NotificationChannel on Android 26 & above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);

            }
            assert notificationManager != null;
            notificationManager.notify((int) System.currentTimeMillis(), notification);
        }
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

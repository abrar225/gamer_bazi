package com.aliveztechnosoft.gamerbaazi.fcm;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage.getNotification());
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {

        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(RemoteMsgNotification.getTitle());
        notificationVO.setMessage(RemoteMsgNotification.getBody());

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO);
        notificationUtils.playNotificationSound();
    }


    private void handleData(Map<String, String> data) {

        String title = data.get("title");
        String message = data.get("message");
        String iconUrl = data.get("image");
        String action = data.get("action");
        String[] campaignIdAndData = {"0", ""};
        if (data.get("data") != null) {
            campaignIdAndData = Objects.requireNonNull(data.get("data")).split(",,,,");
        }
        String actionDestination = data.get("action_destination");

        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        notificationVO.setIconUrl(iconUrl);

        if (campaignIdAndData.length == 1) {
            notificationVO.setData("");
        } else {
            notificationVO.setData(campaignIdAndData[1]);
        }

        notificationVO.setAction(action);
        notificationVO.setCampaignId(campaignIdAndData[0]);
        notificationVO.setActionDestination(actionDestination);

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO);
        notificationUtils.playNotificationSound();

    }
}

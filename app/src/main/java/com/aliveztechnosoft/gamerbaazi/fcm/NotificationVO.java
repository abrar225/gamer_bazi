package com.aliveztechnosoft.gamerbaazi.fcm;

public class NotificationVO {
    private String campaignId;
    private String title;
    private String message;
    private String iconUrl;
    private String action;
    private String data;
    private String actionDestination;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    String getIconUrl() {
        return iconUrl;
    }

    void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    String getAction() {
        return action;
    }

    void setAction(String action) {
        this.action = action;
    }

    String getActionDestination() {
        return actionDestination;
    }

    void setActionDestination(String actionDestination) {
        this.actionDestination = actionDestination;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }
}

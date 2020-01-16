package com.singularitycoder.yesbank.Notifications;

public class NotificationItem {

    private String strImage;
    private String strName;
    private String strNotifMessage;
    private String strDateTime;

    public NotificationItem(String strImage, String strName, String strNotifMessage, String strDateTime) {
        this.strImage = strImage;
        this.strName = strName;
        this.strNotifMessage = strNotifMessage;
        this.strDateTime = strDateTime;
    }

    public String getStrImage() {
        return strImage;
    }

    public void setStrImage(String strImage) {
        this.strImage = strImage;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrNotifMessage() {
        return strNotifMessage;
    }

    public void setStrNotifMessage(String strNotifMessage) {
        this.strNotifMessage = strNotifMessage;
    }

    public String getStrDateTime() {
        return strDateTime;
    }

    public void setStrDateTime(String strDateTime) {
        this.strDateTime = strDateTime;
    }
}

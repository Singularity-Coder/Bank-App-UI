package com.singularitycoder.yesbank.More;

public class MoreItem {

    private String moreItemName;
    private int moreItemImage;
    private int intTextColor;
    private int intIconColor;

    private String strMoreProfilePic;
    private String strMoreName;
    private String strMorePhone;
    private String strMoreLicenseNumber;

    public MoreItem() {
    }

    // Header
    public MoreItem(String strMoreProfilePic, String strMoreName, String strMorePhone, String strMoreLicenseNumber) {
        this.strMoreProfilePic = strMoreProfilePic;
        this.strMoreName = strMoreName;
        this.strMorePhone = strMorePhone;
        this.strMoreLicenseNumber = strMoreLicenseNumber;
    }

    public MoreItem(String moreItemName, int moreItemImage, int intTextColor, int intIconColor) {
        this.moreItemName = moreItemName;
        this.moreItemImage = moreItemImage;
        this.intTextColor = intTextColor;
        this.intIconColor = intIconColor;
    }

    public String getMoreItemName() {
        return moreItemName;
    }

    public void setMoreItemName(String moreItemName) {
        this.moreItemName = moreItemName;
    }

    public int getMoreItemImage() {
        return moreItemImage;
    }

    public void setMoreItemImage(int moreItemImage) {
        this.moreItemImage = moreItemImage;
    }

    public int getIntTextColor() {
        return intTextColor;
    }

    public void setIntTextColor(int intTextColor) {
        this.intTextColor = intTextColor;
    }

    public int getIntIconColor() {
        return intIconColor;
    }

    public void setIntIconColor(int intIconColor) {
        this.intIconColor = intIconColor;
    }

    public String getStrMoreProfilePic() {
        return strMoreProfilePic;
    }

    public void setStrMoreProfilePic(String strMoreProfilePic) {
        this.strMoreProfilePic = strMoreProfilePic;
    }

    public String getStrMoreName() {
        return strMoreName;
    }

    public void setStrMoreName(String strMoreName) {
        this.strMoreName = strMoreName;
    }

    public String getStrMorePhone() {
        return strMorePhone;
    }

    public void setStrMorePhone(String strMorePhone) {
        this.strMorePhone = strMorePhone;
    }

    public String getStrMoreLicenseNumber() {
        return strMoreLicenseNumber;
    }

    public void setStrMoreLicenseNumber(String strMoreLicenseNumber) {
        this.strMoreLicenseNumber = strMoreLicenseNumber;
    }
}

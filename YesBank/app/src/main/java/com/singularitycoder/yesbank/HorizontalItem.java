package com.singularitycoder.yesbank;

/**
 * Created by Hithesh on 16,January,2020.
 */
public class HorizontalItem {

    private int intImage;
    private String strText;
    private int imageColor;
    private int textColor;
    private int cardColor;

    public HorizontalItem(int intImage, String strText, int imageColor, int textColor, int cardColor) {
        this.intImage = intImage;
        this.strText = strText;
        this.imageColor = imageColor;
        this.textColor = textColor;
        this.cardColor = cardColor;
    }

    public int getIntImage() {
        return intImage;
    }

    public void setIntImage(int intImage) {
        this.intImage = intImage;
    }

    public String getStrText() {
        return strText;
    }

    public void setStrText(String strText) {
        this.strText = strText;
    }

    public int getImageColor() {
        return imageColor;
    }

    public void setImageColor(int imageColor) {
        this.imageColor = imageColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }
}

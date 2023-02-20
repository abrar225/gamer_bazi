package com.aliveztechnosoft.navigationbar;

import android.graphics.Color;

public class CustomNavTheme {

    private int textColor;
    private int iconsColor;
    private int navigationBackground;
    private int groupNameColor;
    private int selectedItemBackgroundColor;
    private int selectedItemTextColor;
    private int selectedItemIconColor;
    private int headerWishTextColor;
    private int headerProfileNameTextColor;
    private int logoutTextColor;

    public CustomNavTheme() {
        textColor = 0;
        iconsColor = 0;
        navigationBackground = 0;
        groupNameColor = 0;
        selectedItemBackgroundColor = 0;
        selectedItemTextColor = 0;
        selectedItemIconColor = 0;
        headerWishTextColor = 0;
        headerProfileNameTextColor = 0;
        logoutTextColor = 0;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextColor(NavigationBar.NavColors textColor) {
        this.textColor = Color.parseColor(getNavColorValue(textColor));
    }

    public void setLogoutTextColor(int textColor) {
        this.logoutTextColor = textColor;
    }

    public void setLogoutTextColor(NavigationBar.NavColors textColor) {
        this.logoutTextColor = Color.parseColor(getNavColorValue(textColor));
    }

    public int getLogoutTextColor() {
        return logoutTextColor;
    }

    public void setHeaderWishTextColor(int textColor) {
        this.headerWishTextColor = textColor;
    }

    public void setHeaderWishTextColor(NavigationBar.NavColors textColor) {
        this.headerWishTextColor = Color.parseColor(getNavColorValue(textColor));
    }

    public int getHeaderWishTextColor() {
        return headerWishTextColor;
    }

    public void setHeaderProfileNameTextColor(int textColor) {
        this.headerProfileNameTextColor = textColor;
    }

    public void setHeaderProfileNameTextColor(NavigationBar.NavColors textColor) {
        this.headerProfileNameTextColor = Color.parseColor(getNavColorValue(textColor));
    }

    public int getHeaderProfileNameTextColor() {
        return headerProfileNameTextColor;
    }

    public int getIconsColor() {
        return iconsColor;
    }

    public void setIconsColor(int iconsColor) {
        this.iconsColor = iconsColor;
    }

    public void setIconsColor(NavigationBar.NavColors iconsColor) {
        this.iconsColor = Color.parseColor(getNavColorValue(iconsColor));
    }

    public int getNavigationBackground() {
        return navigationBackground;
    }

    public void setNavigationBackground(int navigationBackgroundColor) {
        this.navigationBackground = navigationBackgroundColor;
    }

    public int getGroupNameColor() {
        return groupNameColor;
    }

    public void setGroupNameColor(int groupNameColor) {
        this.groupNameColor = groupNameColor;
    }

    public void setGroupNameColor(NavigationBar.NavColors groupNameColor) {
        this.groupNameColor = Color.parseColor(getNavColorValue(groupNameColor));
    }

    public int getSelectedItemBackgroundColor() {
        return selectedItemBackgroundColor;
    }

    public void setSelectedItemBackgroundColor(int selectedItemBackgroundColor) {
        this.selectedItemBackgroundColor = selectedItemBackgroundColor;
    }

    public void setSelectedItemBackgroundColor(NavigationBar.NavColors selectedItemBackgroundColor) {
        this.selectedItemBackgroundColor = Color.parseColor(getNavColorValue(selectedItemBackgroundColor));
    }

    public int getSelectedItemTextColor() {
        return selectedItemTextColor;
    }

    public void setSelectedItemTextColor(int selectedItemTextColor) {
        this.selectedItemTextColor = selectedItemTextColor;
    }

    public void setSelectedItemTextColor(NavigationBar.NavColors selectedItemTextColor) {
        this.selectedItemTextColor = Color.parseColor(getNavColorValue(selectedItemTextColor));
    }

    public int getSelectedItemIconColor() {
        return selectedItemIconColor;
    }

    public void setSelectedItemIconColor(int selectedItemIconColor) {
        this.selectedItemIconColor = selectedItemIconColor;
    }

    public void setSelectedItemIconColor(NavigationBar.NavColors selectedItemIconColor) {
        this.selectedItemIconColor = Color.parseColor(getNavColorValue(selectedItemIconColor));
    }

    private String getNavColorValue(NavigationBar.NavColors navColor) {

        String selectedColorValue = "";

        if (navColor == NavigationBar.NavColors.RED) {
            selectedColorValue = "#FFFF1744";
        } else if (navColor == NavigationBar.NavColors.BLACK) {
            selectedColorValue = "#000000";
        } else if (navColor == NavigationBar.NavColors.GRAY) {
            selectedColorValue = "#998A8A8A";
        } else if (navColor == NavigationBar.NavColors.ORANGE) {
            selectedColorValue = "#FF9100";
        } else if (navColor == NavigationBar.NavColors.WHITE) {
            selectedColorValue = "#FFFFFF";
        } else if (navColor == NavigationBar.NavColors.YELLOW) {
            selectedColorValue = "#FFEA00";
        } else if (navColor == NavigationBar.NavColors.DARK_RED) {
            selectedColorValue = "#FFD50000";
        } else if (navColor == NavigationBar.NavColors.LIGHT_RED) {
            selectedColorValue = "#FFFF8A80";
        } else if (navColor == NavigationBar.NavColors.DARK_ORANGE) {
            selectedColorValue = "#FFFF6D00";
        } else if (navColor == NavigationBar.NavColors.LIGHT_ORANGE) {
            selectedColorValue = "#FFFFD180";
        } else if (navColor == NavigationBar.NavColors.BLUE) {
            selectedColorValue = "#FF00B0FF";
        } else if (navColor == NavigationBar.NavColors.DARK_BLUE) {
            selectedColorValue = "#FF0091EA";
        } else if (navColor == NavigationBar.NavColors.LIGHT_BLUE) {
            selectedColorValue = "#FF80D8FF";
        }

        return selectedColorValue;
    }

}

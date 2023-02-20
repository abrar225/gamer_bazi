package com.aliveztechnosoft.bottombar;

import android.graphics.Color;

public class BottomBarCustomTheme {

    private int iconsColor;
    private int backgroundColor;
    private int selectedItemBackgroundColor;
    private int selectedItemTextColor;
    private int selectedItemIconColor;

    public BottomBarCustomTheme() {
        iconsColor = 0;
        selectedItemBackgroundColor = 0;
        selectedItemTextColor = 0;
        selectedItemIconColor = 0;
    }

    public BottomBarCustomTheme(int iconsColor, int backgroundColor, int selectedItemBackgroundColor, int selectedItemTextColor, int selectedItemIconColor) {
        this.iconsColor = iconsColor;
        this.backgroundColor = backgroundColor;
        this.selectedItemBackgroundColor = selectedItemBackgroundColor;
        this.selectedItemTextColor = selectedItemTextColor;
        this.selectedItemIconColor = selectedItemIconColor;
    }

    public int getIconsColor() {
        return iconsColor;
    }

    public void setIconsColor(int iconsColor) {
        this.iconsColor = iconsColor;
    }

    public void setIconsColor(BottomBar.BottomBarColors iconsColor) {
        this.iconsColor = Color.parseColor(getColorValue(iconsColor));
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundColor(BottomBar.BottomBarColors backgroundColor) {
        this.backgroundColor = Color.parseColor(getColorValue(backgroundColor));
    }

    public int getSelectedItemBackgroundColor() {
        return selectedItemBackgroundColor;
    }

    public void setSelectedItemBackgroundColor(int selectedItemBackgroundColor) {
        this.selectedItemBackgroundColor = selectedItemBackgroundColor;
    }

    public void setSelectedItemBackgroundColor(BottomBar.BottomBarColors selectedItemBackgroundColor) {
        this.selectedItemBackgroundColor = Color.parseColor(getColorValue(selectedItemBackgroundColor));
    }

    public int getSelectedItemTextColor() {
        return selectedItemTextColor;
    }

    public void setSelectedItemTextColor(int selectedItemTextColor) {
        this.selectedItemTextColor = selectedItemTextColor;
    }

    public void setSelectedItemTextColor(BottomBar.BottomBarColors selectedItemTextColor) {
        this.selectedItemTextColor = Color.parseColor(getColorValue(selectedItemTextColor));
    }

    public int getSelectedItemIconColor() {
        return selectedItemIconColor;
    }

    public void setSelectedItemIconColor(int selectedItemIconColor) {
        this.selectedItemIconColor = selectedItemIconColor;
    }

    public void setSelectedItemIconColor(BottomBar.BottomBarColors selectedItemIconColor) {
        this.selectedItemIconColor = Color.parseColor(getColorValue(selectedItemIconColor));
    }

    private String getColorValue(BottomBar.BottomBarColors bottomBarColor) {

        String selectedColorValue = "";

        if (bottomBarColor == BottomBar.BottomBarColors.RED) {
            selectedColorValue = "#FFFF1744";
        } else if (bottomBarColor == BottomBar.BottomBarColors.BLACK) {
            selectedColorValue = "#000000";
        } else if (bottomBarColor == BottomBar.BottomBarColors.GRAY) {
            selectedColorValue = "#998A8A8A";
        } else if (bottomBarColor == BottomBar.BottomBarColors.ORANGE) {
            selectedColorValue = "#FF9100";
        } else if (bottomBarColor == BottomBar.BottomBarColors.WHITE) {
            selectedColorValue = "#FFFFFF";
        } else if (bottomBarColor == BottomBar.BottomBarColors.YELLOW) {
            selectedColorValue = "#FFEA00";
        } else if (bottomBarColor == BottomBar.BottomBarColors.DARK_RED) {
            selectedColorValue = "#FFD50000";
        } else if (bottomBarColor == BottomBar.BottomBarColors.LIGHT_RED) {
            selectedColorValue = "#FFFF8A80";
        } else if (bottomBarColor == BottomBar.BottomBarColors.DARK_ORANGE) {
            selectedColorValue = "#FFFF6D00";
        } else if (bottomBarColor == BottomBar.BottomBarColors.LIGHT_ORANGE) {
            selectedColorValue = "#FFFFD180";
        } else if (bottomBarColor == BottomBar.BottomBarColors.BLUE) {
            selectedColorValue = "#FF00B0FF";
        } else if (bottomBarColor == BottomBar.BottomBarColors.DARK_BLUE) {
            selectedColorValue = "#FF0091EA";
        } else if (bottomBarColor == BottomBar.BottomBarColors.LIGHT_BLUE) {
            selectedColorValue = "#FF80D8FF";
        }

        return selectedColorValue;
    }

}

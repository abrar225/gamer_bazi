package com.aliveztechnosoft.navigationbar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NavigationBar extends NavigationView {

    public static int GROUPS_COUNT = 0;
    public static int selectedItemPosition = 0;
    public static int drawerGravity = GravityCompat.START;

    private final Context context;
    private final List<NavItem> navItems = new ArrayList<>();
    private final List<NavItemsGroup> navItemsGroups = new ArrayList<>();
    private int iconsColor;
    private int navItemTxtColor;
    private int navGroupTxtColor;
    private int selectedItemBackgroundColor;
    private int selectedItemTextColor;
    private int selectedItemIconColor;
    private ImageView headerImage;
    private TextView profileName;
    private NavigationAdapter navigationAdapter;
    private NavThemes selectedTheme = NavThemes.LIGHT;
    private LinearLayout headerRootLayout;
    private RelativeLayout bodyRootLayout;
    private RelativeLayout navLogOutLayout;
    private TextView navLogOutTxt;
    private TextView wishMessage;
    private NavigationEventListener navigationEventListener;

    public NavigationBar(@NonNull Context context) {
        super(context);
        this.context = context;
        NavigationBar.drawerGravity = GravityCompat.START;
        gettingSelectedThemeDetails();
        init();
    }

    public NavigationBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        NavigationBar.drawerGravity = GravityCompat.START;
        gettingSelectedThemeDetails();
        init();
    }

    public void setSelected(int navItemPosition) {

        navItems.get(NavigationBar.selectedItemPosition).setSelected(false);
        navItems.get(navItemPosition).setSelected(true);

        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void setDrawerLayout(DrawerLayout drawerLayout, DrawerGravity drawerGravity) {

        if (drawerGravity == DrawerGravity.LEFT) {
            NavigationBar.drawerGravity = GravityCompat.START;
        } else {
            NavigationBar.drawerGravity = GravityCompat.END;
        }

        navigationAdapter.setDrawerLayout(drawerLayout);
    }

    public void setTheme(NavThemes theme) {
        this.selectedTheme = theme;

        if (selectedTheme == NavThemes.DARK) {
            headerRootLayout.setBackgroundColor(Color.parseColor("#0E0E0E"));
            bodyRootLayout.setBackgroundColor(Color.parseColor("#0E0E0E"));

            profileName.setTextColor(Color.parseColor("#FFFFFF"));
            wishMessage.setTextColor(Color.parseColor("#CCFFFFFF"));

            navLogOutTxt.setTextColor(Color.WHITE);
        }

        gettingSelectedThemeDetails();
        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void setTheme(CustomNavTheme customNavigationTheme) {

        if (customNavigationTheme.getNavigationBackground() != 0) {
            headerRootLayout.setBackgroundColor(customNavigationTheme.getNavigationBackground());
            bodyRootLayout.setBackgroundColor(customNavigationTheme.getNavigationBackground());
        }

        if (customNavigationTheme.getTextColor() != 0) {
            navItemTxtColor = customNavigationTheme.getTextColor();
        }

        if (customNavigationTheme.getLogoutTextColor() != 0) {
            navLogOutTxt.setTextColor(customNavigationTheme.getLogoutTextColor());
        }

        if(customNavigationTheme.getHeaderWishTextColor() != 0){
            wishMessage.setTextColor(customNavigationTheme.getHeaderWishTextColor());
        }

        if(customNavigationTheme.getHeaderProfileNameTextColor() != 0){
            profileName.setTextColor(customNavigationTheme.getHeaderProfileNameTextColor());
        }
        if (customNavigationTheme.getIconsColor() != 0) {
            iconsColor = customNavigationTheme.getIconsColor();
        }

        if (customNavigationTheme.getGroupNameColor() != 0) {
            navGroupTxtColor = customNavigationTheme.getGroupNameColor();
        }

        if (customNavigationTheme.getSelectedItemBackgroundColor() != 0) {
            selectedItemBackgroundColor = customNavigationTheme.getSelectedItemBackgroundColor();
        }

        if (customNavigationTheme.getSelectedItemIconColor() != 0) {
            selectedItemIconColor = customNavigationTheme.getSelectedItemIconColor();
        }

        if (customNavigationTheme.getSelectedItemTextColor() != 0) {
            selectedItemTextColor = customNavigationTheme.getSelectedItemTextColor();
        }

        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    private void gettingSelectedThemeDetails() {

        if (selectedTheme == NavThemes.DARK) {
            iconsColor = Color.parseColor("#E6FFFFFF");
            navItemTxtColor = Color.parseColor("#E6FFFFFF");
            navGroupTxtColor = Color.parseColor("#66FFFFFF");
            selectedItemBackgroundColor = Color.parseColor("#4C74FA");
            selectedItemIconColor = Color.WHITE;
            selectedItemTextColor = Color.WHITE;
        } else {
            iconsColor = Color.parseColor("#99000000");
            navItemTxtColor = Color.parseColor("#99000000");
            selectedItemBackgroundColor = Color.parseColor("#4C74FA");
            navGroupTxtColor = Color.parseColor("#66000000");
            selectedItemIconColor = Color.WHITE;
            selectedItemTextColor = Color.WHITE;
        }
    }

    private String getNavColorValue(NavColors navColor) {

        String selectedColorValue = "";

        if (navColor == NavColors.RED) {
            selectedColorValue = "#FFFF1744";
        } else if (navColor == NavColors.BLACK) {
            selectedColorValue = "#000000";
        } else if (navColor == NavColors.GRAY) {
            selectedColorValue = "#998A8A8A";
        } else if (navColor == NavColors.ORANGE) {
            selectedColorValue = "#FF9100";
        } else if (navColor == NavColors.WHITE) {
            selectedColorValue = "#FFFFFF";
        } else if (navColor == NavColors.YELLOW) {
            selectedColorValue = "#FFEA00";
        } else if (navColor == NavColors.DARK_RED) {
            selectedColorValue = "#FFD50000";
        } else if (navColor == NavColors.LIGHT_RED) {
            selectedColorValue = "#FFFF8A80";
        } else if (navColor == NavColors.DARK_ORANGE) {
            selectedColorValue = "#FFFF6D00";
        } else if (navColor == NavColors.LIGHT_ORANGE) {
            selectedColorValue = "#FFFFD180";
        } else if (navColor == NavColors.BLUE) {
            selectedColorValue = "#FF00B0FF";
        } else if (navColor == NavColors.DARK_BLUE) {
            selectedColorValue = "#FF0091EA";
        } else if (navColor == NavColors.LIGHT_BLUE) {
            selectedColorValue = "#FF80D8FF";
        }

        return selectedColorValue;
    }


    public void setEventListener(NavigationEventListener navigationEventListener) {
        this.navigationEventListener = navigationEventListener;
        navigationAdapter.setNavigationEventListener(this.navigationEventListener);
    }

    public void setIconsColor(NavColors iconsColor) {

        if (iconsColor == NavColors.DEFAULT) {
            gettingSelectedThemeDetails();
        } else {
            this.iconsColor = Color.parseColor(getNavColorValue(iconsColor));
        }

        navigationAdapter.reloadNavigationBar(this.iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void setSelectedItemBackground(NavColors selectedItemBackgroundColor) {
        if (selectedItemBackgroundColor == NavColors.DEFAULT) {
            gettingSelectedThemeDetails();
        } else {
            this.selectedItemBackgroundColor = Color.parseColor(getNavColorValue(selectedItemBackgroundColor));
        }

        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, this.selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void setHeaderData(String profileNameTxt) {
        profileName.setText(profileNameTxt);
    }

    public void setHeaderData(String profileNameTxt, @Nullable Bitmap profileImage) {

        if (profileImage != null) {
            headerImage.setImageBitmap(profileImage);
        }

        profileName.setText(profileNameTxt);
    }

    public void setHeaderData(String profileNameTxt, @Nullable int profileImageResId) {
        headerImage.setImageResource(profileImageResId);
        profileName.setText(profileNameTxt);
    }

    public void setHeaderData(String profileNameTxt, @Nullable File profileImageFile) throws NavExceptions {
        profileName.setText(profileNameTxt);

        if (profileImageFile.exists()) {
            headerImage.setImageBitmap(BitmapFactory.decodeFile(profileImageFile.getAbsolutePath()));
        } else {
            throw new NavExceptions("Invalid File Path");
        }
    }

    public void setHeaderData(String profileNameTxt, @Nullable Uri profileImageUri) throws NavExceptions {
        profileName.setText(profileNameTxt);

        final ContentResolver contentResolver = context.getContentResolver();
        final String[] projection = {MediaStore.MediaColumns.DATA};

        final Cursor cursor = contentResolver.query(profileImageUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(0);

                if (new File(filePath).exists()) {
                    headerImage.setImageURI(profileImageUri);
                } else {
                    throw new NavExceptions("No File Found");
                }
            } else {
                throw new NavExceptions("Invalid URI Format");
            }
            cursor.close();
        } else {
            throw new NavExceptions("Invalid URI");
        }

    }

    public void addNavItem(NavItem.BuiltInItems builtInItems) {

        NavItem navItem = new NavItem();

        if (builtInItems == NavItem.BuiltInItems.DASHBOARD) {
            navItem.setTitle("Dashboard");
            navItem.setIcon(R.drawable.dashboard_icon);
        } else if (builtInItems == NavItem.BuiltInItems.HOME) {
            navItem.setTitle("Home");
            navItem.setIcon(R.drawable.home_icon);
        } else if (builtInItems == NavItem.BuiltInItems.SEND) {
            navItem.setTitle("Send");
            navItem.setIcon(R.drawable.send_icon);
        } else if (builtInItems == NavItem.BuiltInItems.SETTINGS) {
            navItem.setTitle("Settings");
            navItem.setIcon(R.drawable.settings_icon);
        } else if (builtInItems == NavItem.BuiltInItems.ABOUT_US) {
            navItem.setTitle("About Us");
            navItem.setIcon(R.drawable.about_us_icon);
        } else if (builtInItems == NavItem.BuiltInItems.CONTACT_US) {
            navItem.setTitle("Contact Us");
            navItem.setIcon(R.drawable.contact_us_icon);
        } else if (builtInItems == NavItem.BuiltInItems.DOWNLOAD) {
            navItem.setTitle("Downloads");
            navItem.setIcon(R.drawable.download_icon);
        } else if (builtInItems == NavItem.BuiltInItems.EMAIL) {
            navItem.setTitle("Email");
            navItem.setIcon(R.drawable.email_icon);
        } else if (builtInItems == NavItem.BuiltInItems.FAVOURITES) {
            navItem.setTitle("Favourites");
            navItem.setIcon(R.drawable.favourite_icon);
        } else if (builtInItems == NavItem.BuiltInItems.GALLERY) {
            navItem.setTitle("Gallery");
            navItem.setIcon(R.drawable.gallery_icon);
        } else if (builtInItems == NavItem.BuiltInItems.HELP) {
            navItem.setTitle("Help");
            navItem.setIcon(R.drawable.help_icon);
        } else if (builtInItems == NavItem.BuiltInItems.MESSAGE) {
            navItem.setTitle("Message");
            navItem.setIcon(R.drawable.message_icon);
        } else if (builtInItems == NavItem.BuiltInItems.FEEDBACK) {
            navItem.setTitle("Feedback");
            navItem.setIcon(R.drawable.feedback_icon);
        } else if (builtInItems == NavItem.BuiltInItems.PRIVACY_POLICY) {
            navItem.setTitle("Privacy Policy");
            navItem.setIcon(R.drawable.privacy_policy_icon);
        } else if (builtInItems == NavItem.BuiltInItems.RATE_US) {
            navItem.setTitle("Rate Us");
            navItem.setIcon(R.drawable.rate_us_icon);
        } else if (builtInItems == NavItem.BuiltInItems.UPLOAD) {
            navItem.setTitle("Upload");
            navItem.setIcon(R.drawable.upload_icon);
        } else if (builtInItems == NavItem.BuiltInItems.TOOLS) {
            navItem.setTitle("Tools");
            navItem.setIcon(R.drawable.tools_icon);
        } else if (builtInItems == NavItem.BuiltInItems.SEARCH) {
            navItem.setTitle("Search");
            navItem.setIcon(R.drawable.search_icon);
        } else if (builtInItems == NavItem.BuiltInItems.SHARE) {
            navItem.setTitle("Share");
            navItem.setIcon(R.drawable.share_icon);
        } else if (builtInItems == NavItem.BuiltInItems.TRASH) {
            navItem.setTitle("Trash");
            navItem.setIcon(R.drawable.trash_icon);
        } else if (builtInItems == NavItem.BuiltInItems.PROFILE) {
            navItem.setTitle("Profile");
            navItem.setIcon(R.drawable.profile_icon);
        }

        if (navItems.size() == 0) {
            navItem.setSelected(true);
        }

        navItems.add(navItem);
        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void addNavItem(NavItem.BuiltInItems builtInItems, Fragment fragment, int fragmentContainerResId) {

        NavItem navItem = new NavItem();
        navItem.setFragment(fragment, fragmentContainerResId);

        if (builtInItems == NavItem.BuiltInItems.DASHBOARD) {
            navItem.setTitle("Dashboard");
            navItem.setIcon(R.drawable.dashboard_icon);
        } else if (builtInItems == NavItem.BuiltInItems.HOME) {
            navItem.setTitle("Home");
            navItem.setIcon(R.drawable.home_icon);
        } else if (builtInItems == NavItem.BuiltInItems.SEND) {
            navItem.setTitle("Send");
            navItem.setIcon(R.drawable.send_icon);
        } else if (builtInItems == NavItem.BuiltInItems.SETTINGS) {
            navItem.setTitle("Settings");
            navItem.setIcon(R.drawable.settings_icon);
        } else if (builtInItems == NavItem.BuiltInItems.ABOUT_US) {
            navItem.setTitle("About Us");
            navItem.setIcon(R.drawable.about_us_icon);
        } else if (builtInItems == NavItem.BuiltInItems.CONTACT_US) {
            navItem.setTitle("Contact Us");
            navItem.setIcon(R.drawable.contact_us_icon);
        } else if (builtInItems == NavItem.BuiltInItems.DOWNLOAD) {
            navItem.setTitle("Downloads");
            navItem.setIcon(R.drawable.download_icon);
        } else if (builtInItems == NavItem.BuiltInItems.EMAIL) {
            navItem.setTitle("Email");
            navItem.setIcon(R.drawable.email_icon);
        } else if (builtInItems == NavItem.BuiltInItems.FAVOURITES) {
            navItem.setTitle("Favourites");
            navItem.setIcon(R.drawable.favourite_icon);
        } else if (builtInItems == NavItem.BuiltInItems.GALLERY) {
            navItem.setTitle("Gallery");
            navItem.setIcon(R.drawable.gallery_icon);
        } else if (builtInItems == NavItem.BuiltInItems.HELP) {
            navItem.setTitle("Help");
            navItem.setIcon(R.drawable.help_icon);
        } else if (builtInItems == NavItem.BuiltInItems.MESSAGE) {
            navItem.setTitle("Message");
            navItem.setIcon(R.drawable.message_icon);
        } else if (builtInItems == NavItem.BuiltInItems.FEEDBACK) {
            navItem.setTitle("Feedback");
            navItem.setIcon(R.drawable.feedback_icon);
        } else if (builtInItems == NavItem.BuiltInItems.PRIVACY_POLICY) {
            navItem.setTitle("Privacy Policy");
            navItem.setIcon(R.drawable.privacy_policy_icon);
        } else if (builtInItems == NavItem.BuiltInItems.RATE_US) {
            navItem.setTitle("Rate Us");
            navItem.setIcon(R.drawable.rate_us_icon);
        } else if (builtInItems == NavItem.BuiltInItems.UPLOAD) {
            navItem.setTitle("Upload");
            navItem.setIcon(R.drawable.upload_icon);
        } else if (builtInItems == NavItem.BuiltInItems.TOOLS) {
            navItem.setTitle("Tools");
            navItem.setIcon(R.drawable.tools_icon);
        } else if (builtInItems == NavItem.BuiltInItems.SEARCH) {
            navItem.setTitle("Search");
            navItem.setIcon(R.drawable.search_icon);
        } else if (builtInItems == NavItem.BuiltInItems.SHARE) {
            navItem.setTitle("Share");
            navItem.setIcon(R.drawable.share_icon);
        } else if (builtInItems == NavItem.BuiltInItems.TRASH) {
            navItem.setTitle("Trash");
            navItem.setIcon(R.drawable.trash_icon);
        } else if (builtInItems == NavItem.BuiltInItems.PROFILE) {
            navItem.setTitle("Profile");
            navItem.setIcon(R.drawable.profile_icon);
        }

        if (navItems.size() == 0) {
            navItem.setSelected(true);
        }

        navItems.add(navItem);
        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void addNavItem(NavItem navItem) {
        navItems.add(navItem);
        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void addNavItems(List<NavItem> navItems) {

        if (navItems.size() == 0) {
            navItems.get(0).setSelected(true);
        }

        navItems.addAll(navItems);
        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void addItemsGroup(NavItemsGroup navItemsGroup) {

        if (navItems.size() == 0) {
            navItemsGroup.getGroupItems().get(0).setSelected(true);
        }

        navItemsGroups.add(navItemsGroup);
        navItems.addAll(navItemsGroup.getGroupItems());
        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void removeItem(int itemPosition) {
        navItems.remove(itemPosition);
        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    public void setBackgroundColor(int color) {
    }

    public void enableLogOutBtn(boolean enable) {
        if (enable) {
            navLogOutLayout.setVisibility(View.VISIBLE);
        } else {
            navLogOutLayout.setVisibility(View.GONE);
        }
    }

    public void removeGroup(String groupName) {

        int getGroupId = 0;

        for (int i = 0; i < navItemsGroups.size(); i++) {
            if (navItemsGroups.get(i).getGroupName().equals(groupName)) {
                getGroupId = navItemsGroups.get(i).id;
                break;
            }
        }

        for (int l = 0; l < navItems.size(); l++) {
            if (navItems.get(l).groupId == getGroupId) {
                navItems.remove(l);
                l--;
            }
        }

        navigationAdapter.reloadNavigationBar(iconsColor, selectedTheme, selectedItemBackgroundColor, navItemTxtColor, navGroupTxtColor, selectedItemTextColor, selectedItemIconColor);
    }

    @SuppressLint("InflateParams")
    private void init() {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View headerView = layoutInflater.inflate(R.layout.nav_header, null);
        addHeaderView(headerView);

        View bodyView = layoutInflater.inflate(R.layout.nav_layout, null);
        addView(bodyView);

        profileName = headerView.findViewById(R.id.headerProfileName);
        headerImage = headerView.findViewById(R.id.headerImageView);
        headerRootLayout = headerView.findViewById(R.id.headerRootLayout);
        wishMessage = headerView.findViewById(R.id.headerWishMessage);

        wishMessage.setText(generateWishMessage());

        final RecyclerView navItemsRecyclerView = bodyView.findViewById(R.id.navItemsRecyclerView);
        bodyRootLayout = bodyView.findViewById(R.id.bodyRootLayout);
        navLogOutLayout = bodyView.findViewById(R.id.navLogOutLayout);
        navLogOutTxt = bodyView.findViewById(R.id.navLogOutTxt);

        navItemsRecyclerView.setHasFixedSize(true);
        navItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        navigationAdapter = new NavigationAdapter(context, navItems, navItemsGroups, iconsColor, selectedTheme, selectedItemBackgroundColor, navigationEventListener, navItemTxtColor, navGroupTxtColor, selectedItemIconColor, selectedItemTextColor);
        navItemsRecyclerView.setAdapter(navigationAdapter);

        navLogOutLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(navigationEventListener != null){
                    navigationEventListener.onLogOutBtnClick();
                }
            }
        });

        navigationEventListener = null;
    }

    private String generateWishMessage() {

        final int getCurrentHour = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));

        if (getCurrentHour < 12) {
            return "Good Morning";
        } else if (getCurrentHour > 12 && getCurrentHour < 18) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }

    }

    public enum IconsShape {
        OUTLINE,
        FILL
    }

    public enum NavColors {
        DEFAULT,
        GRAY,
        RED,
        DARK_RED,
        LIGHT_RED,
        BLACK,
        WHITE,
        ORANGE,
        DARK_ORANGE,
        LIGHT_ORANGE,
        YELLOW,
        BLUE,
        LIGHT_BLUE,
        DARK_BLUE
    }

    public enum DrawerGravity {
        LEFT,
        RIGHT
    }

    public enum NavThemes {
        LIGHT,
        DARK
    }
}

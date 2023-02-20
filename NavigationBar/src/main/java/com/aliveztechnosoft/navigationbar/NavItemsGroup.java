package com.aliveztechnosoft.navigationbar;


import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class NavItemsGroup {

    public int id;
    private final String groupName;
    private final List<NavItem> navItems;

    public NavItemsGroup(String groupName) {
        navItems = new ArrayList<>();
        this.groupName = groupName;

        NavigationBar.GROUPS_COUNT++;
        id = NavigationBar.GROUPS_COUNT;
    }

    public void addGroupItem(NavItem navItem) {
        navItem.groupId = id;
        navItems.add(navItem);
    }

    public void addGroupItems(List<NavItem> navItems) {
        for (int i = 0; i < navItems.size(); i++) {
            NavItem navItem = navItems.get(i);
            navItem.groupId = id;
            navItems.add(navItem);
        }
    }

    public void addGroupItem(NavItem.BuiltInItems builtInItems) {

        NavItem navItem = new NavItem();
        navItem.groupId = id;

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

        navItems.add(navItem);
    }

    public void addGroupItem(NavItem.BuiltInItems builtInItems, Fragment fragment, int fragmentContainerResId) {

        NavItem navItem = new NavItem();
        navItem.setFragment(fragment, fragmentContainerResId);
        navItem.groupId = id;

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

        navItems.add(navItem);
    }

    public List<NavItem> getGroupItems() {
        return navItems;
    }

    public String getGroupName(){
        return groupName;
    }
}

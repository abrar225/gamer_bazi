package com.aliveztechnosoft.gamerbaazi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aliveztechnosoft.gamerbaazi.cash.Wallet;
import com.aliveztechnosoft.gamerbaazi.home.HomeFragment;
import com.aliveztechnosoft.gamerbaazi.login_register.Login;
import com.aliveztechnosoft.gamerbaazi.profile.ProfileFragment;
import com.aliveztechnosoft.gamerbaazi.refer_earn.ReferEarnFragment;
import com.aliveztechnosoft.gamerbaazi.utilities.GamesUsername;
import com.aliveztechnosoft.gamerbaazi.utilities.MainData;
import com.aliveztechnosoft.gamerbaazi.utilities.RoomIds;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.aliveztechnosoft.navigationbar.CustomNavTheme;
import com.aliveztechnosoft.navigationbar.NavItem;
import com.aliveztechnosoft.navigationbar.NavItemsGroup;
import com.aliveztechnosoft.navigationbar.NavigationBar;
import com.aliveztechnosoft.navigationbar.NavigationEventListener;
import com.android.volley.Request;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements VolleyData, NavigationEventListener {

    public LinearLayout homeLayout;

    private DrawerLayout drawerLayout;
    private TextView userAmountTV;
    private ImageView referEarnIV, homeIV, profileIV;
    private TextView referEarnTV, homeTV, profileTV;
    private int selectedBottomItem = 1;
    private NavigationBar navigationBar;

    private RelativeLayout topBar;

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
        } else {
            // Explain to the user that the feature is unavailable because the
            // feature requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Navigation View
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationBar = findViewById(R.id.navigationBar);
        final ImageView navigationIcon = findViewById(R.id.navigationIcon);

        // Bottom View
        final LinearLayout referEarnLayout = findViewById(R.id.referEarnLayout);
        homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout profileLayout = findViewById(R.id.profileLayout);
        final LinearLayout addMoneyLayout = findViewById(R.id.addMoneyLayout);
        referEarnIV = findViewById(R.id.referEarnIV);
        homeIV = findViewById(R.id.homeIV);
        profileIV = findViewById(R.id.profileIv);
        referEarnTV = findViewById(R.id.referEarnTV);
        homeTV = findViewById(R.id.homeTV);
        profileTV = findViewById(R.id.profileTV);

        // top view
        userAmountTV = findViewById(R.id.userAmountTV);
        topBar = findViewById(R.id.topBar);

        // Initializing Firebase, setting topic name, getting token
        initFirebase();

        // initialize NavigationBar
        beautifyNavigationBar(navigationBar);

        // adding items to NavigationBar
        addItemsToNavigationBar(navigationBar);

        // adding Navigation Event listener
        navigationBar.setEventListener(this);

        // click listeners
        referEarnLayout.setOnClickListener(v -> selectFragment(0, referEarnIV, referEarnTV, R.drawable.earn_money_icon, new ReferEarnFragment()));
        homeLayout.setOnClickListener(v -> selectFragment(1, homeIV, homeTV, R.drawable.home_icon_sel, new HomeFragment()));
        profileLayout.setOnClickListener(v -> selectFragment(2, profileIV, profileTV, R.drawable.profile_icon_sel, new ProfileFragment()));

        addMoneyLayout.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Wallet.class)));
        userAmountTV.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Wallet.class)));
        navigationIcon.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        // set HomeFragment to FrameLayout by default
        homeLayout.performClick();

        // getting data from server
        getHomeData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);

        // register firebase topic
        FirebaseMessaging.getInstance().subscribeToTopic("gamers_baazi_2")
                .addOnCompleteListener(task -> {

                });

        // generate user firebase token for push notifications
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }

            if (task.getResult() != null) {
                updateUserFCMToken(task.getResult());
            }
        });
    }

    private void addItemsToNavigationBar(NavigationBar navigationBar) {
        navigationBar.addNavItem(NavItem.BuiltInItems.HOME);
        navigationBar.addNavItem(new NavItem("Refer & Earn", R.drawable.refer_earn_icon));
        navigationBar.addNavItem(new NavItem("Follow Us", R.drawable.instagram));
        navigationBar.addNavItem(new NavItem("Subscribe Us", R.drawable.youtube));

        NavItemsGroup other = new NavItemsGroup("Other");
        other.addGroupItem(NavItem.BuiltInItems.PRIVACY_POLICY);
        other.addGroupItem(NavItem.BuiltInItems.RATE_US);
        navigationBar.addItemsGroup(other);
    }

    private void beautifyNavigationBar(NavigationBar navigationBar) {

        CustomNavTheme customNavTheme = new CustomNavTheme();

        customNavTheme.setNavigationBackground(getResources().getColor(R.color.light_theme2));
        customNavTheme.setTextColor(getResources().getColor(R.color.white_60));
        customNavTheme.setGroupNameColor(getResources().getColor(R.color.white_30));
        customNavTheme.setIconsColor(getResources().getColor(R.color.white_60));
        customNavTheme.setHeaderWishTextColor(Color.WHITE);
        customNavTheme.setHeaderProfileNameTextColor(Color.WHITE);
        customNavTheme.setLogoutTextColor(Color.WHITE);

        navigationBar.setTheme(customNavTheme); // setting theme to NavigationBar

    }

    private void getHomeData() {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(MainActivity.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "home_data");

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, false, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (MyConstants.reloadCoinsAtHomeScreen) {
            MyConstants.reloadCoinsAtHomeScreen = false;

            final UserDetails userDetails = UserDetails.get(this, "");

            // setting user's available amount to TextView
            userAmountTV.setText(String.valueOf(userDetails.getWinAmount() + userDetails.getDepositAmount() + userDetails.getBonusAmount()));
        }
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        int accountBlockedStatus = 0;
        String accountBlockMsg = "";

        if (jsonObject.has("account_blocked")) {
            final JSONObject accountBlocked = jsonObject.getJSONObject("account_blocked");
            accountBlockedStatus = accountBlocked.getInt("blocked");
            accountBlockMsg = accountBlocked.getString("msg");
        }

        if (accountBlockedStatus == 0) {

            // updating user data
            final UserDetails userDetails = UserDetails.get(context, "").updateData(jsonObject.getJSONObject("single_user"));

            // updating game usernames
            GamesUsername.get(context, "").updateData(jsonObject.getJSONArray("games_usernames"));

            // updating room ids data
            final List<RoomIds> roomIdsList = RoomIds.get(context, "").updateData(jsonObject.getJSONArray("room_ids")).getArray();

            // updating user's game usernames

            // show room ids if available
            for (int i = 0; i < roomIdsList.size(); i++) {
                RoomIdDialog roomIdDialog = new RoomIdDialog(MainActivity.this, roomIdsList.get(i).getRoomId(), roomIdsList.get(i).getMessage());
                roomIdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                roomIdDialog.show();
            }

            // setting user's fullname in the navigation header
            navigationBar.setHeaderData(userDetails.getFullname(), R.drawable.bb_profile_pic);

            // setting user's profile pic in the n navigation header
            if (!userDetails.getProfilePic().isEmpty()) {
                Picasso.get().load(userDetails.getProfilePic()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        navigationBar.setHeaderData(userDetails.getFullname(), bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            // setting user's available amount to TextView
            userAmountTV.setText(String.valueOf(userDetails.getWinAmount() + userDetails.getDepositAmount() + userDetails.getBonusAmount()));

        } else {

            // show account blocked dialog
            AccountBlockDialog accountBlockDialog = new AccountBlockDialog(MainActivity.this, accountBlockMsg);
            accountBlockDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            accountBlockDialog.setCancelable(false);
            accountBlockDialog.show();
        }
    }

    public void selectFragment(int position, ImageView imageView, TextView textView, int imgRes, Fragment fragment) {

        selectedBottomItem = position;

        if (selectedBottomItem == 1) {
            topBar.setVisibility(View.VISIBLE);
        } else {
            topBar.setVisibility(View.GONE);
        }

        deSelectBottomItems();

        imageView.setImageResource(imgRes); // set selected image
        textView.setTextColor(Color.parseColor("#E1FFFFFF")); // set selected color

        // Create new fragment and transaction
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer, fragment, null);
        transaction.commit();
    }

    private void deSelectBottomItems() {

        referEarnIV.setImageResource(R.drawable.earn_money_icon2); // set non selected image
        homeIV.setImageResource(R.drawable.home_icon2); // set non selected image
        profileIV.setImageResource(R.drawable.profile_icon2); // set non selected image

        referEarnTV.setTextColor(Color.parseColor("#4BFFFFFF")); // set non selected color
        homeTV.setTextColor(Color.parseColor("#4BFFFFFF")); // set non selected color
        profileTV.setTextColor(Color.parseColor("#4BFFFFFF")); // set non selected color

    }

    private void updateUserFCMToken(String token) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(MainActivity.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "update_fcm");
        myVolley.put("token", token);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, false, 1);
    }

    @Override
    public void onBackPressed() {
        if (selectedBottomItem != 1) {
            selectFragment(1, homeIV, homeTV, R.drawable.home_icon_sel, new HomeFragment());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Exit");
            builder.setMessage("Are you want to exit the application?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialogInterface, i) -> finish());
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        }
    }

    @Override
    public void onItemSelected(int position, NavItem selectedNavItem) {
        final MainData mainData = MainData.get(MainActivity.this, "");

        if (position == 0) {
            selectFragment(1, homeIV, homeTV, R.drawable.home_icon_sel, new HomeFragment());
        } else if (position == 1) {
            selectFragment(0, referEarnIV, referEarnTV, R.drawable.earn_money_icon, new ReferEarnFragment());
        } else if (position == 2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mainData.getInstagram())));
        } else if (position == 3) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mainData.getYouTube())));
        } else if (position == 4) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mainData.getPrivacyPolicy())));
        } else if (position == 5) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mainData.getAppLink())));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onLogOutBtnClick() {

        // save empty string as user id
        MemoryData.saveData("user_id.txt", "", MainActivity.this);

        // opening login activity
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }
}

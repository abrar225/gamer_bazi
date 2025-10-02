package com.aliveztechnosoft.gamerbaazi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONArray;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class MyConstants {

    public static boolean reloadTournaments = false;
    public static boolean reloadCoinsAtHomeScreen = false;
    private static String lastSentLogTag = "";

    public static String getCertificate(Context context) {
        String certificate = "";
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);

                SigningInfo signingInfo = packageInfo.signingInfo;
                Signature[] signature = signingInfo.getApkContentsSigners();
                for (Signature value : signature) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(value.toByteArray());
                    certificate = java.util.Base64.getEncoder().encodeToString(md.digest());
                    break;
                }
            } else {
                @SuppressLint("PackageManagerGetSignatures") PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature value : packageInfo.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(value.toByteArray());
                    certificate = android.util.Base64.encodeToString(md.digest(), 0);
                    break;
                }

            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();

            // create error logs
            MyConstants.createLogs(context, "getCertificate -> " + e.getMessage(), e.getStackTrace(), "5");
        }

        return certificate;
    }

    /**
     * DP to PX and PX to Dp conversion functions
     */
    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Functions to handle URIs /  URLs
     */
    public static String uriToBase64(ContentResolver contentResolver, Uri uri) {
        String base64 = "";

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            base64 = BitmapFunctions.bitmapToBase64(bitmap);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return base64;
    }

    public static Map<String, String> getQueriesFromURL(String url) {

        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("filename", String.valueOf(System.currentTimeMillis()));

        String[] pairs = url.split("\\?");

        if (pairs.length > 1) {
            pairs = pairs[1].split("&");

            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }

    public static String encodeURIComponent(String urlEncodedString) {
        String result;

        result = urlEncodedString
                .replaceAll("\\+", "%20")
                .replaceAll("%21", "!")
                .replaceAll("%27", "'")
                .replaceAll("%28", "(")
                .replaceAll("%29", ")")
                .replaceAll("%7E", "~");

        return result;
    }

    /**
     * Ripple effect and functions to generate backgrounds
     */
    public static GradientDrawable createBackground(int color, float radius) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);

        return gradientDrawable;
    }

    public static GradientDrawable createBackground(int color, float radius, int strokeWidth, int strokeColor) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setCornerRadius(radius);

        return gradientDrawable;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static RippleDrawable createRippleEffect(int rippleColor, int backgroundColor) {
        return new RippleDrawable(getPressedState(rippleColor), createBackground(backgroundColor, 0), null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static RippleDrawable createRippleEffect(int rippleColor, int backgroundColor, int radius) {
        return new RippleDrawable(getPressedState(rippleColor), createBackground(backgroundColor, radius), null);
    }

    public static ColorStateList getPressedState(int rippleColor) {
        return new ColorStateList(new int[][]{new int[]{}}, new int[]{rippleColor});
    }

    public static String generateCountText(long count) {

        if (count < 999) {
            return String.valueOf(count);
        } else if (count < 999999) {
            return String.valueOf(count).substring(0, (String.valueOf(count).length() - 3)) + "k";
        } else {
            return String.valueOf(count).substring(0, (String.valueOf(count).length() - 6)) + "m";
        }
    }

    /**
     * Functions to generate logs and send to the server
     */
    public static void createLogs(Context context, String logTxt) {

        final String getLogs = MemoryData.getData("logs.txt", "", context);
        final String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.getDefault()).format(new Date());
        final String getUserId = MemoryData.getData("user_id.txt", "0", context);

        if (getLogs.isEmpty()) {
            MemoryData.saveData("logs.txt", "%new_line%%new_line%Time : " + currentDateTime + "  User id : " + getUserId + "  |  Logs : " + logTxt, context);
        } else {
            MemoryData.saveData("logs.txt", getLogs + "%new_line%%new_line%Time : " + currentDateTime + "  User id : " + getUserId + "  |  Logs : " + logTxt, context);
        }

        sendErrorLogsToServer(context);
    }

    public static void createLogs(Context context, String errorMsg, StackTraceElement[] stackTrace, String tag) {

        // check if same log is already sent to server then don;t send again
        if (lastSentLogTag.equals(tag)) {
            return;
        }

        String packageName = "com.aliveztechnosoft.gamerbaazi";
        StringBuilder logs = new StringBuilder();

        lastSentLogTag = tag;

        List<StackTraceElement> stackList = Arrays.asList(stackTrace);
        Collections.reverse(stackList);

        for (StackTraceElement ss : stackList) {
            if (ss.toString().contains(packageName)) {
                if (logs.length() == 0) {
                    logs = new StringBuilder(ss.toString().replaceAll(packageName, ""));
                } else {
                    logs.append("%new_line%").append(ss.toString().replaceAll(packageName, ""));
                }
            }
        }

        createLogs(context, errorMsg + "%new_line%Tag = " + tag + "%new_line%StackTrace = " + logs);
    }

    public static void sendErrorLogsToServer(Context context1) {

        // get logs from memory
        final String getLogs = MemoryData.getData("logs.txt", "", context1);

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(context1);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "create_logs");
        myVolley.put("logs", android.util.Base64.encodeToString(getLogs.getBytes(), android.util.Base64.DEFAULT));

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute((context, jsonObject, jsonArray, resultCode, requestUniqueId) -> MemoryData.saveData("logs.txt", "", context), false, 1);
    }

    /**
     * Functions to handle push notification events like received or clicked events
     *
     * @param action will be received or clicked
     */
    public static void acknowledgeNotification(Context context1, String action) {

        // get last saved campaign id from memory
        final String getCampaignId = MemoryData.getData("campaign_id.txt", "", context1);

        if (!getCampaignId.isEmpty()) {

            // Create MyVolley Object
            final MyVolley myVolley = new MyVolley(context1);

            // set request method
            myVolley.setMethod(Request.Method.POST);

            // setting URL
            myVolley.setCustomURL("https://push.lawctopus.com/APIs/index.php");

            // adding parameters
            myVolley.put("from", "acknowledge_notification");
            myVolley.put("type", action);
            myVolley.put("campaign_id", getCampaignId);

            if (action.equals("received") && Looper.myLooper() == null) {
                Looper.prepare();
            }

            // Make Volley Request. Response will be come in onResult overridden function below
            myVolley.execute((context, jsonObject, jsonArray, resultCode, requestUniqueId) -> {

                // removed last saved campaign id so it wont be acknowledged as clicked again and again
                if (action.equals("clicked")) {
                    MemoryData.saveData("campaign_id.txt", "", context);
                }
            }, false, 1);
        }
    }

    /*** To check whether app is opened from a notification. Also return payload data if any **/
    public static String checkIfOpenedFromNotification(Activity activity) {

        // get last saved campaign id from memory
        final String campaignId = activity.getIntent().getStringExtra("campaign_id");

        if (campaignId != null) {

            //acknowledgeNotification(activity, "clicked");

            return campaignId;
        }

        return null;
    }

    // ------------------------------------------------------------------------------------------------
    public static void loadInterstitialAd(Activity activity, AdRequest adRequest) {
        InterstitialAd.load(activity, activity.getString(R.string.interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                //interstitialAd.show(activity);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

            }
        });
    }

    public static void loadRewardedVideoAd(Activity activity, AdRequest adRequest) {
        RewardedAd.load(activity, activity.getString(R.string.reward_ad), adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                rewardedAd.show(activity, rewardItem -> {
//                });
            }
        });
    }

    public static boolean checkIfPlayerJoined(Context ctx, JSONArray joinedPlayers) {

        final List<String> joinedPlayersList = JSONFunctions.convertStrJSONArrayToList(ctx, joinedPlayers.toString());

        for (int i = 0; i < joinedPlayersList.size(); i++) {
            if (joinedPlayersList.get(i).equals(UserDetails.get(ctx, "").getId())) {
                return true;
            }
        }

        return false;
    }
}

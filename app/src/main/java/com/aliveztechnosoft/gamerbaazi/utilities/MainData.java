package com.aliveztechnosoft.gamerbaazi.utilities;

import android.content.Context;

import com.aliveztechnosoft.gamerbaazi.JSONFunctions;
import com.aliveztechnosoft.gamerbaazi.MemoryData;

import org.json.JSONArray;
import org.json.JSONObject;

public final class MainData {

    private String memoryFileName = "";

    private static Context ctx = null;

    // instance to hold reference of this class
    private static MainData instance = null;

    // JSONObject form of the utility class
    public JSONObject jsonObject = null;

    @Attributes(key = "version", dataType = DataTypes.INTEGER)
    private int version;

    @Attributes(key = "update_details")
    private String updateDetails;

    @Attributes(key = "app_link")
    private String appLink;

    @Attributes(key = "website_link")
    private String websiteLink;

    @Attributes(key = "youtube")
    private String youTube;

    @Attributes(key = "instagram")
    private String instagram;

    @Attributes(key = "privacy_policy")
    private String privacyPolicy;

    @Attributes(key = "refer_amount", dataType = DataTypes.FLOAT)
    private Float referralAmount;

    @Attributes(key = "min_withdraw", dataType = DataTypes.FLOAT)
    private Float minWithdraw;

    @Attributes(key = "share_txt")
    private String appShareTxt;

    @Attributes(key = "terms")
    private String terms;

    @Attributes(key = "announcements")
    private String announcements;

    @Attributes(key = "offline_payment_instructions")
    private String offlinePaymentInstructions;

    public MainData(Context context) {
        if (ctx == null) {
            ctx = context.getApplicationContext();
        }
    }

    public static MainData get(Context context, String memoryFileName) {

        if(memoryFileName.isEmpty()){
            memoryFileName = "main_data.txt";
        }

        // initialize instance variable if not initialized before
        if (instance == null || ctx == null || !memoryFileName.equals(instance.memoryFileName)) {
            instance = new MainData(context);
            instance.memoryFileName = memoryFileName;
        }

        if(instance.jsonObject == null){

            instance.jsonObject = MemoryData.getDataInJSONObjectForm(instance.memoryFileName, ctx);

            // convert json Object to class Object
            JSONFunctions.jsonObjectToClassObject(ctx, instance, instance.jsonObject);
        }

        // return the instance
        return instance;
    }

    /**
     * Get JSONObject form of this utility class
     */
    public JSONObject getJSONObjectForm() {
        if (instance.jsonObject == null) {
            instance.jsonObject = MemoryData.getDataInJSONObjectForm(instance.memoryFileName, ctx);
        }
        return instance.jsonObject;
    }

    /**
     * Update whole JSONArray / this class with new JSONArray
     */
    public MainData updateData(JSONObject jsonData) {
        MemoryData.saveData(instance.memoryFileName, jsonData.toString(), ctx);
        instance.jsonObject = null;
        return get(ctx, instance.memoryFileName);
    }

    /**
     * Remove all saved data
     */
    public MainData clearData() {
        return updateData(new JSONObject());
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public MainData updateSingleValue(String key, String value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateStringValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public MainData updateSingleValue(String key, int value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateIntValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public MainData updateSingleValue(String key, float value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateFloatValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public MainData updateSingleValue(String key, double value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateDoubleValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public MainData updateSingleValue(String key, JSONObject value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateJSONObjectValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public MainData updateSingleValue(String key, JSONArray value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateJSONArrayValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    public int getVersion() {
        return version;
    }

    public String getUpdateDetails() {
        return updateDetails;
    }

    public String getAppLink() {
        return appLink;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public String getYouTube() {
        return youTube;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public Float getReferralAmount() {
        return referralAmount;
    }

    public Float getMinWithdraw() {
        return minWithdraw;
    }

    public String getAppShareTxt() {
        return appShareTxt;
    }

    public String getTerms() {
        return terms;
    }

    public String getAnnouncements() {
        return announcements;
    }

    public String getOfflinePaymentInstructions() {
        return offlinePaymentInstructions;
    }
}

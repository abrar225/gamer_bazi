package com.aliveztechnosoft.gamerbaazi.utilities;

import android.content.Context;

import com.aliveztechnosoft.gamerbaazi.JSONFunctions;
import com.aliveztechnosoft.gamerbaazi.MemoryData;

import org.json.JSONArray;
import org.json.JSONObject;

public final class UserDetails {

    private String memoryFileName = "";

    private static Context ctx = null;

    // instance to hold reference of this class
    private static UserDetails instance = null;

    // JSONObject form of the utility class
    public JSONObject jsonObject = null;

    @Attributes(key = "id")
    private String id;

    @Attributes(key = "blocked", dataType = DataTypes.INTEGER)
    private int blocked;

    @Attributes(key = "email")
    private String email;

    @Attributes(key = "mobile")
    private String mobile;

    @Attributes(key = "password")
    private String password;

    @Attributes(key = "fullname")
    private String fullname;

    @Attributes(key = "profile_pic")
    private String profilePic;

    @Attributes(key = "referral_code")
    private String referralCode;

    @Attributes(key = "sponsor_id")
    private String sponsorId;

    @Attributes(key = "bonus_amount", dataType = DataTypes.FLOAT)
    private float bonusAmount;

    @Attributes(key = "deposit_amount", dataType = DataTypes.FLOAT)
    private float depositAmount;

    @Attributes(key = "win_amount", dataType = DataTypes.FLOAT)
    private float winAmount;

    @Attributes(key = "lifetime_winning", dataType = DataTypes.FLOAT)
    private float lifetimeWinning;

    @Attributes(key = "played_tournaments", dataType = DataTypes.INTEGER)
    private int playedTournaments;

    @Attributes(key = "won_tournaments", dataType = DataTypes.INTEGER)
    private int wonTournaments;


    public UserDetails(Context context) {
        if (ctx == null) {
            ctx = context.getApplicationContext();
        }
    }

    public static UserDetails get(Context context, String memoryFileName) {

        if(memoryFileName.isEmpty()){
            memoryFileName = "user_details.txt";
        }

        // initialize instance variable if not initialized before
        if (instance == null || ctx == null || !memoryFileName.equals(instance.memoryFileName)) {
            instance = new UserDetails(context);
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
    public UserDetails updateData(JSONObject jsonData) {
        MemoryData.saveData(instance.memoryFileName, jsonData.toString(), ctx);
        instance.jsonObject = null;
        return get(ctx, instance.memoryFileName);
    }

    /**
     * Remove all saved data
     */
    public UserDetails clearData() {
        return updateData(new JSONObject());
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public UserDetails updateSingleValue(String key, String value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateStringValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public UserDetails updateSingleValue(String key, int value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateIntValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public UserDetails updateSingleValue(String key, float value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateFloatValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public UserDetails updateSingleValue(String key, double value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateDoubleValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public UserDetails updateSingleValue(String key, JSONObject value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateJSONObjectValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public UserDetails updateSingleValue(String key, JSONArray value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateJSONArrayValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    public String getId() {
        return id;
    }

    public int getBlocked() {
        return blocked;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public float getBonusAmount() {
        return bonusAmount;
    }

    public float getDepositAmount() {
        return depositAmount;
    }

    public float getWinAmount() {
        return winAmount;
    }

    public float getLifetimeWinning() {
        return lifetimeWinning;
    }

    public int getPlayedTournaments() {
        return playedTournaments;
    }

    public int getWonTournaments() {
        return wonTournaments;
    }
}

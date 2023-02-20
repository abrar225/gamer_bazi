package com.aliveztechnosoft.gamerbaazi.utilities;

import android.content.Context;

import com.aliveztechnosoft.gamerbaazi.JSONFunctions;
import com.aliveztechnosoft.gamerbaazi.MemoryData;

import org.json.JSONArray;
import org.json.JSONObject;

public final class SimpleFile {

    private String memoryFileName = "";

    private static Context ctx = null;

    // instance to hold reference of this class
    private static SimpleFile instance = null;

    // JSONObject form of the utility class
    public JSONObject jsonObject = null;

    @Attributes(key = "version", dataType = DataTypes.INTEGER)
    private int version;

    public SimpleFile(Context context) {
        if (ctx == null) {
            ctx = context.getApplicationContext();
        }
    }

    public static SimpleFile get(Context context, String memoryFileName) {

        if(memoryFileName.isEmpty()){
            memoryFileName = "fileName.txt";
        }

        // initialize instance variable if not initialized before
        if (instance == null || ctx == null || !memoryFileName.equals(instance.memoryFileName)) {
            instance = new SimpleFile(context);
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
    public SimpleFile updateData(JSONObject jsonData) {
        MemoryData.saveData(instance.memoryFileName, jsonData.toString(), ctx);
        instance.jsonObject = null;
        return get(ctx, instance.memoryFileName);
    }

    /**
     * Remove all saved data
     */
    public SimpleFile clearData() {
        return updateData(new JSONObject());
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public SimpleFile updateSingleValue(String key, String value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateStringValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public SimpleFile updateSingleValue(String key, int value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateIntValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public SimpleFile updateSingleValue(String key, float value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateFloatValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public SimpleFile updateSingleValue(String key, double value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateDoubleValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public SimpleFile updateSingleValue(String key, JSONObject value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateJSONObjectValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    /**
     * Update single value of JSONObject / Update this class instance with new data
     */
    public SimpleFile updateSingleValue(String key, JSONArray value) {
        final JSONObject updatedJSONObject = JSONFunctions.updateJSONArrayValue(ctx, getJSONObjectForm(), key, value);
        return updateData(updatedJSONObject);
    }

    public int getVersion() {
        return version;
    }
}

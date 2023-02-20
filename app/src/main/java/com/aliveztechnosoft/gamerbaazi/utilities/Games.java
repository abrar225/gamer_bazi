package com.aliveztechnosoft.gamerbaazi.utilities;

import android.content.Context;

import com.aliveztechnosoft.gamerbaazi.JSONFunctions;
import com.aliveztechnosoft.gamerbaazi.MemoryData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Games {

    private String memoryFileName = "";

    private static Context ctx = null;

    // parent instance to hold reference of this class as parent
    private static Games parentInstance = null;

    // all the child instances for this class will be saved here
    public List<Games> allInstances = new ArrayList<>();

    // JSONObject form of single instance(this)
    public JSONObject jsonObject = null;

    // JSONArray form of this utility class
    private JSONArray jsonArray = null;

    // Instance position in allInstances array
    private int instancePosition;

    @Attributes(key = "id")
    public String gameId;

    @Attributes(key = "name")
    public String name;

    @Attributes(key = "image")
    public String image;

    @Attributes(key = "how_to_get_id")
    public String howToGetId;

    @Attributes(key = "tournaments", dataType = DataTypes.INTEGER)
    public int tournaments;

    private final Map<String, Games> mapInstanceById = new HashMap<>();

    public Games(Context context) {
        if (ctx == null) {
            ctx = context.getApplicationContext();
        }
    }

    public static Games get(Context context, String memoryFileName) {

        if (memoryFileName.isEmpty()) {
            memoryFileName = "games.txt";
        }

        // initialize parent instance if not initialized before
        if (parentInstance == null || ctx == null || !memoryFileName.equals(parentInstance.memoryFileName)) {
            parentInstance = new Games(context);
            parentInstance.memoryFileName = memoryFileName;
        }

        // If json data is in a JSONArray form then this class must have multiple instances corresponding to the each JSONObject in the JSONArray
        if (parentInstance.jsonArray == null) {
            parentInstance.jsonArray = MemoryData.getDataInJSONArrayForm(parentInstance.memoryFileName, ctx);
            parentInstance.jsonArrayToClassObjects(parentInstance.jsonArray);
        }

        // return the parent instance / instance
        return parentInstance;
    }

    /**
     * Get JSONArray form of this utility class
     */
    public JSONArray getJSONArrayForm() {
        if (parentInstance.jsonArray == null) {
            parentInstance.jsonArray = MemoryData.getDataInJSONArrayForm(parentInstance.memoryFileName, ctx);
        }
        return parentInstance.jsonArray;
    }

    /**
     * Get JSONObject form of the single instance of this utility class
     */
    public JSONObject getJSONObjectForm() {
        if (this.jsonObject == null) {
            this.jsonObject = JSONFunctions.getJSONObjectFromJSONArray(ctx, getJSONArrayForm(), getPosition());
        }
        return this.jsonObject;
    }

    /**
     * Update whole JSONArray / this class with new JSONArray
     */
    public Games updateData(JSONArray jsonArray) {
        MemoryData.saveData(parentInstance.memoryFileName, jsonArray.toString(), ctx);

        parentInstance.jsonArray = null;
        parentInstance.mapInstanceById.clear();

        if(jsonArray.length() < parentInstance.allInstances.size()){
            parentInstance.allInstances.clear();
        }

        return get(ctx, parentInstance.memoryFileName);
    }

    /**
     * Remove all saved data
     */
    public Games clearData() {
        return updateData(new JSONArray());
    }

    /**
     * Appending new JSONObjects to the JSONArray
     */
    public Games appendJSONArray(JSONArray jsonArray) {
        final JSONArray oldJSONArray = getJSONArrayForm();

        for (int i = 0; i < jsonArray.length(); i++) {
            oldJSONArray.put(JSONFunctions.getJSONObjectFromJSONArray(ctx, jsonArray, i));
        }
        return updateData(oldJSONArray);
    }

    /**
     * Appending new JSONObject (single object) to the JSONArray
     */
    public Games appendJSONObject(JSONObject jsonObject) {
        final JSONArray oldJSONArray = getJSONArrayForm();
        oldJSONArray.put(jsonObject);
        return updateData(oldJSONArray);
    }

    /**
     * Update single value of one of a JSONObject (JSONObject correspondence to this class instance) in JSONArray
     */
    public Games updateSingleValue(String key, String value) {
        return updateValue(DataTypes.STRING, key, value, 0, 0, 0, null, null);
    }

    /**
     * Update single value of one of a JSONObject (JSONObject correspondence to this class instance) in JSONArray
     */
    public Games updateSingleValue(String key, int value) {
        return updateValue(DataTypes.INTEGER, key, null, value, 0, 0, null, null);
    }

    /**
     * Update single value of one of a JSONObject (JSONObject correspondence to this class instance) in JSONArray
     */
    public Games updateSingleValue(String key, float value) {
        return updateValue(DataTypes.FLOAT, key, null, 0, value, 0, null, null);
    }

    /**
     * Update single value of one of a JSONObject (JSONObject correspondence to this class instance) in JSONArray
     */
    public Games updateSingleValue(String key, double value) {
        return updateValue(DataTypes.DOUBLE, key, null, 0, 0, value, null, null);
    }

    /**
     * Update single value of one of a JSONObject (JSONObject correspondence to this class instance) in JSONArray
     */
    public Games updateSingleValue(String key, JSONObject value) {
        return updateValue(DataTypes.JSONObject, key, null, 0, 0, 0, value, null);
    }

    /**
     * Update single value of one of a JSONObject (JSONObject correspondence to this class instance) in JSONArray
     */
    public Games updateSingleValue(String key, JSONArray value) {
        return updateValue(DataTypes.JSONArray, key, null, 0, 0, 0, null, value);
    }

    /**
     * Function to update single value of one of a JSONObject (JSONObject correspondence to this class instance) in JSONArray
     */
    private Games updateValue(DataTypes dataType, String key, String strValue, int intValue, float floatValue, double doubleValue, JSONObject jsonObjectValue, JSONArray jsonArrayValue) {

        JSONArray updatedJSONArray;

        // getting JSONArray form of this utility class
        final JSONArray jsonArray = getJSONArrayForm();

        // update value of a JSONObject in JSONArray
        if (dataType == DataTypes.INTEGER) {
            updatedJSONArray = JSONFunctions.updateIntValue(ctx, jsonArray, getPosition(), key, intValue);
        } else if (dataType == DataTypes.FLOAT) {
            updatedJSONArray = JSONFunctions.updateFloatValue(ctx, jsonArray, getPosition(), key, floatValue);
        } else if (dataType == DataTypes.DOUBLE) {
            updatedJSONArray = JSONFunctions.updateDoubleValue(ctx, jsonArray, getPosition(), key, doubleValue);
        } else if (dataType == DataTypes.JSONObject) {
            updatedJSONArray = JSONFunctions.updateJSONObjectValue(ctx, jsonArray, getPosition(), key, jsonObjectValue);
        } else if (dataType == DataTypes.JSONArray) {
            updatedJSONArray = JSONFunctions.updateJSONArrayValue(ctx, jsonArray, getPosition(), key, jsonArrayValue);
        } else {
            updatedJSONArray = JSONFunctions.updateStringValue(ctx, jsonArray, getPosition(), key, strValue);
        }

        // update JSONArray and this class
        return updateData(updatedJSONArray).getArray().get(getPosition());
    }

    public void jsonArrayToClassObjects(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            // getting instance if already created OR creating a new child instance to hold values from a JSONObject
            Games obj;
            if (i < allInstances.size()) {
                obj = allInstances.get(i);
            } else {
                obj = new Games(ctx);

                // add child instance to parent instance
                this.allInstances.add(obj);
            }

            // assign the instance position
            obj.instancePosition = i;

            obj.jsonObject = JSONFunctions.getJSONObjectFromJSONArray(ctx, jsonArray, i);

            // convert json Object to class Object
            JSONFunctions.jsonObjectToClassObject(ctx, obj, obj.jsonObject);

            mapInstanceById.put(obj.getId(), obj);
        }
    }

    public List<Games> getArray() {
        return allInstances;
    }

    public int getPosition() {
        return instancePosition;
    }

    public String getId() {
        return gameId;
    }

    public Games getGameById(String gameId) {
        return mapInstanceById.get(gameId);
    }

    public String getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getHowToGetId() {
        return howToGetId;
    }

    public int getTournaments() {
        return tournaments;
    }

}

package com.aliveztechnosoft.gamerbaazi;

import android.content.Context;
import android.widget.Toast;

import com.aliveztechnosoft.gamerbaazi.utilities.Attributes;
import com.aliveztechnosoft.gamerbaazi.utilities.DataTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class JSONFunctions {

    /**
     * GET FROM JSON
     */
    public static String getStringFromJSONObject(Context context, JSONObject jsonObject, String jsonObjectKey) {
        try {
            if (jsonObject.has(jsonObjectKey)) {
                return jsonObject.getString(jsonObjectKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "10");
        }

        return "";
    }

    public static float getFloatFromJSONObject(Context context, JSONObject jsonObject, String jsonObjectKey) {
        try {
            if (jsonObject.has(jsonObjectKey)) {
                return jsonObject.getLong(jsonObjectKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "11");
        }

        return 0;
    }

    public static int getIntFromJSONObject(Context context, JSONObject jsonObject, String jsonObjectKey) {
        try {
            if (jsonObject.has(jsonObjectKey)) {
                return jsonObject.getInt(jsonObjectKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "12");
        }

        return 0;
    }

    public static double getDoubleFromJSONObject(Context context, JSONObject jsonObject, String jsonObjectKey) {
        try {
            if (jsonObject.has(jsonObjectKey)) {
                return jsonObject.getDouble(jsonObjectKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "13");
        }

        return 0;
    }

    public static boolean getBooleanFromJSONObject(Context context, JSONObject jsonObject, String jsonObjectKey) {
        try {
            if (jsonObject.has(jsonObjectKey)) {
                return jsonObject.getBoolean(jsonObjectKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "14");
        }
        return false;
    }

    public static String getStringFromJSONArray(Context context, JSONArray jsonArray, int jsonObjectPosition, String jsonObjectKey) {
        try {
            if (jsonObjectPosition < jsonArray.length()) {
                if (jsonArray.getJSONObject(jsonObjectPosition).has(jsonObjectKey)) {
                    return jsonArray.getJSONObject(jsonObjectPosition).getString(jsonObjectKey);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "15");
        }

        return "";
    }

    public static String getStringFromJSONArray(Context context, JSONArray jsonArray, int jsonObjectPosition) {
        try {
            if (jsonObjectPosition < jsonArray.length()) {
                return jsonArray.getString(jsonObjectPosition);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "15");
        }

        return "";
    }

    public static int getIntFromJSONArray(Context context, JSONArray jsonArray, int jsonObjectPosition, String jsonObjectKey) {
        try {
            if (jsonObjectPosition < jsonArray.length()) {
                if (jsonArray.getJSONObject(jsonObjectPosition).has(jsonObjectKey)) {
                    return jsonArray.getJSONObject(jsonObjectPosition).getInt(jsonObjectKey);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "sfdgfasg575646");
        }

        return 0;
    }

    public static JSONObject getJSONObjectFromJSONArray(Context context, JSONArray jsonArray, int jsonObjectPosition) {
        JSONObject response = null;

        try {
            response = new JSONObject("{}");
            if (jsonObjectPosition < jsonArray.length()) {
                response = jsonArray.getJSONObject(jsonObjectPosition);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "16");
        }

        return response;
    }

    public static JSONObject getJSONObjectFromJSONObject(Context context, JSONObject jsonObject, String jsonObjectKey) {
        try {
            if (jsonObject.has(jsonObjectKey)) {
                return jsonObject.getJSONObject(jsonObjectKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "18");
        }
        return new JSONObject();
    }

    public static JSONArray getJSONOArrayFromJSONObject(Context context, JSONObject jsonObject, String jsonObjectKey) {
        JSONArray response = new JSONArray();

        try {
            if (jsonObject.has(jsonObjectKey)) {
                response = jsonObject.getJSONArray(jsonObjectKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "20");
        }

        return response;
    }

    /**
     * UPDATE Data in JSON
     */
    public static JSONObject updateStringValue(Context context, JSONObject jsonObject, String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "21");
        }

        return jsonObject;
    }

    public static JSONObject updateIntValue(Context context, JSONObject jsonObject, String key, int value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "sd87f64");
        }

        return jsonObject;
    }

    public static JSONObject updateJSONObjectValue(Context context, JSONObject jsonObject, String key, JSONObject value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "sd87f64");
        }

        return jsonObject;
    }

    public static JSONObject updateJSONArrayValue(Context context, JSONObject jsonObject, String key, JSONArray value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "sd87f64");
        }

        return jsonObject;
    }

    public static JSONObject updateFloatValue(Context context, JSONObject jsonObject, String key, float value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "sd87f64");
        }

        return jsonObject;
    }

    public static JSONObject updateDoubleValue(Context context, JSONObject jsonObject, String key, double value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "sd87f64");
        }

        return jsonObject;
    }

    public static JSONArray updateStringValue(Context context, JSONArray jsonArray, int jsonObjectPosition, String key, String value) {
        try {
            jsonArray.getJSONObject(jsonObjectPosition).put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "22");
        }
        return jsonArray;
    }

    public static JSONArray updateIntValue(Context context, JSONArray jsonArray, int jsonObjectPosition, String key, int value) {
        try {
            jsonArray.getJSONObject(jsonObjectPosition).put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "22");
        }
        return jsonArray;
    }

    public static JSONArray updateFloatValue(Context context, JSONArray jsonArray, int jsonObjectPosition, String key, float value) {
        try {
            jsonArray.getJSONObject(jsonObjectPosition).put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "22");
        }
        return jsonArray;
    }

    public static JSONArray updateDoubleValue(Context context, JSONArray jsonArray, int jsonObjectPosition, String key, double value) {
        try {
            jsonArray.getJSONObject(jsonObjectPosition).put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "22");
        }
        return jsonArray;
    }

    public static JSONArray updateJSONObjectValue(Context context, JSONArray jsonArray, int jsonObjectPosition, String key, JSONObject value) {
        try {
            jsonArray.getJSONObject(jsonObjectPosition).put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "22");
        }
        return jsonArray;
    }

    public static JSONArray updateJSONArrayValue(Context context, JSONArray jsonArray, int jsonObjectPosition, String key, JSONArray value) {
        try {
            jsonArray.getJSONObject(jsonObjectPosition).put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "22");
        }
        return jsonArray;
    }

    public static JSONArray updateJSONObjectInJSONArray(Context context, JSONArray jsonArray, int jsonObjectPosition, JSONObject jsonObject) {
        try {
            jsonArray.put(jsonObjectPosition, jsonObject);
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "19");
            return new JSONArray();
        }
    }

    /**
     * REMOVE DATA FROM JSON
     */
    public static JSONArray removeJSONArray(Context context, JSONArray jsonArray, int jsonObjectPosition) {
        jsonArray.remove(jsonObjectPosition);
        return jsonArray;
    }

    /**
     * CONVERSIONS (String to JSON, JSON to String)
     */
    public static JSONObject stringToJSONObject(Context context, String jsonData) {
        try {
            return new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "28");
        }
        return new JSONObject();
    }

    public static JSONArray stringToJSONArray(Context context, String jsonData) {
        try {
            return new JSONArray(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "29");
        }
        return new JSONArray();
    }

    public static List<String> convertStrJSONArrayToList(Context context, String jsonArray) {

        List<String> response = new ArrayList<>();
        try {
            JSONArray jsonArray2 = new JSONArray(jsonArray);
            for (int i = 0; i < jsonArray2.length(); i++) {
                response.add(jsonArray2.getString(i));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + ex.getMessage(), ex.getStackTrace(), "17");
        }

        return response;
    }

    public static List<String> convertJSONArrayToList(Context context, JSONArray jsonArray) {

        List<String> response = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                response.add(jsonArray.getString(i));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            MyConstants.createLogs(context, "JSONException -> " + ex.getMessage(), ex.getStackTrace(), "17");
        }

        return response;
    }

    public static JSONArray convertListToJSONArray(Context context, List<String> list) {

        final JSONArray response = new JSONArray();
        for (String data : list) {
            response.put(data);
        }
        return response;
    }

    public static void jsonObjectToClassObject(Context context, Object instance, JSONObject memoryData) {
        try {

            // getting class object
            Class<?> object = instance.getClass();

            // getting all fields of the class
            for (Field field : object.getDeclaredFields()) {

                // check if Attributes annotation in presented in the field
                if (field.isAnnotationPresent(Attributes.class)) {

                    // setting accessibility so value can be assigned to the field / variable
                    field.setAccessible(true);

                    // getting field attributes
                    Attributes attributes = field.getAnnotation(Attributes.class);
                    if (attributes != null) {

                        // checking if key if available, if not then it means field not supposed to be assigned a value
                        if (!attributes.key().isEmpty()) {
                            // assign value to a variable
                            if (attributes.dataType() == DataTypes.FLOAT) {
                                field.set(instance, JSONFunctions.getFloatFromJSONObject(context, memoryData, attributes.key()));
                            } else if (attributes.dataType() == DataTypes.INTEGER) {
                                field.set(instance, JSONFunctions.getIntFromJSONObject(context, memoryData, attributes.key()));
                            } else if (attributes.dataType() == DataTypes.DOUBLE) {
                                field.set(instance, JSONFunctions.getDoubleFromJSONObject(context, memoryData, attributes.key()));
                            } else if (attributes.dataType() == DataTypes.BOOLEAN) {
                                field.set(instance, JSONFunctions.getBooleanFromJSONObject(context, memoryData, attributes.key()));
                            } else if (attributes.dataType() == DataTypes.JSONObject) {
                                field.set(instance, JSONFunctions.getJSONObjectFromJSONObject(context, memoryData, attributes.key()));
                            } else if (attributes.dataType() == DataTypes.JSONArray) {
                                field.set(instance, JSONFunctions.getJSONOArrayFromJSONObject(context, memoryData, attributes.key()));
                            } else {
                                field.set(instance, JSONFunctions.getStringFromJSONObject(context, memoryData, attributes.key()));
                            }
                        }
                    }

                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {

            // create error logs
            MyConstants.createLogs(context, "IllegalAccessException -> " + e.getMessage(), e.getStackTrace(), "4");

            Toast.makeText(context, "Something went wrong (1015)" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static String convertJSONArrayToCommaSeparated(Context context, JSONArray jsonArray, int offset, int limit) {
        final StringBuilder data = new StringBuilder();

        // limit -1 means convert all elements of jsonArray to comma separated String
        if (limit == -1) {
            limit = jsonArray.length();
        }

        for (int i = offset; i < limit; i++) {
            if (i < jsonArray.length()) {
                if (data.length() == 0) {
                    data.append(JSONFunctions.getStringFromJSONArray(context, jsonArray, i));
                } else {
                    data.append(",").append(JSONFunctions.getStringFromJSONArray(context, jsonArray, i));
                }
            }
        }

        return data.toString();
    }

    public static JSONObject convertStrToJSONObject(Context context, String jsonObjectString) {
        try {
            return new JSONObject(jsonObjectString);
        } catch (JSONException e) {
            // create error logs
            MyConstants.createLogs(context, "IllegalAccessException -> " + e.getMessage(), e.getStackTrace(), "4gsdghshdhfxbbhrsd");

            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray convertStrToJSONArray(Context context, String jsonArrayString) {
        try {
            return new JSONArray(jsonArrayString);
        } catch (JSONException e) {
            // create error logs
            MyConstants.createLogs(context, "IllegalAccessException -> " + e.getMessage(), e.getStackTrace(), "asfasfasfgasgas4");

            e.printStackTrace();
            return null;
        }
    }
}

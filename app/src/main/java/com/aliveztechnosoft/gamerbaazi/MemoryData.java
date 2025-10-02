package com.aliveztechnosoft.gamerbaazi;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class MemoryData {

    public static void saveData(String filename, String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getData(String filename, String defaultValue, Context context) {
        String data = defaultValue;

        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject getDataInJSONObjectForm(String filename, Context context) {
        JSONObject data = new JSONObject();

        try {
            String rawData = getData(filename, "", context);
            if (!rawData.isEmpty()) {
                data = new JSONObject(rawData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONArray getDataInJSONArrayForm(String filename, Context context) {
        JSONArray data = new JSONArray();
        try {
            String rawData = getData(filename, "", context);
            if (!rawData.isEmpty()) {
                data = new JSONArray(rawData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}

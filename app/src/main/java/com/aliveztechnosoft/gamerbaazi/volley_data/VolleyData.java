package com.aliveztechnosoft.gamerbaazi.volley_data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyData {
    void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException;
}

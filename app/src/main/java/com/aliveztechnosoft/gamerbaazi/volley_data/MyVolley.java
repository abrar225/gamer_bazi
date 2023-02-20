package com.aliveztechnosoft.gamerbaazi.volley_data;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.aliveztechnosoft.gamerbaazi.BuildConfig;
import com.aliveztechnosoft.gamerbaazi.JSONFunctions;
import com.aliveztechnosoft.gamerbaazi.MemoryData;
import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.MyProgressDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class MyVolley {

    private final Map<String, String> params = new HashMap<>();
    private final JSONObject jsonObject = new JSONObject();
    private final HashMap<String, String> headers = new HashMap<>();

    private Context context;
    private String url = "";
    private int requestMethod;
    private String body;
    private int requestUniqueId;
    private static boolean errorFounded;

    public MyVolley(Context context) {
        this.context = context;
        this.requestMethod = Request.Method.GET;
        this.body = "";
        this.requestUniqueId = -10;
    }

    public void setRequestUniqueId(int uniqueId) {
        this.requestUniqueId = uniqueId;
    }

    public void setMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void putBody(String body) {
        this.body = body;
    }

    public void putHeader(String key, String value) {
        headers.put(key, value);
    }

    public void put(String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();

            // create error logs
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "33");
            Toast.makeText(context, "Something Went Wrong!!! (1000)", Toast.LENGTH_SHORT).show();
        }
    }

    public void execute(final VolleyData toAdd, boolean showProgress, final int resultCode) {

        try {
            jsonObject.put("version", String.valueOf(BuildConfig.VERSION_CODE));
            jsonObject.put("sha", MyConstants.getCertificate(context));

            if (!jsonObject.has("user_id")) {
                jsonObject.put("user_id", MemoryData.getData("user_id.txt", "", context));
            }
        } catch (JSONException e) {
            e.printStackTrace();

            // create error logs
            MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "34");
        }

        // getting encryption key from memory
        final String getEncryptionKey = MemoryData.getData("k.txt", "", context);

        // adding POST request parameters and encrypting them
        params.put("d", encrypt(getEncryptionKey, getEncryptionKey, jsonObject.toString()));

        final MyProgressDialog progressDialog = new MyProgressDialog(context);
        progressDialog.setCancelable(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (showProgress) {
            progressDialog.show();
        }

        context = context.getApplicationContext();

        if (url.isEmpty()) {
            url = MemoryData.getData("r.txt", "", context);
        }
        Log.e("volley_url", "FROM = " + JSONFunctions.getStringFromJSONObject(context, jsonObject, "from") + "  |  URL : " + url);
        StringRequest stringRequest = new StringRequest(requestMethod, url, response -> {

            final String decryptData = decrypt(getEncryptionKey, getEncryptionKey, response);
            Log.e("volley_res", "FROM = "+JSONFunctions.getStringFromJSONObject(context, jsonObject, "from")+"  |  Response : " + decryptData);
            try {
                if (!decryptData.isEmpty()) {

                    // check if string is JSONObject or JSONArray
                    final char lastCharacter = decryptData.trim().charAt(decryptData.trim().length() - 1);
                    final char firstCharacter = decryptData.trim().charAt(0);

                    if (firstCharacter == '{' && lastCharacter == '}') {
                        final JSONObject responseJSONObject = new JSONObject(decryptData);

                        // pass response as jsonObject to onResult
                        toAdd.onResult(context, responseJSONObject, new JSONArray(), resultCode, requestUniqueId);

                        errorFounded = false;
                    } else if (firstCharacter == '[' && lastCharacter == ']') {

                        // pass response as jsonArray to onResult
                        toAdd.onResult(context, new JSONObject(), new JSONArray(decryptData), resultCode, requestUniqueId);

                        errorFounded = false;
                    } else {

                        if (!errorFounded) {

                            Log.e("volley_error", "FROM = "+JSONFunctions.getStringFromJSONObject(context, jsonObject, "from")+"  |  Invalid Response : " + response);

                            // create error logs
                            MyConstants.createLogs(context, "Invalid Response from server, Response -> " + decryptData, Thread.currentThread().getStackTrace(), "35");

                            // Invalid server response
                            Toast.makeText(context, "Invalid response from server. Please try again later", Toast.LENGTH_SHORT).show();

                            errorFounded = true;
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();

                Log.e("volley_error", "FROM = "+JSONFunctions.getStringFromJSONObject(context, jsonObject, "from")+"  |  JSONException : " + e.getMessage());

                // create error logs
                MyConstants.createLogs(context, "JSONException -> " + e.getMessage(), e.getStackTrace(), "26");

                Toast.makeText(context, "Something Went Wrong!!! (1001)", Toast.LENGTH_SHORT).show();
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }, error -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            String response = "";

            if (error.getMessage() != null) {
                response = error.getMessage();
            } else if (error.networkResponse != null) {
                response = new String(error.networkResponse.data);
            }

            Log.e("volley_error", "FROM = "+JSONFunctions.getStringFromJSONObject(context, jsonObject, "from")+"  |  Network Error : " + error);
            if (!errorFounded) {

                // create error logs
                MyConstants.createLogs(context, "Network Error -> " + response, Thread.currentThread().getStackTrace(), "37");
                errorFounded = true;
            }

            Toast.makeText(context, "Something Went Wrong!!! (1002)", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public void setCustomURL(String customURL) {
        this.url = customURL;
    }

    private String encrypt(String key, String iv, String data) {

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedData = cipher.doFinal((data.getBytes()));

            String encryptedDataInBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT);
            String ivInBase64 = Base64.encodeToString(iv.getBytes(), Base64.DEFAULT);
            return encryptedDataInBase64 + ":" + ivInBase64;

        } catch (Exception ex) {

            // create error logs
            MyConstants.createLogs(context, "Encryption Exception -> " + ex.getMessage(), ex.getStackTrace(), "38");
            return "";
        }
    }

    private String decrypt(String key, String iv, String data) {

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedBytes = Base64.decode(data, Base64.DEFAULT);
            byte[] decryptedData = cipher.doFinal(encryptedBytes);
            return new String(decryptedData);
        } catch (Exception ex) {

            // create error logs
            MyConstants.createLogs(context, "Decryption Exception -> " + ex.getMessage(), ex.getStackTrace(), "39");
            return data;
        }
    }
}
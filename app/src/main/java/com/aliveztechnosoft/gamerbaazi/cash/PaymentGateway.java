package com.aliveztechnosoft.gamerbaazi.cash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.aliveztechnosoft.gamerbaazi.MemoryData;
import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class PaymentGateway extends AppCompatActivity implements VolleyData {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        webView = findViewById(R.id.webview);
        final LinearLayout redirectingLayout = findViewById(R.id.redirectingLayout);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                webView.setVisibility(View.VISIBLE);
                redirectingLayout.setVisibility(View.GONE);

                if (url.contains("payment_status.php")) {
                    if (url.contains("payment_failed")) {
                        finish();
                    } else {
                        getUserDetails();
                    }
                }
            }
        });

        webView.loadUrl(MemoryData.getData("r.txt", "", this) + "index.php?from=add_payment&user_id=" + UserDetails.get(this, "").getId() + "&amount=" + getIntent().getStringExtra("amount"));
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            new AlertDialog.Builder(PaymentGateway.this)
                    .setMessage("Are you sure want to cancel the transaction?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
        }
    }

    private void getUserDetails() {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(PaymentGateway.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "single_user");

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, false, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {
        MyConstants.reloadCoinsAtHomeScreen = true;
        UserDetails.get(context, "").updateData(jsonObject);
        finish();
    }
}

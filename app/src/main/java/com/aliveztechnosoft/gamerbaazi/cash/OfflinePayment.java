package com.aliveztechnosoft.gamerbaazi.cash;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.MainData;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OfflinePayment extends AppCompatActivity implements VolleyData {

    private String screenshotBase64 = "";
    private ImageView ssImageView;
    private final ActivityResultLauncher<String> imageChooser = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        if (result != null) {
            screenshotBase64 = MyConstants.uriToBase64(getContentResolver(), result);
            ssImageView.setImageURI(result);
        } else {
            screenshotBase64 = "";
            ssImageView.setImageBitmap(null);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_payment);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView instructionsTV = findViewById(R.id.instructionsTV);
        ssImageView = findViewById(R.id.ssImageView);
        final Button chooseSSBtn = findViewById(R.id.chooseSSBtn);
        final Button uploadBtn = findViewById(R.id.uploadBtn);

        instructionsTV.setText(MainData.get(this, "").getOfflinePaymentInstructions());

        chooseSSBtn.setOnClickListener(v -> imageChooser.launch("image/*"));

        uploadBtn.setOnClickListener(v -> {
            if (screenshotBase64.isEmpty()) {
                Toast.makeText(OfflinePayment.this, "Please choose an image first", Toast.LENGTH_SHORT).show();
            } else {
                updateScreenshot();
            }
        });

        backBtn.setOnClickListener(v -> finish());
    }

    private void updateScreenshot() {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(OfflinePayment.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "upload_screenshot");
        myVolley.put("screenshot", screenshotBase64);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        final int status = jsonObject.getInt("status");
        final String title = jsonObject.getString("title");
        final String message = jsonObject.getString("msg");

        if (status == 1) {
            ResponseDialog responseDialog = new ResponseDialog(OfflinePayment.this, title, message, status);
            responseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            responseDialog.show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
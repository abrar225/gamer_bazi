package com.aliveztechnosoft.gamerbaazi.login_register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.aliveztechnosoft.gamerbaazi.MainActivity;
import com.aliveztechnosoft.gamerbaazi.MemoryData;
import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.aliveztechnosoft.uidialogs.OTPEventListener;
import com.aliveztechnosoft.uidialogs.OTPVerificationDialog;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity implements VolleyData {

    private String mobile, email, fullName, password;
    private String profilePicBase64;
    private OTPVerificationDialog otpVerificationDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize XML Widgets
        final EditText fullNameET = findViewById(R.id.fullNameET);
        final EditText emailET = findViewById(R.id.emailET);
        final EditText mobileET = findViewById(R.id.mobileET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final EditText conPasswordET = findViewById(R.id.conPasswordET);
        final Button registerNowBtn = findViewById(R.id.registerNowBtn);
        final TextView signInBtn = findViewById(R.id.signInBtn);
        final TextView uploadProfilePicTV = findViewById(R.id.uploadProfilePicTV);
        final CircleImageView profilePicIV = findViewById(R.id.profilePicIV);

        signInBtn.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });

        // creating activity result launcher for Profile Pic
        final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            profilePicIV.setImageURI(result);
            profilePicBase64 = MyConstants.uriToBase64(getContentResolver(), result);
        });

        uploadProfilePicTV.setOnClickListener(view -> activityResultLauncher.launch("image/*"));

        registerNowBtn.setOnClickListener(v -> {

            // getting data from EditTexts into String
            fullName = fullNameET.getText().toString();
            mobile = mobileET.getText().toString();
            email = emailET.getText().toString();
            password = passwordET.getText().toString();
            final String getConPassword = conPasswordET.getText().toString();

            if (fullName.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
                Toast.makeText(Register.this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(getConPassword)) {
                Toast.makeText(Register.this, "Passwords not matching!!!", Toast.LENGTH_SHORT).show();
            } else {
                verifyOtp("5555");
                //sendOTP(mobile, email); // verify user through OTP
            }
        });
    }

    private void sendOTP(String mobile, String email) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(Register.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "send_otp");
        myVolley.put("mobile", mobile);
        myVolley.put("email", email);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    private void reSendOTP(String mobile) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(Register.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "resend_otp");
        myVolley.put("mobile", mobile);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 2);
    }

    private void verifyOtp(String otp) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(Register.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "verify_otp");
        myVolley.put("fullname", fullName);
        myVolley.put("otp", otp);
        myVolley.put("mobile", mobile);
        myVolley.put("email", email);
        myVolley.put("profile_pic", profilePicBase64);
        myVolley.put("password", password);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 3);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        if (resultCode == 1) {

            final int getStatus = jsonObject.getInt("status");
            final String getMessage = jsonObject.getString("msg");

            // show message as Toast
            Toast.makeText(context, getMessage, Toast.LENGTH_SHORT).show();

            if (getStatus == 1) {

                otpVerificationDialog = new OTPVerificationDialog(Register.this, mobile, new OTPEventListener() {
                    @Override
                    public void resendBtnClicked() {

                        // resend OTP
                        reSendOTP(mobile);
                    }

                    @Override
                    public void verifyBtnClicked(String otp) {

                        // verify OTP
                        verifyOtp(otp);
                    }
                });

                // show OTP Verification Dialog
                otpVerificationDialog.show();
            }

        } else if (resultCode == 2) {

            final String getMessage = jsonObject.getString("msg");

            // show message as Toast
            Toast.makeText(context, getMessage, Toast.LENGTH_SHORT).show();

        } else if (resultCode == 3) {

            final int getStatus = jsonObject.getInt("status");
            final String getMessage = jsonObject.getString("msg");

            // show message as Toast
            Toast.makeText(context, getMessage, Toast.LENGTH_SHORT).show();

            if (getStatus == 1) {

                // getting user id
                final String userId = jsonObject.getString("id");

                // saving user id to memory
                MemoryData.saveData("user_id.txt", userId, context);

                if (otpVerificationDialog != null && otpVerificationDialog.isShowing()) {
                    otpVerificationDialog.dismiss();
                }

                // open MainActivity
                startActivity(new Intent(Register.this, MainActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, Login.class));
        finish();
    }
}

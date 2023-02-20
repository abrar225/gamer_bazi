package com.aliveztechnosoft.gamerbaazi.cash;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class WithdrawDetailsDialog extends Dialog implements VolleyData {

    private final String type, amount;
    private final WithdrawListener withdrawListener;

    public WithdrawDetailsDialog(@NonNull Context context, String type, String amount, WithdrawListener withdrawListener) {
        super(context);
        this.type = type;
        this.amount = amount;
        this.withdrawListener = withdrawListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_details_dialog_layout);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final EditText numberET = findViewById(R.id.numberET);
        final Button withdrawNowBtn = findViewById(R.id.withdrawNowBtn);
        final LinearLayout bankDetailsLayout = findViewById(R.id.bankDetailsLayout);
        final EditText accountNumber = findViewById(R.id.accountNumberET);
        final EditText fullName = findViewById(R.id.fullNameET);
        final EditText bankName = findViewById(R.id.bankNameET);
        final EditText ifscCode = findViewById(R.id.IFSCET);

        if (type.equals("bank")) {
            bankDetailsLayout.setVisibility(View.VISIBLE);
            numberET.setVisibility(View.GONE);
        } else {
            bankDetailsLayout.setVisibility(View.GONE);
            numberET.setVisibility(View.VISIBLE);
        }

        withdrawNowBtn.setOnClickListener(view -> {
            final String getNumberTxt = numberET.getText().toString();
            final String getAccountNumber = accountNumber.getText().toString();
            final String getFullName = fullName.getText().toString();
            final String getBankName = bankName.getText().toString();
            final String getIFSCCode = ifscCode.getText().toString();

            if (type.equals("bank")) {
                if (getAccountNumber.isEmpty() || getFullName.isEmpty() || getBankName.isEmpty() || getIFSCCode.isEmpty()) {
                    Toast.makeText(getContext(), "All Fields Required", Toast.LENGTH_SHORT).show();
                } else {
                    makeWithdraw(amount, type, getAccountNumber, getFullName, getBankName, getIFSCCode, "");
                }
            } else {
                if (getNumberTxt.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
                } else {
                    makeWithdraw(amount, type, "", "", "", "", getNumberTxt);
                }
            }
        });

        backBtn.setOnClickListener(view -> dismiss());
    }

    private void makeWithdraw(String amount, String type, String accountNumber, String fullName, String bankName, String ifscCode, String mobileNumber) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(getContext());

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "withdraw_request");
        myVolley.put("amount", amount);
        myVolley.put("type", type);
        myVolley.put("account_number", accountNumber);
        myVolley.put("fullname", fullName);
        myVolley.put("bank_name", bankName);
        myVolley.put("ifsc_code", ifscCode);
        myVolley.put("mobile", mobileNumber);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        final int getStatus = jsonObject.getInt("status");
        final String getTitle = jsonObject.getString("title");
        final String getMessage = jsonObject.getString("msg");

        ResponseDialog responseDialog = new ResponseDialog(getContext(), getTitle, getMessage, getStatus);
        responseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        responseDialog.show();

        // dismiss dialog
        dismiss();

        if (getStatus == 1) {

            UserDetails.get(context, "").updateData(jsonObject.getJSONObject("user_details"));

            MyConstants.reloadCoinsAtHomeScreen = false;

            // notify Withdraw Amount activity
            withdrawListener.makeWithdraw();
        }

    }
}

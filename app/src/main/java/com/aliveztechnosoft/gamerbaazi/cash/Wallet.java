package com.aliveztechnosoft.gamerbaazi.cash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;

public final class Wallet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        // Initialize XML widgets
        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView userAmountTV = findViewById(R.id.userAmountTV);
        final Button addNowBtn = findViewById(R.id.addNowBtn);
        final Button offlinePayment = findViewById(R.id.offlinePayment);
        final Button withdrawAmountBtn = findViewById(R.id.withdrawAmountBtn);
        final EditText amountET = findViewById(R.id.amountET);

        userAmountTV.setText(String.valueOf(UserDetails.get(this, "").getDepositAmount()));

        addNowBtn.setOnClickListener(v -> {

            if (amountET.getText().toString().isEmpty()) {
                Toast.makeText(Wallet.this, "Please enter amount", Toast.LENGTH_SHORT).show();
            } else {
                final int getAmount = Integer.parseInt(amountET.getText().toString());

                if (getAmount < 10) {
                    Toast.makeText(Wallet.this, "Minimum amount must be of 10", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Wallet.this, PaymentGateway.class).putExtra("amount", String.valueOf(getAmount)));
                    finish();
                }
            }

        });

        offlinePayment.setOnClickListener(v -> startActivity(new Intent(Wallet.this, OfflinePayment.class)));
        withdrawAmountBtn.setOnClickListener(view -> startActivity(new Intent(Wallet.this, WithdrawAmount.class)));
        backBtn.setOnClickListener(v -> finish());
    }
}

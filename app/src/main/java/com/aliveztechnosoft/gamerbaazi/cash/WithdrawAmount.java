package com.aliveztechnosoft.gamerbaazi.cash;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.MainData;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;

public final class WithdrawAmount extends AppCompatActivity {
    private TextView userAmount;
    private MainData mainData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_amount);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final EditText withdrawAmount = findViewById(R.id.withdrawAmountET);
        final TextView minimumWithdraw = findViewById(R.id.minimumWithdrawTV);

        userAmount = findViewById(R.id.userBalanceTV);

        final RelativeLayout paytmWithdraw = findViewById(R.id.paytmWithdraw);
        final RelativeLayout gPayWithdraw = findViewById(R.id.gPayWithdraw);
        final RelativeLayout phonePayWithdraw = findViewById(R.id.phonePayWithdraw);
        final RelativeLayout bankWithdraw = findViewById(R.id.bankWithdraw);

        final TextView termsTV = findViewById(R.id.termsTV);

        // get main data from memory
        mainData = MainData.get(this, "");

        userAmount.setText(String.valueOf(UserDetails.get(this, "").getWinAmount()));

        minimumWithdraw.setText("You need minimum " + mainData.getMinWithdraw() + " winning amount to make withdraw request");

        // open terms link
        termsTV.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MainData.get(WithdrawAmount.this, "").getTerms()))));

        // make withdraw through withdraw methods
        paytmWithdraw.setOnClickListener(view -> makeWithdraw(withdrawAmount.getText().toString(), "paytm"));
        gPayWithdraw.setOnClickListener(view -> makeWithdraw(withdrawAmount.getText().toString(), "gpay"));
        phonePayWithdraw.setOnClickListener(view -> makeWithdraw(withdrawAmount.getText().toString(), "phone_pay"));
        bankWithdraw.setOnClickListener(view -> makeWithdraw(withdrawAmount.getText().toString(), "bank"));

        backBtn.setOnClickListener(view -> finish());
    }

    private void makeWithdraw(String amount, String type) {

        if (amount.isEmpty()) {
            Toast.makeText(WithdrawAmount.this, "Please enter amount", Toast.LENGTH_SHORT).show();
        } else if (Float.parseFloat(amount) < mainData.getMinWithdraw()) {
            Toast.makeText(WithdrawAmount.this, "Minimum withdraw amount is " + mainData.getMinWithdraw(), Toast.LENGTH_SHORT).show();
        } else if (Float.parseFloat(amount) > UserDetails.get(this, "").getWinAmount()) {
            Toast.makeText(WithdrawAmount.this, "Insufficient Balance!", Toast.LENGTH_SHORT).show();
        } else {
            WithdrawDetailsDialog withdrawDetailsDialog = new WithdrawDetailsDialog(WithdrawAmount.this, type, amount, () -> userAmount.setText(String.valueOf(UserDetails.get(WithdrawAmount.this, "").getWinAmount())));
            withdrawDetailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            withdrawDetailsDialog.show();
        }
    }
}
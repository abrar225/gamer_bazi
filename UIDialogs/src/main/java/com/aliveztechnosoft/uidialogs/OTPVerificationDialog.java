package com.aliveztechnosoft.uidialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class OTPVerificationDialog extends Dialog {

    // resend otp time in seconds
    private final int resendTime = 60;
    private final String mobileNumberTxt;
    private final OTPEventListener otpEventListener;
    private EditText otpET1, otpET2, otpET3, otpET4;
    private TextView resendBtn;
    private Button verifyBtn;
    // will be true after 60 seconds
    private boolean resendEnabled = false;
    private int selectedETPosition = 0;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() > 0) {

                if (selectedETPosition == 0) {

                    // select next edit text
                    selectedETPosition = 1;
                    showKeyboard(otpET2);
                } else if (selectedETPosition == 1) {

                    // select next edit text
                    selectedETPosition = 2;
                    showKeyboard(otpET3);
                } else if (selectedETPosition == 2) {

                    // select next edit text
                    selectedETPosition = 3;
                    showKeyboard(otpET4);
                } else {
                    verifyBtn.setBackgroundResource(R.drawable.round_back_red_100);
                }
            }
        }
    };

    public OTPVerificationDialog(@NonNull Context context, String mobileNumber, OTPEventListener otpEventListener) {
        super(context);
        this.mobileNumberTxt = mobileNumber;
        this.otpEventListener = otpEventListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.otp_dialog_layout);

        otpET1 = findViewById(R.id.otpET1);
        otpET2 = findViewById(R.id.otpET2);
        otpET3 = findViewById(R.id.otpET3);
        otpET4 = findViewById(R.id.otpET4);

        resendBtn = findViewById(R.id.resendBtn);
        verifyBtn = findViewById(R.id.verifyBtn);
        final TextView mobileNumber = findViewById(R.id.mobileNumber);

        otpET1.addTextChangedListener(textWatcher);
        otpET2.addTextChangedListener(textWatcher);
        otpET3.addTextChangedListener(textWatcher);
        otpET4.addTextChangedListener(textWatcher);

        // By default open Keyboard on first EditText
        showKeyboard(otpET1);

        // start countdown timer
        startCountDownTimer();

        // set mobile number to TextView
        mobileNumber.setText(mobileNumberTxt);

        resendBtn.setOnClickListener(v -> {
            if (resendEnabled) {
                otpEventListener.resendBtnClicked();
                startCountDownTimer();
            }
        });

        verifyBtn.setOnClickListener(v -> {

            final String getOTP = otpET1.getText().toString() + otpET2.getText().toString() + otpET3.getText().toString() + otpET4.getText().toString();
            otpEventListener.verifyBtnClicked(getOTP);

        });

    }

    private void showKeyboard(EditText otpET) {

        otpET.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer() {

        resendEnabled = false;
        resendBtn.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setText("Resend Code (" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                resendBtn.setText("Resend Code");
                resendBtn.setTextColor(getContext().getResources().getColor(R.color.secodary_color));
            }
        }.start();
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DEL) {

            if (selectedETPosition == 3) {

                // select previous Edit Text
                selectedETPosition = 2;
                showKeyboard(otpET3);
            } else if (selectedETPosition == 2) {

                // select previous Edit Text
                selectedETPosition = 1;
                showKeyboard(otpET2);
            } else if (selectedETPosition == 1) {

                // select previous Edit Text
                selectedETPosition = 0;
                showKeyboard(otpET1);
            }

            verifyBtn.setBackgroundResource(R.drawable.round_back_brown_100);
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }

    }
}

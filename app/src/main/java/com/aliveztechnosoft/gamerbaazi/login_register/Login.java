package com.aliveztechnosoft.gamerbaazi.login_register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.aliveztechnosoft.gamerbaazi.BitmapFunctions;
import com.aliveztechnosoft.gamerbaazi.BitmapLoadListener;
import com.aliveztechnosoft.gamerbaazi.BuildConfig;
import com.aliveztechnosoft.gamerbaazi.JSONFunctions;
import com.aliveztechnosoft.gamerbaazi.MainActivity;
import com.aliveztechnosoft.gamerbaazi.MemoryData;
import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.MyProgressDialog;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.MainData;
import com.aliveztechnosoft.gamerbaazi.utilities.NotificationData;
import com.aliveztechnosoft.gamerbaazi.utilities.Tournaments;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.aliveztechnosoft.uidialogs.UpdateDialog;
import com.android.volley.Request;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Login extends AppCompatActivity implements VolleyData {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize XML Widgets
        final EditText mobileNumber = findViewById(R.id.mobileNumber);
        final EditText password = findViewById(R.id.password);
        final AppCompatButton loginBtn = findViewById(R.id.loginBtn);
        final AppCompatImageView googleSignInBtn = findViewById(R.id.googleSignInBtn);
        final TextView signUpBtn = findViewById(R.id.signUpBtn);

        // initialize google admob ads
        MobileAds.initialize(this, initializationStatus -> {
        });

        final MyProgressDialog progressDialog = new MyProgressDialog(this);
        progressDialog.setCancelable(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        // getting API from Firebase. Firebase is secure so store your API in the Firebase Database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // getting root api
                final String getRootApi = snapshot.child("r").getValue(String.class);

                // getting encryption key for security
                final String getEncryptionKey = snapshot.child("k").getValue(String.class);

                if (getRootApi != null) {

                    progressDialog.dismiss();

                    // Save ROOT API to Memory
                    MemoryData.saveData("r.txt", getRootApi, Login.this);

                    if (getEncryptionKey != null) {

                        // saving encryption key to Memory
                        MemoryData.saveData("k.txt", getEncryptionKey, Login.this);
                    }

                    // get MainData from server (app_link, website_link, share_txt etc.)
                    getMainData(Login.this);

                } else {
                    Toast.makeText(Login.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
            }
        });

        signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
            finish();
        });

        loginBtn.setOnClickListener(v -> {

            final String mobileTxt = mobileNumber.getText().toString();
            final String passwordTxt = password.getText().toString();

            if (mobileTxt.isEmpty() || passwordTxt.isEmpty()) {
                Toast.makeText(Login.this, "Please enter mobile and password", Toast.LENGTH_SHORT).show();
            } else {

                // login user
                loginUser("", mobileTxt, passwordTxt, "", "");
            }
        });

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // check if user already logged in with google
        if (account != null) {
            mGoogleSignInClient.signOut(); //  Logout user
        }

        // creating activity result launcher for Google Sign In
        final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            final Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

            // handle google data
            handleSignInResult(task);

        });

        googleSignInBtn.setOnClickListener(v -> {
            final Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });

    }

    private void getMainData(Context context) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(context);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "main_data");

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    private void loginUser(String email, String mobile, String password, String fullName, String profilePicBase64) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(Login.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "login_user");
        myVolley.put("email", email);
        myVolley.put("mobile", mobile);
        myVolley.put("password", password);
        myVolley.put("fullname", fullName);
        myVolley.put("profile_pic", profilePicBase64);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 2);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        if (resultCode == 1) {

            // converting jsonObject to classObject
            final MainData mainData = MainData.get(context, "").updateData(jsonObject);

            // checking for update
            if (mainData.getVersion() > BuildConfig.VERSION_CODE) {

                // show update dialog
                showUpdateDialog(mainData);

            } else {

                // check if user already logged in. If already logged in then open MainActivity else stay on Login page
                if (!MemoryData.getData("user_id.txt", "", context).isEmpty()) {

                    final String campaignId = MyConstants.checkIfOpenedFromNotification(Login.this);

                    // check whether notification has payload. if notificationPayload it means user didn't open the app from notification
                    if (campaignId == null || campaignId.isEmpty()) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    } else {
                        final NotificationData notificationData = NotificationData.get(context, "").getNotificationByCampaignId(campaignId);

                        if (notificationData == null || notificationData.getDestination().isEmpty()) {
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        } else {

                            // TODO load home data here before opening destination activity
                            //loadHomeData();
                            final String destination = notificationData.getDestination();

                            if (destination.equals("tournaments")) {
                                final JSONObject payloadData = notificationData.getPayloadJSONObjectForm();
                                final String gameId = JSONFunctions.getStringFromJSONObject(Login.this, payloadData, "gameId");

                                startActivity(new Intent(Login.this, Tournaments.class));
                                finish();
                            }
                        }
                    }
                }
            }

        } else if (resultCode == 2) {

            // getting status. 0 means login failed and 1 means login success
            final int getStatus = jsonObject.getInt("status");
            final String getMessage = jsonObject.getString("msg"); // getting message

            // show message in toast if not empty
            if (!getMessage.isEmpty()) {
                Toast.makeText(this, getMessage, Toast.LENGTH_SHORT).show();
            }

            if (getStatus == 1) {

                // getting user id
                final String getUserId = jsonObject.getString("user_id");

                MemoryData.saveData("user_id.txt", getUserId, Login.this); // saving user id to memory

                // opening MainActivity
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();

            }
        }
    }

    private void showUpdateDialog(MainData mainData) {

        // show update available dialog
        final UpdateDialog updateDialog = new UpdateDialog(Login.this);

        updateDialog.setCancelable(false);

        // setting theme (one of Light or Dark)
        updateDialog.setDialogTheme(UpdateDialog.Theme.Dark);

        // passing updated version name to show on the dialog
        updateDialog.setVersionName("V." + mainData.getVersion());

        // passing new update details
        updateDialog.addUpdateDetails(mainData.getUpdateDetails().split(","));

        updateDialog.setUpdateBtnClickListener(() -> {

            // open app link to update the application
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mainData.getAppLink())));
        });

        // show dialog
        updateDialog.show();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {

                // getting email from google account
                final String getEmail = account.getEmail();

                // getting fullName from google account
                final String getFullName = account.getDisplayName();

                // getting profile pic as Bitmap
                if (account.getPhotoUrl() != null) {
                    BitmapFunctions.uriToBitmap(getContentResolver(), account.getPhotoUrl(), new BitmapLoadListener() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap) {
                            final String profilePicBase64 = BitmapFunctions.bitmapToBase64(bitmap);

                            // login user
                            loginUser(getEmail, "", "", getFullName, profilePicBase64);
                        }

                        @Override
                        public void onFailed(String error) {

                            // login user
                            loginUser(getEmail, "", "", getFullName, "");
                        }
                    });
                } else {

                    // login user
                    loginUser(getEmail, "", "", getFullName, "");
                }

            } else {
                Toast.makeText(Login.this, "Unable to fetch google account", Toast.LENGTH_SHORT).show();
            }


        } catch (ApiException e) {
            Toast.makeText(this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.aliveztechnosoft.gamerbaazi.profile;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends Dialog implements VolleyData {

    private final Context context;
    private String profilePicBase64;

    private String fullNameTxt;
    private ActivityResultLauncher<String> activityResultLauncher;
    private CircleImageView profilePic;
    private final ProfileFragment profileFragment;

    public UpdateProfile(@NonNull Context context, ProfileFragment profileFragment) {
        super(context);
        this.context = context;
        this.profileFragment = profileFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_layout);

        final ImageView backBtn = findViewById(R.id.backBtn);
        profilePic = findViewById(R.id.profilePic);
        final TextView changeProfilePicTV = findViewById(R.id.uploadProfilePicTV);
        final EditText fullNameET = findViewById(R.id.fullNameET);
        final AppCompatButton updateNowBtn = findViewById(R.id.updateNowBtn);

        // making empty
        this.profilePicBase64 = "";

        fullNameET.setText(UserDetails.get(context, "").getFullname());
        if (!UserDetails.get(context, "").getProfilePic().isEmpty()) {
            Picasso.get().load(UserDetails.get(context, "").getProfilePic()).into(profilePic);
        }

        backBtn.setOnClickListener(view -> dismiss());

        changeProfilePicTV.setOnClickListener(view -> activityResultLauncher.launch("image/*"));

        updateNowBtn.setOnClickListener(view -> {

            fullNameTxt = fullNameET.getText().toString();

            if (fullNameTxt.isEmpty()) {
                Toast.makeText(context, "Please enter fullname", Toast.LENGTH_SHORT).show();
            } else {
                updateProfile(context, fullNameTxt);
            }
        });
    }

    public void setProfilePic(Uri profilePic, String profilePicBase64) {
        this.profilePicBase64 = profilePicBase64;
        this.profilePic.setImageURI(profilePic);
    }

    public void setActivityLauncher(ActivityResultLauncher<String> activityResultLauncher) {
        this.activityResultLauncher = activityResultLauncher;
    }

    private void updateProfile(Context context, String fullName) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(context);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "update_profile");
        myVolley.put("fullname", fullName);
        myVolley.put("profile_pic", profilePicBase64);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        final int status = jsonObject.getInt("status");

        if (status == 1) {
            UserDetails.get(context, "").updateSingleValue("fullname", fullNameTxt);
            UserDetails.get(context, "").updateSingleValue("profile_pic", jsonObject.getString("profile_pic"));
        }
        dismiss();

        // refresh profile
        profileFragment.refreshProfile();
    }
}

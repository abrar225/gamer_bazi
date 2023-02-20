package com.aliveztechnosoft.gamerbaazi.profile;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;
import com.aliveztechnosoft.gamerbaazi.utilities.UserTransactions;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements VolleyData {

    private RecyclerView transactionsRecyclerView;
    private CircleImageView profileImage;
    private TextView fullName;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize XML Widgets
        final ImageView backBtn = view.findViewById(R.id.backBtn);
        profileImage = view.findViewById(R.id.profileImage);
        fullName = view.findViewById(R.id.fullNameTV);
        final TextView mobileOrEmail = view.findViewById(R.id.mobileTV);
        final AppCompatButton editProfileBtn = view.findViewById(R.id.editProfileBtn);
        final TextView playedTournaments = view.findViewById(R.id.playedTournaments);
        final TextView wonMatches = view.findViewById(R.id.wonMatches);
        final TextView lifetimeWinning = view.findViewById(R.id.lifeTimeWinning);
        final TextView depositCash = view.findViewById(R.id.depositCash);
        final TextView winCash = view.findViewById(R.id.winCash);
        final TextView bonusCash = view.findViewById(R.id.bonusCash);
        transactionsRecyclerView = view.findViewById(R.id.transactionsRecyclerView);

        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final UserDetails userDetails = UserDetails.get(requireContext(), "");

        // set user details to TextView fields
        fullName.setText(userDetails.getFullname());
        playedTournaments.setText(String.valueOf(userDetails.getPlayedTournaments()));
        wonMatches.setText(String.valueOf(userDetails.getWonTournaments()));
        lifetimeWinning.setText(String.valueOf(userDetails.getLifetimeWinning()));
        depositCash.setText(String.valueOf(userDetails.getDepositAmount()));
        winCash.setText(String.valueOf(userDetails.getWinAmount()));
        bonusCash.setText(String.valueOf(userDetails.getBonusAmount()));

        mobileOrEmail.setText(userDetails.getMobile());
        if (userDetails.getMobile().isEmpty()) {
            mobileOrEmail.setText(userDetails.getEmail());
        }

        if (!userDetails.getProfilePic().isEmpty()) {
            Picasso.get().load(userDetails.getProfilePic()).into(profileImage);
        }

        // creating activity result launcher for Profile Pic
        final UpdateProfile updateProfile = new UpdateProfile(requireContext(), ProfileFragment.this);

        final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                updateProfile.setProfilePic(result, MyConstants.uriToBase64(requireContext().getContentResolver(), result));
            }
        });

        editProfileBtn.setOnClickListener(view12 -> {
            updateProfile.setActivityLauncher(activityResultLauncher);
            updateProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            updateProfile.show();
        });

        // get user transactions
        getUserTransactions(getContext());
    }

    private void getUserTransactions(Context context) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(context);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "get_transactions");

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {
        UserTransactions.get(context, "").updateData(jsonArray);
        transactionsRecyclerView.setAdapter(new TransactionsAdapter(context));
    }

    public void refreshProfile() {

        // set user details to TextView fields
        fullName.setText(UserDetails.get(requireContext(), "").getFullname());

        if (!UserDetails.get(requireContext(), "").getProfilePic().isEmpty()) {
            Picasso.get().load(UserDetails.get(requireContext(), "").getProfilePic()).into(profileImage);
        }
    }
}

package com.aliveztechnosoft.gamerbaazi.home.games.tournaments.results;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TournamentResults extends AppCompatActivity implements VolleyData {

    private final List<ResultsList> resultsLists = new ArrayList<>();

    private CircleImageView profilePic1, profilePic2, profilePic3;
    private TextView fullName1, fullName2, fullName3;
    private TextView amount1, amount2, amount3;
    private CircleImageView myProfilePic;
    private TextView myWinning;
    private TextView myProfileName;
    private TextView myRank;
    private RelativeLayout userRankLayout;
    private RecyclerView resultsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_results);

        final ImageView backBtn = findViewById(R.id.backBtn);
        profilePic1 = findViewById(R.id.profilePic1);
        profilePic2 = findViewById(R.id.profilePic2);
        profilePic3 = findViewById(R.id.profilePic3);
        fullName1 = findViewById(R.id.profileName1);
        fullName2 = findViewById(R.id.profileName2);
        fullName3 = findViewById(R.id.profileName3);
        amount1 = findViewById(R.id.winning1);
        amount2 = findViewById(R.id.winning2);
        amount3 = findViewById(R.id.winning3);
        myProfilePic = findViewById(R.id.myProfilePic);
        myProfileName = findViewById(R.id.myProfileName);
        myWinning = findViewById(R.id.myWinning);
        myRank = findViewById(R.id.myRank);
        userRankLayout = findViewById(R.id.user_rank_view);
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);

        resultsRecyclerView.setHasFixedSize(true);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // setting results from server
        gettingResults(getIntent().getStringExtra("tournament_id"));
        backBtn.setOnClickListener(view -> finish());

        // ADMOB INTERSTITIAL AD
        AdRequest adRequest = new AdRequest.Builder().build();
        MyConstants.loadInterstitialAd(this, adRequest);
    }

    private void gettingResults(String tournamentId) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(TournamentResults.this);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "tournament_results");
        myVolley.put("tournament_id", tournamentId);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        for (int i = 0; i < jsonArray.length(); i++) {

            final String userId = jsonArray.getJSONObject(i).getString("user_id");
            final String fullName = jsonArray.getJSONObject(i).getString("fullname");
            final String profilePic = jsonArray.getJSONObject(i).getString("profile_pic");
            final String wonAmount = jsonArray.getJSONObject(i).getString("won_amount");

            if (i == 0) {
                if (!profilePic.isEmpty()) {
                    Picasso.get().load(profilePic).into(profilePic1);
                }
                fullName1.setText(fullName);
                amount1.setText(wonAmount);

            } else if (i == 1) {
                if (!profilePic.isEmpty()) {
                    Picasso.get().load(profilePic).into(profilePic2);
                }
                fullName2.setText(fullName);
                amount2.setText(wonAmount);
            } else if (i == 2) {
                if (!profilePic.isEmpty()) {
                    Picasso.get().load(profilePic).into(profilePic3);
                }
                fullName3.setText(fullName);
                amount3.setText(wonAmount);
            } else {

                if (userId.equals(UserDetails.get(this, "").getId())) {

                    if (!profilePic.isEmpty()) {
                        Picasso.get().load(profilePic).into(myProfilePic);
                    }

                    userRankLayout.setVisibility(View.VISIBLE);
                    myRank.setText(String.valueOf(i));
                    myProfileName.setText(fullName);
                    myWinning.setText(wonAmount);
                }

                ResultsList resultsList = new ResultsList(fullName, profilePic, wonAmount);
                resultsLists.add(resultsList);
            }
        }

        resultsRecyclerView.setAdapter(new ResultsAdapter(resultsLists));

    }
}

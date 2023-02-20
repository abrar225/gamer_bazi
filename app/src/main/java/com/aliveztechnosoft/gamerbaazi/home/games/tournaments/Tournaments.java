package com.aliveztechnosoft.gamerbaazi.home.games.tournaments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.Games;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.aliveztechnosoft.tablayout.TabLayout;
import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tournaments extends AppCompatActivity implements VolleyData {

    private RecyclerView tournamentsRecyclerView;
    private String selectedTabType = "upcoming";
    private LinearLayout noTournamentLayout;
    private Games selectedGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments);

        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView gameName = findViewById(R.id.gameName);
        tournamentsRecyclerView = findViewById(R.id.tournamentsRecyclerView);
        noTournamentLayout = findViewById(R.id.noTournamentLayout);

        // ADMOB  BANNER ADS
        final AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // ADMOB INTERSTITIAL AD
        MyConstants.loadInterstitialAd(this, adRequest);

        // configure TabLayout
        tabLayout.addTabItem("Ongoing");
        tabLayout.addTabItem("Upcoming");
        tabLayout.addTabItem("Results");
        tabLayout.setTabRadius(10);
        tabLayout.setSelected(1);

        gameName.setText("Tournaments");

        final String gameId = getIntent().getStringExtra("game_id");
        if (gameId == null) {
            return;
        }

        selectedGame = Games.get(this, "").getGameById(gameId);
        if (selectedGame == null) {
            return;
        }

        selectedTabType = "Upcoming".toLowerCase();
        getTournaments(Tournaments.this);
        noTournamentLayout.setVisibility(View.GONE);

        // on tab item select listener
        tabLayout.setOnTabItemSelectedListener((itemPosition, tabItem) -> {
            selectedTabType = tabItem.getItemName().toLowerCase();
            getTournaments(Tournaments.this);
            noTournamentLayout.setVisibility(View.GONE);
        });

        // configure recyclerview
        tournamentsRecyclerView.setHasFixedSize(true);
        tournamentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn.setOnClickListener(view -> finish());
    }

    private void getTournaments(Context context) {

        com.aliveztechnosoft.gamerbaazi.utilities.Tournaments.get(Tournaments.this, "").clearData();

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(context);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "get_tournaments");
        myVolley.put("game_id", selectedGame.getGameId());
        myVolley.put("type", selectedTabType);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {
        if (jsonArray.length() == 0) {
            noTournamentLayout.setVisibility(View.VISIBLE);
        } else {
            noTournamentLayout.setVisibility(View.GONE);
        }

        com.aliveztechnosoft.gamerbaazi.utilities.Tournaments.get(context, "").updateData(jsonArray);
        tournamentsRecyclerView.setAdapter(new TournamentsAdapter(Tournaments.this, selectedTabType));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(MyConstants.reloadTournaments){
            MyConstants.reloadTournaments = false;
            getTournaments(Tournaments.this);
        }
    }
}
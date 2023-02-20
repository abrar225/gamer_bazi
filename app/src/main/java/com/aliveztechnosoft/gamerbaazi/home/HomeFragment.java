package com.aliveztechnosoft.gamerbaazi.home;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.home.games.GamesAdapter;
import com.aliveztechnosoft.gamerbaazi.utilities.Games;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment implements VolleyData {

    private RecyclerView gamesRecyclerView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gamesRecyclerView = view.findViewById(R.id.gamesRecyclerView);

        // initialize recyclerview
        gamesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ADMOB  BANNER ADS
        final AdView adView = view.findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // ADMOB INTERSTITIAL AD
        MyConstants.loadInterstitialAd(requireActivity(), adRequest);

        // getting games from server
        getGames(requireContext());
    }

    private void getGames(Context context) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(context);

        // setting request method
        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "get_games");

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        Context activityContext = getContext();

        if (activityContext != null) {

            // updating games data
            Games.get(context, "").updateData(jsonArray);

            // setting adapter to recyclerview
            gamesRecyclerView.setAdapter(new GamesAdapter(activityContext));
        }
    }
}

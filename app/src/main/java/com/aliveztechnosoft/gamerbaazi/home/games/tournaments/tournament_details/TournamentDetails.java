package com.aliveztechnosoft.gamerbaazi.home.games.tournaments.tournament_details;

import static com.aliveztechnosoft.gamerbaazi.DateAndTimeFunctions.changeFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.RoomIdDialog;
import com.aliveztechnosoft.gamerbaazi.utilities.Games;
import com.aliveztechnosoft.gamerbaazi.utilities.RoomIds;
import com.aliveztechnosoft.gamerbaazi.utilities.Tournaments;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressLint("SetTextI18n")
public class TournamentDetails extends AppCompatActivity implements VolleyData {

    private TextView tournamentName, tournamentSchedule, prizePool, perKill, entryFees, type, mode, map, fromBonusTV;
    private LinearLayout layout2, perKillLayout, modeLayout, mapLayout;

    private TextView spotsLeftTV;
    private TextView timeLeftTV;
    private Button actionBtn;
    private CountDownTimer countdownTimer = null;
    private boolean canJoinTournament = false;
    private AdRequest adRequest;
    private Tournaments selectedTournament;
    private Games selectedGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);

        // back button
        final ImageView backBtn = findViewById(R.id.backBtn);

        tournamentName = findViewById(R.id.tournamentName);
        tournamentSchedule = findViewById(R.id.tournamentSchedule);
        prizePool = findViewById(R.id.prizePool);
        perKill = findViewById(R.id.perKill);
        entryFees = findViewById(R.id.entryFees);
        type = findViewById(R.id.type);
        mode = findViewById(R.id.mode);
        map = findViewById(R.id.map);
        spotsLeftTV = findViewById(R.id.spotsLeftTV);
        fromBonusTV = findViewById(R.id.fromBonusTV);

        layout2 = findViewById(R.id.layout2);
        perKillLayout = findViewById(R.id.perKillLayout);
        modeLayout = findViewById(R.id.modeLayout);
        mapLayout = findViewById(R.id.mapLayout);

        // recycler views
        final RecyclerView prizesRecyclerView = findViewById(R.id.prizesRecyclerView);
        final RecyclerView detailsRecyclerView = findViewById(R.id.detailsRecyclerView);

        // bottom view
        final CheckBox tcCheckBox = findViewById(R.id.tcCheckBox);
        actionBtn = findViewById(R.id.actionBtn);
        timeLeftTV = findViewById(R.id.timeLeftTV);

        // initialize recycler views
        detailsRecyclerView.setLayoutManager(new LinearLayoutManager(TournamentDetails.this));
        prizesRecyclerView.setLayoutManager(new LinearLayoutManager(TournamentDetails.this));

        // ADMOB  BANNER ADS
        final AdView adView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // ADMOB INTERSTITIAL AD
        MyConstants.loadInterstitialAd(this, adRequest);

        final String tournamentId = getIntent().getStringExtra("tournament_id");
        final String gameId = getIntent().getStringExtra("game_id");
        if (tournamentId == null || gameId == null) {
            return;
        }

        List<RoomIds> roomIdsList = RoomIds.get(this, "").getArray();

        // show room ids if available
        for (int i = 0; i < roomIdsList.size(); i++) {
            if (roomIdsList.get(i).getTournamentId().equals(tournamentId)) {
                RoomIdDialog roomIdDialog = new RoomIdDialog(this, roomIdsList.get(i).getRoomId(), roomIdsList.get(i).getMessage());
                roomIdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                roomIdDialog.show();
                break;
            }
        }

        selectedTournament = Tournaments.get(this, "").getTournamentById(tournamentId);
        selectedGame = Games.get(this, "").getGameById(gameId);
        if (selectedTournament == null || selectedGame == null) {
            return;
        }

        // setting adapters to RecyclerViews
        prizesRecyclerView.setAdapter(new DistributionAdapter(selectedTournament.getTournamentPrizes().getArray()));
        detailsRecyclerView.setAdapter(new DetailsAdapter(selectedTournament.getDetails()));

        // setting tournament data to the fields
        setTournamentData();
        getJoinedPlayer(TournamentDetails.this);

        actionBtn.setOnClickListener(v -> {
            if (!tcCheckBox.isChecked()) {
                Toast.makeText(TournamentDetails.this, "Please check details, Terms & Conditions", Toast.LENGTH_SHORT).show();
            } else if (canJoinTournament) {
                final JoinNowDialog joinNowDialog = new JoinNowDialog(TournamentDetails.this, selectedTournament, selectedGame, () -> {

                    getJoinedPlayer(TournamentDetails.this);

                    // ADMOB REWARD VIDEO AD
                    adRequest = new AdRequest.Builder().build();
                    MyConstants.loadRewardedVideoAd(TournamentDetails.this, adRequest);
                });
                joinNowDialog.show();
            } else {
                Toast.makeText(TournamentDetails.this, actionBtn.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void setTournamentData() {

        tournamentName.setText(selectedTournament.getTournamentName());

        // Set tournament schedule to TextView
        final String tournamentDate = changeFormat(selectedTournament.startDateTime, "", "dd-MM-yyyy");
        final String tournamentTime = changeFormat(selectedTournament.startDateTime, "", "hh:mm:ss a").toUpperCase();
        tournamentSchedule.setText("On " + tournamentDate + " At " + tournamentTime);

        int weight = 3;
        if (selectedTournament.getPerKill() == -1) {
            weight--;
            perKillLayout.setVisibility(View.GONE);
        }
        if (selectedTournament.getMap().isEmpty()) {
            weight--;
            mapLayout.setVisibility(View.GONE);
        }
        if (selectedTournament.getMode().equalsIgnoreCase("no_mode")) {
            weight--;
            modeLayout.setVisibility(View.GONE);
        }

        layout2.setWeightSum(weight);

        prizePool.setText(String.valueOf(selectedTournament.getPrizePool()));
        perKill.setText(String.valueOf(selectedTournament.getPerKill()));
        entryFees.setText(String.valueOf(selectedTournament.getEntryFees()));
        type.setText(selectedTournament.getType());
        mode.setText(selectedTournament.getMode());
        map.setText(selectedTournament.getMap());

        if (selectedTournament.getFromBonus() > 0) {
            fromBonusTV.setText("Note :- You can use " + selectedTournament.getFromBonus() + " from your Bonus Amount in case of insufficient balance (Winning + Deposit)");
        } else {
            fromBonusTV.setVisibility(View.GONE);
        }

    }

    private void getJoinedPlayer(Context context) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(context);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "get_joined_players");
        myVolley.put("tournament_id", selectedTournament.getTournamentId());

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        if (resultCode == 1) {

            // reload tournaments in Tournaments activity
            MyConstants.reloadTournaments = true;

            // update joined players
            selectedTournament = selectedTournament.updateSingleValue("joined_players", jsonArray);

            // getting spots left
            final int spotsLeft = selectedTournament.getTotalPlayers() - selectedTournament.getJoinedPlayers().length();

            if (spotsLeft > 0) {
                spotsLeftTV.setTextColor(Color.GREEN);
                spotsLeftTV.setText("Only " + spotsLeft + " Spots Left");
            } else {
                spotsLeftTV.setTextColor(Color.RED);
                spotsLeftTV.setText("Match Full");
            }

            // checking tournament status
            checkTournamentStatus(context, false);

            // start countdown timer
            startCountDown(selectedTournament.getStartDateTime());
        }
    }

    private void checkTournamentStatus(Context context, boolean registrationClosed) {

        // det default value
        canJoinTournament = false;

        if (MyConstants.checkIfPlayerJoined(context, selectedTournament.getJoinedPlayers())) {
            actionBtn.setText("Already Joined");
            actionBtn.setBackgroundResource(R.drawable.round_back_green);
        } else if (selectedTournament.getJoinedPlayers().length() == selectedTournament.getTotalPlayers()) {
            actionBtn.setText("Match Full");
            actionBtn.setBackgroundResource(R.drawable.round_back_red);
        } else if (registrationClosed) {
            actionBtn.setText("Registration Closed");
            actionBtn.setBackgroundResource(R.drawable.round_back_red);
        } else {
            canJoinTournament = true;
            actionBtn.setText("Join Now");
            actionBtn.setBackgroundResource(R.drawable.round_back_green);
        }
    }

    private void startCountDown(String startDateTime) {

        try {
            final Date tournamentEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(startDateTime);

            if (tournamentEndTime != null) {

                final long tournamentTimeStamps = tournamentEndTime.getTime();
                final long currentTimeStamps = System.currentTimeMillis();

                if (currentTimeStamps < tournamentTimeStamps) {
                    countdownTimer = new CountDownTimer((tournamentTimeStamps - currentTimeStamps), 1000) {
                        @Override
                        public void onTick(long l) {

                            String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(l),
                                    TimeUnit.MILLISECONDS.toMinutes(l) -
                                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                                    TimeUnit.MILLISECONDS.toSeconds(l) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

                            timeLeftTV.setText(time);
                        }

                        @Override
                        public void onFinish() {

                            // checking tournament status
                            checkTournamentStatus(TournamentDetails.this, true);
                        }
                    };
                    countdownTimer.start();
                } else {

                    // checking tournament status
                    checkTournamentStatus(TournamentDetails.this, true);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
        super.onBackPressed();
    }
}

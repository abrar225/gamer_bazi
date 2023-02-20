package com.aliveztechnosoft.gamerbaazi.home.games.tournaments.tournament_details;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.cash.Wallet;
import com.aliveztechnosoft.gamerbaazi.utilities.Games;
import com.aliveztechnosoft.gamerbaazi.utilities.GamesUsername;
import com.aliveztechnosoft.gamerbaazi.utilities.Tournaments;
import com.aliveztechnosoft.gamerbaazi.utilities.UserDetails;
import com.aliveztechnosoft.gamerbaazi.volley_data.MyVolley;
import com.aliveztechnosoft.gamerbaazi.volley_data.VolleyData;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JoinNowDialog extends Dialog implements VolleyData {

    private final Context context;
    private final Tournaments selectedTournament;
    private final com.aliveztechnosoft.gamerbaazi.utilities.Games selectedGame;
    private final JoinListener joinListener;
    private boolean insufficientBalance = false;

    JoinNowDialog(@NonNull Context context, Tournaments selectedTournament, Games selectedGame, JoinListener joinListener) {
        super(context);
        this.context = context;
        this.selectedTournament = selectedTournament;
        this.selectedGame = selectedGame;
        this.joinListener = joinListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_dialog_layout);

        final TextView howToGetGameId = findViewById(R.id.howToGetGameId);
        final TextView entryFees = findViewById(R.id.entryFeesTV);
        final TextView depositAmount = findViewById(R.id.deposiAmountTV);
        final TextView bonusAmount = findViewById(R.id.bonusAmountTV);
        final TextView winAmount = findViewById(R.id.winAmountTV);
        final TextView messageTV = findViewById(R.id.messageTV);
        final TextView fromBonusTV = findViewById(R.id.fromBonusTV);
        final AppCompatButton actionBtn = findViewById(R.id.actionBtn);
        final EditText gameIdET = findViewById(R.id.gameIdET);

        // setting game id to game id EditText
        final GamesUsername gameUserName = GamesUsername.get(context, "").getGameUsernameByGameId(selectedGame.getGameId());
        if (gameUserName != null) {
            gameIdET.setText(gameUserName.getUsername());
        }

        // User Details
        final UserDetails userDetails = UserDetails.get(context, "");

        // setting details to TextViews
        entryFees.setText(String.valueOf(selectedTournament.getEntryFees()));
        winAmount.setText(String.valueOf(userDetails.getWinAmount()));
        bonusAmount.setText(String.valueOf(userDetails.getBonusAmount()));
        depositAmount.setText(String.valueOf(userDetails.getDepositAmount()));

        float fromBonus = selectedTournament.getFromBonus();
        if (selectedTournament.getFromBonus() > 0) {
            fromBonusTV.setText("Note :- You can use " + selectedTournament.getFromBonus() + " from your Bonus Amount in case of insufficient balance (Winning + Deposit)");
            if (userDetails.getBonusAmount() < fromBonus) {
                fromBonus = userDetails.getBonusAmount();
            }
        } else {
            fromBonusTV.setVisibility(View.GONE);
        }

        // check if user has required amount to join the tournament
        if (selectedTournament.getEntryFees() <= (userDetails.getWinAmount() + userDetails.getDepositAmount() + fromBonus)) {
            actionBtn.setText("Participate Now");
            messageTV.setVisibility(View.GONE);
        } else {
            insufficientBalance = true;
            actionBtn.setText("Add Money");
            messageTV.setTextColor(Color.RED);
            messageTV.setText("You are unable to participate into the tournament because of insufficient balance in your account. Please add money to your account in order to participate in the tournament.");
        }


        actionBtn.setOnClickListener(v -> {

            final String gameUserIdTxt = gameIdET.getText().toString();

            if (gameUserIdTxt.isEmpty()) {
                Toast.makeText(context, "Please enter your game username", Toast.LENGTH_SHORT).show();
            } else {
                if (insufficientBalance) {
                    context.startActivity(new Intent(context, Wallet.class));
                    dismiss();
                } else {
                    joinTournament(context, gameUserIdTxt);
                }
            }
        });

        howToGetGameId.setOnClickListener(view -> {

            // get how to get game id tutorials link
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedGame.getHowToGetId())));
        });
    }

    private void joinTournament(Context context, String gameUserName) {

        // Create MyVolley Object
        final MyVolley myVolley = new MyVolley(context);

        myVolley.setMethod(Request.Method.POST);

        // Add POST data to Request
        myVolley.put("from", "join_tournament");
        myVolley.put("tournament_id", selectedTournament.getTournamentId());
        myVolley.put("game_username", gameUserName);

        // Make Volley Request. Response will be come in onResult overridden function below
        myVolley.execute(this, true, 1);
    }

    @Override
    public void onResult(Context context, JSONObject jsonObject, JSONArray jsonArray, int resultCode, int requestUniqueId) throws JSONException {

        final int status = jsonObject.getInt("status");
        final String message = jsonObject.getString("msg");

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        if (status == 1) {

            // updating user changes (deposit amount, win amount, bonus, after joined a tournament)
            UserDetails.get(context, "").updateData(jsonObject.getJSONObject("user_details"));
            GamesUsername.get(context, "").updateData(jsonObject.getJSONArray("games_usernames"));

            dismiss();
            joinListener.joined();
        }
    }
}

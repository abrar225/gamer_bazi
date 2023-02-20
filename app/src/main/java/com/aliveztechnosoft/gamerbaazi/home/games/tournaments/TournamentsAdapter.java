package com.aliveztechnosoft.gamerbaazi.home.games.tournaments;

import static com.aliveztechnosoft.gamerbaazi.DateAndTimeFunctions.changeFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.MyConstants;
import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.home.games.tournaments.results.TournamentResults;
import com.aliveztechnosoft.gamerbaazi.home.games.tournaments.tournament_details.TournamentDetails;
import com.aliveztechnosoft.gamerbaazi.utilities.Tournaments;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TournamentsAdapter extends RecyclerView.Adapter<TournamentsAdapter.MyViewHolder> {

    private final List<com.aliveztechnosoft.gamerbaazi.utilities.Tournaments> tournamentsLists;
    private final Context context;

    // values must be either ongoing, upcoming, results
    private final String type;

    public TournamentsAdapter(Context context, String type) {
        this.tournamentsLists = com.aliveztechnosoft.gamerbaazi.utilities.Tournaments.get(context, "").getArray();
        this.context = context;
        this.type = type;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public TournamentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tournaments_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentsAdapter.MyViewHolder holder, int position) {

        com.aliveztechnosoft.gamerbaazi.utilities.Tournaments tournamentsList = tournamentsLists.get(position);

        if (!tournamentsList.getImage().isEmpty()) {
            Picasso.get().load(tournamentsList.getImage()).into(holder.tournamentImage);
        }

        // setting tournament name to TextView
        holder.tournamentName.setText(tournamentsList.getTournamentName());

        // Set tournament schedule to TextView
        final String tournamentDate = changeFormat(tournamentsList.startDateTime, "", "dd-MM-yyyy");
        final String tournamentTime = changeFormat(tournamentsList.startDateTime, "", "hh:mm:ss a").toUpperCase();
        holder.tournamentSchedule.setText("On " + tournamentDate + " At " + tournamentTime);

        // setting prize pool to TextView
        holder.prizePool.setText(String.valueOf(tournamentsList.getPrizePool()));

        if (tournamentsList.getPerKill() == -1) {
            holder.perKillLayout.setVisibility(View.GONE);
            holder.linearLayout.setWeightSum(2);
        } else {
            holder.perKillLayout.setVisibility(View.VISIBLE);
            holder.linearLayout.setWeightSum(3);

            // setting per kill amount to TextView
            holder.perKill.setText(String.valueOf(tournamentsList.getPerKill()));
        }

        // setting entry fees
        holder.entryFees.setText(String.valueOf(tournamentsList.getEntryFees()));

        // setting total players
        holder.totalPlayers.setText(String.valueOf(tournamentsList.getTotalPlayers()));

        // setting spots left
        holder.spotsLeft.setText((tournamentsList.getTotalPlayers() - tournamentsList.getJoinedPlayers().length()) + " Spots Left");

        // setting progress
        holder.joinedProgress.setMax(tournamentsList.getTotalPlayers());
        holder.joinedProgress.setProgress(tournamentsList.getJoinedPlayers().length());

        if (type.equals("ongoing")) {
            holder.actionBtn.setText("Watch Now");
        } else if (type.equals("results")) {
            holder.actionBtn.setText("View Results");
        } else {
            if (MyConstants.checkIfPlayerJoined(context, tournamentsList.getJoinedPlayers())) {
                holder.actionBtn.setText("Already Joined");
                holder.actionBtn.setBackgroundResource(R.drawable.round_back_green);
            } else if (tournamentsList.getJoinedPlayers().length() == tournamentsList.getTotalPlayers()) {
                holder.actionBtn.setText("Match Full");
                holder.actionBtn.setBackgroundResource(R.drawable.round_back_red);
            } else {
                holder.actionBtn.setText("Join Now");
                holder.actionBtn.setBackgroundResource(R.drawable.round_back_green);
            }
        }

        // open details
        holder.tournamentImage.setOnClickListener(view -> performAction(tournamentsList));

        // open details
        holder.actionBtn.setOnClickListener(view -> performAction(tournamentsList));
    }

    private void performAction(Tournaments tournamentsList) {
        if (type.equals("ongoing")) {
            if (!tournamentsList.getYoutubeVideo().isEmpty()) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tournamentsList.getYoutubeVideo())));
            } else {
                Toast.makeText(context, "Not link found", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equals("results")) {
            context.startActivity(new Intent(context, TournamentResults.class).putExtra("tournament_id", tournamentsList.getTournamentId()));
        } else {
            Intent intent = new Intent(context, TournamentDetails.class);
            intent.putExtra("tournament_id", tournamentsList.getTournamentId());
            intent.putExtra("game_id", tournamentsList.getGameId());
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return tournamentsLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView tournamentImage;
        private final TextView tournamentName, tournamentSchedule, prizePool, perKill, entryFees, spotsLeft, totalPlayers;
        private final ProgressBar joinedProgress;
        private final Button actionBtn;
        private final LinearLayout perKillLayout, linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tournamentImage = itemView.findViewById(R.id.tournamentImage);
            tournamentName = itemView.findViewById(R.id.tournamentName);
            tournamentSchedule = itemView.findViewById(R.id.tournamentSchedule);
            prizePool = itemView.findViewById(R.id.prizePool);
            perKill = itemView.findViewById(R.id.perKill);
            entryFees = itemView.findViewById(R.id.entryFees);
            spotsLeft = itemView.findViewById(R.id.spotsLeftTV);
            totalPlayers = itemView.findViewById(R.id.totalPlayers);
            joinedProgress = itemView.findViewById(R.id.joinedProgress);
            actionBtn = itemView.findViewById(R.id.actionBtn);
            perKillLayout = itemView.findViewById(R.id.perKillLayout);
            linearLayout = itemView.findViewById(R.id.layout1);
        }
    }
}

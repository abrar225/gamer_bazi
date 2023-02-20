package com.aliveztechnosoft.gamerbaazi.home.games.tournaments.tournament_details;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.TournamentPrizes;

import java.util.List;

public class DistributionAdapter extends RecyclerView.Adapter<DistributionAdapter.MyViewHolder> {

    private final List<TournamentPrizes> prizeDistributions;

    public DistributionAdapter(List<TournamentPrizes> prizeDistributions) {
        this.prizeDistributions = prizeDistributions;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public DistributionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.distribution_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull DistributionAdapter.MyViewHolder holder, int position) {

        TournamentPrizes tournamentPrizes = prizeDistributions.get(position);

        holder.rankTV.setText(tournamentPrizes.getStartRank() + "-" + tournamentPrizes.getEndRank());
        holder.prizeTV.setText(tournamentPrizes.getAmount());
    }

    @Override
    public int getItemCount() {
        return prizeDistributions.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView rankTV, prizeTV;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTV = itemView.findViewById(R.id.rankTV);
            prizeTV = itemView.findViewById(R.id.prizeTV);
        }
    }
}

package com.aliveztechnosoft.gamerbaazi.home.games.tournaments.results;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.MyViewHolder> {

    private final List<ResultsList> resultsLists;

    public ResultsAdapter(List<ResultsList> resultsLists) {
        this.resultsLists = resultsLists;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ResultsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.results_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.MyViewHolder holder, int position) {
        ResultsList resultsList = resultsLists.get(position);

        holder.profileName.setText(resultsList.getFullName());
        holder.winAmount.setText(resultsList.getAmount());
        holder.rankTV.setText(String.valueOf(position + 3));

        if (!resultsList.getProfilePic().isEmpty()) {
            Picasso.get().load(resultsList.getProfilePic()).into(holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return resultsLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView rankTV, profileName, winAmount;
        private final CircleImageView profileImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rankTV = itemView.findViewById(R.id.c_rank);
            profileName = itemView.findViewById(R.id.profileName);
            winAmount = itemView.findViewById(R.id.winAmount);
            profileImage = itemView.findViewById(R.id.profileImage);
        }
    }
}

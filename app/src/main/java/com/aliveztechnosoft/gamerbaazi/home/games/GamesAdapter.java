package com.aliveztechnosoft.gamerbaazi.home.games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.home.games.tournaments.Tournaments;
import com.aliveztechnosoft.gamerbaazi.utilities.Games;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private final List<Games> gamesLists;
    private final Context context;

    public GamesAdapter(Context context) {
        this.gamesLists = Games.get(context, "").getArray();
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.games_adapter_layout, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Games game = gamesLists.get(position);

        holder.gameNameTV.setText(game.getName());

        if (!game.getImage().isEmpty()) {
            Picasso.get().load(game.getImage()).into(holder.gameImage);
        }
        if (game.getTournaments() > 0) {
            holder.tournamentsTV.setTextColor(Color.GREEN);
        } else {
            holder.tournamentsTV.setTextColor(Color.RED);
        }

        holder.tournamentsTV.setText("Tournaments : " + game.getTournaments());

        holder.gameImage.setOnClickListener(view -> {

            // opening Tournaments Activity
            final Intent intent = new Intent(context, Tournaments.class);
            intent.putExtra("game_id", game.getGameId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gamesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView gameImage;
        private final TextView gameNameTV;
        private final TextView tournamentsTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gameImage = itemView.findViewById(R.id.gameImage);
            gameNameTV = itemView.findViewById(R.id.gameNameTV);
            tournamentsTV = itemView.findViewById(R.id.tournamentsTV);
        }
    }
}

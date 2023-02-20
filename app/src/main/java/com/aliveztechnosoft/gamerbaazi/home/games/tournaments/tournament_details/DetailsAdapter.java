package com.aliveztechnosoft.gamerbaazi.home.games.tournaments.tournament_details;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.R;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {

    private final List<String> details;

    public DetailsAdapter(List<String> details) {
        this.details = details;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public DetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.MyViewHolder holder, int position) {
        holder.detailSno.setText((position + 1) + ".");
        holder.detailTxt.setText(details.get(position));
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView detailSno, detailTxt;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            detailSno = itemView.findViewById(R.id.detail_sno);
            detailTxt = itemView.findViewById(R.id.detail_txt);
        }
    }
}

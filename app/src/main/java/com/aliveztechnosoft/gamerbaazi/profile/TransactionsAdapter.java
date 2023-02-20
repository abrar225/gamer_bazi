package com.aliveztechnosoft.gamerbaazi.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliveztechnosoft.gamerbaazi.R;
import com.aliveztechnosoft.gamerbaazi.utilities.UserTransactions;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.MyViewHolder> {

    private final List<UserTransactions> transactionsLists;

    public TransactionsAdapter(Context context) {
        this.transactionsLists = UserTransactions.get(context, "").getArray();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public TransactionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.MyViewHolder holder, int position) {
        final UserTransactions userTransactions = transactionsLists.get(position);

        holder.title.setText(userTransactions.getTitle());
        holder.date.setText(userTransactions.getDate() + " T" + userTransactions.getTime());

        if (userTransactions.getAmount() < 0) {
            holder.amount.setText(String.valueOf(userTransactions.getAmount()));
            holder.amount.setTextColor(Color.parseColor("#DD2C00"));
        } else {
            holder.amount.setText("+" + userTransactions.getAmount());
            holder.amount.setTextColor(Color.parseColor("#00C853"));
        }
    }

    @Override
    public int getItemCount() {
        return transactionsLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView title, date, amount;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}

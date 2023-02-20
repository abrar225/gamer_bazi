package com.aliveztechnosoft.uidialogs;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpdateDetailsAdapter extends RecyclerView.Adapter<UpdateDetailsAdapter.MyViewHolder> {

    private List<String> updateDetailsList;
    private int textColor;

    public UpdateDetailsAdapter(List<String> updateDetailsList) {
        this.updateDetailsList = updateDetailsList;
        this.textColor = 0;
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public UpdateDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.update_details_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateDetailsAdapter.MyViewHolder holder, int position) {

        if(textColor != 0){
            holder.updateDetailsTxt.setTextColor(textColor);
        }
        holder.updateDetailsTxt.setText((position + 1) +". "+ updateDetailsList.get(position));

    }

    public void refreshAdapter(List<String> updateDetailsList) {
        this.updateDetailsList = updateDetailsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return updateDetailsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView updateDetailsTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            updateDetailsTxt = itemView.findViewById(R.id.updateDetailsTxt);
        }
    }
}

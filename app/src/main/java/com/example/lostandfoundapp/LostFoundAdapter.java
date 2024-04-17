package com.example.lostandfoundapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.ViewHolder> {
    private List<LostandFoundItem> lostAndFoundItems;
    private OnItemClickListener listener;

    // Interface to handle item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public LostFoundAdapter(List<LostandFoundItem> lostAndFoundItems, OnItemClickListener listener) {
        this.lostAndFoundItems = lostAndFoundItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lost_found, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LostandFoundItem item = lostAndFoundItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        Log.d("LostFoundAdapter", "getItemCount: " + lostAndFoundItems.size()); // Log the number of items in the adapter
        return lostAndFoundItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewType;
        TextView textViewName;
        TextView textViewPhone;
        TextView textViewDescription;
        TextView textViewDate;
        TextView textViewLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
        }

        public void bind(final LostandFoundItem item, final OnItemClickListener listener) {
            textViewType.setText(item.getType());
            textViewName.setText(item.getName());
            textViewPhone.setText(item.getPhone());
            textViewDescription.setText(item.getDescription());
            textViewDate.setText(item.getDate());
            textViewLocation.setText(item.getLocation());

            // Set click listener on the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}


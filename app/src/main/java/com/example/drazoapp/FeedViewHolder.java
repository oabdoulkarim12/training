package com.example.drazoapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedViewHolder extends RecyclerView.ViewHolder {

    public TextView itemName;
    public TextView itemPrice;
    public TextView itemLocation;
    public ImageView itemPhotos;
    public TextView description;

    public FeedViewHolder(@NonNull View itemView) {
        super(itemView);

        itemName = itemView.findViewById(R.id.item_name);
        itemPrice = itemView.findViewById(R.id.item_price);
        itemLocation = itemView.findViewById(R.id.item_localisation);

        itemPhotos = itemView.findViewById(R.id.grid_item);
        description = itemView.findViewById(R.id.desc);
    }
}

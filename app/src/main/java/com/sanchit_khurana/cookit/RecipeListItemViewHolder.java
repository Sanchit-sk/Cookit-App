package com.sanchit_khurana.cookit;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView itemThumbnail;
    public TextView itemLabel;
    public CardView itemCardView;
    public TextView itemChef;

    public RecipeListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemThumbnail = itemView.findViewById(R.id.foodlist_item_thumbnail);
        itemLabel = itemView.findViewById(R.id.foodlist_item_label);
        itemCardView = itemView.findViewById(R.id.fooditem_card);
        itemChef = itemView.findViewById(R.id.foodlist_item_chef);
    }
}

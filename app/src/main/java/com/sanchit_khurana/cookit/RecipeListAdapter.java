package com.sanchit_khurana.cookit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListItemViewHolder> {
    private List<FoodItem> foodItemsList;
    private static final String TAG = "RecipeListAdapter";
    private final OnItemSelect itemSelectCallback;

    interface OnItemSelect{
        void onItemSelect(FoodItem selectedItem);
    }

    public RecipeListAdapter(OnItemSelect callBack){
        itemSelectCallback = callBack;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListItemViewHolder holder, int position) {
       FoodItem currentFoodItem = foodItemsList.get(position);
       holder.itemLabel.setText(currentFoodItem.getLabel());
        Picasso.get()
                .load(currentFoodItem.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(holder.itemThumbnail);
        holder.itemChef.setText(currentFoodItem.getChef());

        holder.itemCardView.setTag(currentFoodItem);
        holder.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelectCallback.onItemSelect((FoodItem) view.getTag());
            }
        });
    }

    @NonNull
    @Override
    public RecipeListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodlist_item_layout, parent , false);
        return new RecipeListItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (foodItemsList != null) ? foodItemsList.size() : 0;
    }

    public void onNewRecipeList(List<FoodItem> newList){
        Log.d(TAG, "onNewRecipeList: Called");
        foodItemsList = newList;
        notifyDataSetChanged();
    }
}

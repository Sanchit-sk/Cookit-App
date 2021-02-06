package com.sanchit_khurana.cookit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_details);

        Toolbar toolbar = findViewById(R.id.itemdetails_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        FoodItem selectedFoodItem = (FoodItem) intent.getSerializableExtra("SELECTED_FOOD_ITEM");
        TextView foodItemLabel = findViewById(R.id.fooditem_label);
        TextView foodItemChef = findViewById(R.id.fooditem_chef);
        ImageView foodItemImage = findViewById(R.id.fooditem_image);

        if(selectedFoodItem != null){
            foodItemLabel.setText(selectedFoodItem.getLabel());
            foodItemChef.setText(selectedFoodItem.getChef());
            Picasso.get()
                    .load(selectedFoodItem.getImageUrl())
                    .placeholder(R.drawable.chef_icon)
                    .error(R.drawable.placeholder_image)
                    .into(foodItemImage);

            List<Ingredient> itemIngredients = selectedFoodItem.getItemIngredients();
            ViewGroup parentView = findViewById(R.id.ingredients_parent_view);

            for(int i=0;i<itemIngredients.size();i++){
                View ingredientView = LayoutInflater.from(this).inflate(R.layout.ingredient_item_layout, parentView,false);
                TextView ingredientName = ingredientView.findViewById(R.id.ingredient_name);
                ImageView ingredientImage = ingredientView.findViewById(R.id.ingredient_image);
                ingredientName.setText(itemIngredients.get(i).getIngredientName());

                if(itemIngredients.get(i).getIngredientPhotoUrl() != null){
                    Picasso.get().load(itemIngredients.get(i).getIngredientPhotoUrl())
                            .error(R.drawable.chef_icon)
                            .placeholder(R.drawable.chef_icon)
                            .into(ingredientImage);
                }

                parentView.addView(ingredientView, i+1);
            }
        }
    }
}
package com.sanchit_khurana.cookit;

import java.io.Serializable;
import java.util.List;

public class FoodItem implements Serializable {
    private static final long serialVersionUID=1L;
    private String imageUrl;
    private String label;
    private String chef;
    private List<Ingredient> itemIngredients;
    private Double itemCalories;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public List<Ingredient> getItemIngredients() {
        return itemIngredients;
    }

    public void setItemIngredients(List<Ingredient> itemIngredients) {
        this.itemIngredients = itemIngredients;
    }

    public Double getItemCalories() {
        return itemCalories;
    }

    public void setItemCalories(Double itemCalories) {
        this.itemCalories = itemCalories;
    }

    @Override
    public String toString() {
        String result =  "FoodItem{" +
                "imageUrl='" + imageUrl + '\n' +
                "label: " + label + '\n' +
                "chef: " + chef + '\n' +
                "itemCalories: " + itemCalories;

        for(int i=0;i<itemIngredients.size();i++){
            result += itemIngredients.get(i).toString();
        }

        result += '}';

        return result;
    }
}

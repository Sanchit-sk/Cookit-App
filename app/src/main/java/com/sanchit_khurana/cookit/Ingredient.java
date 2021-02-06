package com.sanchit_khurana.cookit;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private final long serialVersionUID = 1L;
    private String ingredientName;
    private String ingredientPhotoUrl;

    public Ingredient(String ingredientName, String ingredientPhotoUrl){
        this.ingredientName = ingredientName;
        this.ingredientPhotoUrl = ingredientPhotoUrl;
    }

    public String getIngredientName(){
        return this.ingredientName;
    }

    public String getIngredientPhotoUrl(){
        return this.ingredientPhotoUrl;
    }

    @Override
    public String toString() {
        return '\n'+"Ingredient{" + '\n'+
                "ingredientName: " + ingredientName + '\n' +
                "ingredientPhotoUrl: " + ingredientPhotoUrl + '\n' +
                '}';
    }
}

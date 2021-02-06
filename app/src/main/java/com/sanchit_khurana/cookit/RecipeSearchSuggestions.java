package com.sanchit_khurana.cookit;

import android.content.SearchRecentSuggestionsProvider;

public class RecipeSearchSuggestions extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.sanchit_khurana.cookit.RecipeSearchSuggestions";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecipeSearchSuggestions(){
        setupSuggestions(AUTHORITY,MODE);
    }
}

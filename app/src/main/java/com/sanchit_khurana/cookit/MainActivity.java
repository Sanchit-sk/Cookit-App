package com.sanchit_khurana.cookit;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetRecipeList.OnRecipeListAvailable,
               RecipeListAdapter.OnItemSelect {
    private String searchQuery = null;
    private static final String TAG = "MainActivity";
    private RecyclerView foodList;
    private RecipeListAdapter recipeListAdapter;
    private final String queryRetrieveKey = "SEARCHED_QUERY";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchRecentSuggestions suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);

        suggestions = new SearchRecentSuggestions(this,RecipeSearchSuggestions.AUTHORITY,RecipeSearchSuggestions.MODE);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            suggestions.saveRecentQuery(query,null);
            searchQuery = query;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPreferences.edit().putString(queryRetrieveKey, query).apply();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: Called");
                GetRecipeList refreshedRecipeList = new GetRecipeList(MainActivity.this);
                refreshedRecipeList.execute(searchQuery);
            }
        });

        foodList = findViewById(R.id.foodlist);
        foodList.setLayoutManager(new LinearLayoutManager(this));

        recipeListAdapter = new RecipeListAdapter(this);
        foodList.setAdapter(recipeListAdapter);

        Log.d(TAG, "onCreate: Starts");
//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute("https://api.edamam.com/search?app_id=b8939818&app_key=42670557ef175c9e8735b976b0c48c85&q=pastry");
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: Starts");
        String defaultQuery = "n";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        searchQuery = sharedPreferences.getString(queryRetrieveKey,null);
        GetRecipeList getRecipeList = new GetRecipeList(this);
        if(searchQuery == null){
            searchQuery = defaultQuery;
            sharedPreferences.edit().putString(queryRetrieveKey,defaultQuery).apply();
        }
        getRecipeList.execute(searchQuery);
        mSwipeRefreshLayout.setRefreshing(true);
        super.onResume();
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: Submitted Query: " + query);
                if(query.length() == 0){
                    Toast.makeText(MainActivity.this,"Please enter something first!",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "onQueryTextSubmit: New query updated");
                    GetRecipeList getNewRecipeList = new GetRecipeList(MainActivity.this);
                    getNewRecipeList.execute(query);
                    mSwipeRefreshLayout.setRefreshing(true);
                    searchQuery = query;
                    suggestions.saveRecentQuery(query,null);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().putString(queryRetrieveKey,query).apply();
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_clear_history){
            suggestions.clearHistory();
        }

        if(id == R.id.menu_refresh){
            mSwipeRefreshLayout.setRefreshing(true);
            GetRecipeList getNewRecipeList = new GetRecipeList(this);
            getNewRecipeList.execute(searchQuery);
        }
        return true;
    }

    @Override
    public void onRecipeListAvailable(List<FoodItem> foodItems) {

        if(foodItems != null){
            Log.d(TAG, "onRecipeListAvailable: Cancelling the refresh loading");
            mSwipeRefreshLayout.setRefreshing(false);
            recipeListAdapter.onNewRecipeList(foodItems);
        }
    }

    @Override
    public void onItemSelect(FoodItem selectedItem) {
        Log.d(TAG, "onItemSelect: " + selectedItem.toString());
        Intent intent = new Intent(MainActivity.this,FoodItemDetailsActivity.class);
        intent.putExtra("SELECTED_FOOD_ITEM", selectedItem);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: App Exited successfully");
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
}
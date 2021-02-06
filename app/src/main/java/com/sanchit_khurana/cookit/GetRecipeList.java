package com.sanchit_khurana.cookit;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetRecipeList extends AsyncTask<String, Void, List<FoodItem>> implements GetRawData.OnRawDataDownloaded {
    private static final String TAG = "GetRecipeList";
    private List<FoodItem> mFoodItemList = null;
    private final OnRecipeListAvailable mCallback;

    public GetRecipeList(OnRecipeListAvailable callback) {
        mCallback = callback;
    }

    interface OnRecipeListAvailable{
        void onRecipeListAvailable(List<FoodItem> foodItems);
    }

    @Override
    protected void onPostExecute(List<FoodItem> foodItems) {
        if(mCallback != null){
            mCallback.onRecipeListAvailable(foodItems);
        }
    }

    @Override
    protected List<FoodItem> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: Starts");
        String resultUri;

        if(strings != null){
            String userQuery = strings[0];
            UriBuilder uriBuilder = new UriBuilder();
            resultUri = uriBuilder.buildUri(userQuery);
        }else{
            Log.d(TAG, "doInBackground: Null query passed");
            return null;
        }

        GetRawData getRawData = new GetRawData(this);
        getRawData.getRawDataOnSameThread(resultUri);

        Log.d(TAG, "doInBackground: Ended");

        return mFoodItemList;
    }

    @Override
    public void onRawDataDownloaded(String downloadedData, RawDataDownloadStatus status) {
        Log.d(TAG, "onRawDataDownloaded: Downloaded data: " + downloadedData);

        if(status == RawDataDownloadStatus.OK){
          processJsonData(downloadedData);
        }
    }

    private void processJsonData(String jsonData){
        mFoodItemList = new ArrayList<>();

        try{
            JSONObject jsonDoc = new JSONObject(jsonData);
            JSONArray itemList = jsonDoc.getJSONArray("hits");

            for(int i=0;i<itemList.length();i++){
                FoodItem foodItem = new FoodItem();
                JSONObject currentFoodItem = itemList.getJSONObject(i);
                JSONObject itemData = currentFoodItem.getJSONObject("recipe");

                foodItem.setLabel(itemData.getString("label"));
                foodItem.setImageUrl(itemData.getString("image"));
                foodItem.setChef(itemData.getString("source"));

                JSONArray jsonIngredientsList = itemData.getJSONArray("ingredients");
                List<Ingredient> currentItemIngredients = new ArrayList<>();
                for(int j=0;j<jsonIngredientsList.length();j++){
                    JSONObject currentJsonIngredient = jsonIngredientsList.getJSONObject(j);
                    String ingredientText = currentJsonIngredient.getString("text");
                    String ingredientImageUrl = currentJsonIngredient.getString("image");
                    Ingredient currentIngredient = new Ingredient(ingredientText,ingredientImageUrl);
                    currentItemIngredients.add(currentIngredient);
                }

                foodItem.setItemIngredients(currentItemIngredients);
                foodItem.setItemCalories(itemData.getDouble("calories"));

                mFoodItemList.add(foodItem);
            }
        }catch(JSONException e){
            Log.d(TAG, "processJsonData: Exception while parsing the json data" + e.getMessage());
            e.printStackTrace();
        }
    }
}

package com.sanchit_khurana.cookit;

import android.net.Uri;
import android.util.Log;

public class UriBuilder {
    private static final String TAG = "UriBuilder";
    private final String appKey = "42670557ef175c9e8735b976b0c48c85";
    private final String appId = "b8939818";
    private final String baseUri = "https://api.edamam.com/search";

    public String buildUri(String query){
        Log.d(TAG, "buildUri: Called with a query: " + query);

        return Uri.parse(this.baseUri).buildUpon()
                .appendQueryParameter("app_key", this.appKey)
                .appendQueryParameter("app_id", this.appId)
                .appendQueryParameter("q", query)
                .build().toString();
    }
}

package com.sanchit_khurana.cookit;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum RawDataDownloadStatus {IDLE , NOT_INITIALIZED, OK, FAILED, PROCESSING}

public class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    RawDataDownloadStatus status;
    OnRawDataDownloaded mCallback;

    interface OnRawDataDownloaded{
        void onRawDataDownloaded(String downloadedData, RawDataDownloadStatus status);
    }

    public GetRawData(OnRawDataDownloaded callback) {
        mCallback = callback;
        status = RawDataDownloadStatus.IDLE;
    }

    @Override
    protected void onPostExecute(String rawData) {

        if(mCallback != null){
            mCallback.onRawDataDownloaded(rawData, status);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... urlPaths) {
        Log.d(TAG, "doInBackground: Starts");

        if(urlPaths != null){
            String urlPath = urlPaths[0];
            Log.d(TAG, "doInBackground: Received Url: " + urlPath);
            String loadedData = downloadData(urlPath);
            return loadedData;
        }else{
            status = RawDataDownloadStatus.NOT_INITIALIZED;
            return null;
        }
    }

    void getRawDataOnSameThread(String urlPath){
        Log.d(TAG, "getRawDataOnSameThread: started");
        if(mCallback != null){
            mCallback.onRawDataDownloaded(doInBackground(urlPath),status);
        }
        Log.d(TAG, "getRawDataOnSameThread: ended");
    }

    String downloadData(String urlPath){
        Log.d(TAG, "downloadData: Called");
        BufferedReader reader = null;

        try{
            status = RawDataDownloadStatus.PROCESSING;
            StringBuilder dataBuilder = new StringBuilder();
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "downloadData: Response code: " + responseCode);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for(String line = reader.readLine(); line != null; line = reader.readLine()){
                dataBuilder.append(line).append('\n');
            }

            status = RawDataDownloadStatus.OK;
            return dataBuilder.toString();

        }catch(MalformedURLException me){
            Log.d(TAG, "downloadData: Invalid Url provided: " + me.getMessage());
        }catch(IOException e){
            Log.d(TAG, "downloadData: Failed with an IO Exception: " + e.getMessage());
        }catch(SecurityException e){
            Log.d(TAG, "downloadData: App permissions missing: " + e.getMessage());
        }finally{
            try{
                reader.close();
            }catch(IOException e){
                Log.d(TAG, "downloadData: Failed to close the reader: " + e.getMessage());
            }
        }

        status = RawDataDownloadStatus.FAILED;
        return null;
    }
}

package android.abhik.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by abmitra on 6/14/2015.
 */
public class FetchReviewsAsync extends AsyncTask<Long,Integer,  ArrayList<String>> {
    private String TAG = "PopularMovies - FetchMoviesAsyncTask";
    private final String URL = "api.themoviedb.org";
    private final String API_KEY = "a670cb5c49630b38e1ca06f0cd82b8eb";
    private Event event;
    public FetchReviewsAsync(Event event){
        super();
        this.event=event;

    }

    @Override
    protected ArrayList<String> doInBackground(Long... params) {
        Long id = params[0];
        Uri.Builder builder = new Uri.Builder();
        builder
                .scheme("http")
                .authority(URL)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Long.toString(id))
                .appendPath("reviews")
                .appendQueryParameter("api_key", API_KEY);
        String jsonStr = getDataFromServer(builder.build().toString());
        return getTrailers(jsonStr);
    }

    protected ArrayList<String> getTrailers(String jsonStr){
        ArrayList<String> trailer = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray list = jsonObj.getJSONArray("results");
            for(int i =0;i< list.length();i++){
                JSONObject item = list.getJSONObject(i);
                String t = item.getString("content");
                trailer.add(t);

            }
        } catch (JSONException e ){
            Log.e(TAG, "Internet is probably off", e);
        }
        catch (NullPointerException e){
            Log.e(TAG, "Internet is probably off", e);
        }
        catch (Exception e){
            Log.e(TAG, "Internet is probably off", e);
        }
        return trailer;


    }
    protected  String getDataFromServer(String urlStr){
        HttpURLConnection urlConnection = null;
        String result = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream==null){
                result = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line=reader.readLine())!=null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0){
                result = null;
            }

            result = buffer.toString();

        } catch (MalformedURLException e){
            Log.e(TAG, "Url is bad", e);

        }catch (IOException e){
            Log.e(TAG, "IO Exception", e);

        }catch (Exception e){
            Log.e(TAG, "Something went wrong", e);
        }
        finally {
            closeConnections(urlConnection,reader);
        }
        return result;
    }

    protected void closeConnections(HttpURLConnection urlConnection, BufferedReader reader){
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e(TAG, "Error closing stream", e);
            }
        }
    }
    @Override
    protected void onPostExecute(ArrayList<String> trailer) {
        super.onPostExecute(trailer);
        event.onReviewsLoaded(trailer);
    }

    public interface Event {
        void onReviewsLoaded(ArrayList<String> trailer);
    }
}

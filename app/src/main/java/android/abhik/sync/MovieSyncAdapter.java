package android.abhik.sync;

import android.abhik.data.MovieContract;
import android.abhik.popularmovies.Movie;
import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.Vector;

/**
 * Created by abmitra on 6/28/2015.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String TAG = MovieSyncAdapter.class.getSimpleName();
    ContentResolver contentResolver ;
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
       int nextPage =  extras.getInt("nextPage");
        String sort = extras.getString("sortBy");
        doInBackground(nextPage + "",sort,"a670cb5c49630b38e1ca06f0cd82b8eb","api.themoviedb.org");

    }
    protected ArrayList<Movie> doInBackground(String pageNumber,String sort,String api_key,String Url) {
        Uri.Builder builder = new Uri.Builder();
        builder
                .scheme("http")
                .authority(Url)
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", sort)
                .appendQueryParameter("page", pageNumber.toString())
                .appendQueryParameter("api_key", api_key);
        String jsonStr = getDataFromServer(builder.build().toString());
        return getPopularMovies(jsonStr);
    }

    protected ArrayList<Movie> getPopularMovies(String jsonStr){
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int page = jsonObj.getInt("page");
            JSONArray list = jsonObj.getJSONArray("results");
            Vector<ContentValues> cVVector = new Vector<ContentValues>(list.length());
            for(int i =0;i< list.length();i++){
                JSONObject item = list.getJSONObject(i);
                ContentValues movieDetails = new ContentValues();
                movieDetails.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,item.getLong("id"));
                movieDetails.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, item.getString("overview"));
                movieDetails.put(MovieContract.MovieEntry.COLUMN_POPULARITY, item.getDouble("popularity"));
                movieDetails.put(MovieContract.MovieEntry.COLUMN_POSTER,item.getString("poster_path"));
                movieDetails.put(MovieContract.MovieEntry.COLUMN_RATING, item.getDouble("vote_average"));
                movieDetails.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,item.getString("release_date"));
                movieDetails.put(MovieContract.MovieEntry.COLUMN_TITLE, item.getString("original_title"));
                cVVector.add(movieDetails);


            }
            int inserted = 0;
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);


            }
            Log.d(TAG, "Fetching Movies Complete. " + inserted + " Inserted");

        } catch (JSONException e ){
            Log.e(TAG, "Internet is probably off", e);
        }
        catch (NullPointerException e){
            Log.e(TAG, "Internet is probably off", e);
        }
        catch (Exception e){
            Log.e(TAG, "Internet is probably off", e);
        }
        return movies;


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
    public MovieSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
       contentResolver = context.getContentResolver();

    }


}

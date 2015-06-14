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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by abmitra on 6/14/2015.
 */
public class FetchMovieAsyncTask extends AsyncTask<Integer,Integer,  ArrayList<Movie>> {
    private String TAG = "PopularMovies - FetchMoviesAsyncTask";
    private PopularMovies popularMovies;

    public FetchMovieAsyncTask(PopularMovies popularMovies){
        super();
        this.popularMovies = popularMovies;

    }

    @Override
    protected ArrayList<Movie> doInBackground(Integer... params) {
        Integer pageNumber = params[0];
        Uri.Builder builder = new Uri.Builder();
        builder
                .scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", "popularity.desc")
                .appendQueryParameter("page", pageNumber.toString())
                .appendQueryParameter("api_key", "a670cb5c49630b38e1ca06f0cd82b8eb");
        String jsonStr = getDataFromServer(builder.build().toString());
        return getPopularMovies(jsonStr);
    }

    protected ArrayList<Movie> getPopularMovies(String jsonStr){
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int page = jsonObj.getInt("page");
            JSONArray list = jsonObj.getJSONArray("results");
            for(int i =0;i< list.length();i++){
                JSONObject item = list.getJSONObject(i);
                String date = item.getString("release_date");
                SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                Date release_date = new Date();
                Movie movie;
                try {
                    release_date = format.parse(date);

                } catch (Exception e){
                    release_date = new Date();
                } finally {
                    movie = new Movie(
                            item.getString("poster_path"),
                            item.getString("original_title"),
                            item.getString("overview"),
                            item.getString("vote_average"),
                            release_date,
                            item.getLong("id"));
                    movies.add(movie);
                }

            }
        } catch (JSONException e ){

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
    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        popularMovies.populateMovies(movies);
        super.onPostExecute(movies);
    }
}

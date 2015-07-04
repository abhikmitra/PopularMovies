package android.abhik.popularmovies;

import android.abhik.data.MovieContract;
import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by abmitra on 6/14/2015.
 */
public class PopularMovies {
    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public int getNextPage() {
        return nextPage;
    }

    public Boolean isShouldFetchMore() {
        return shouldFetchMore;
    }

    private ArrayList<Movie> movies ;
    private int nextPage;
    private Boolean shouldFetchMore;
    private ImageAdapter imageAdapter;
    private Context context;
    private Account mAccount;
    public PopularMovies(int nextPage, Boolean shouldFetchMore, ImageAdapter imageAdapter, Context context, ArrayList<Movie> movies,Account mAccount){
        this.nextPage = nextPage;
        this.movies =movies;
        this.shouldFetchMore = shouldFetchMore;
        this.imageAdapter = imageAdapter;
        this.context = context;
        this.mAccount = mAccount;
    }
    public void populateMovies(ArrayList<Movie> movies){

        if(movies.size()<20 && movies.size()!=0){
            shouldFetchMore = false;
        }
        this.movies.addAll(movies);
        nextPage++;
        if(movies.size()==0){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "Could not fetch data", duration);
            toast.show();
        }


    }
     public void resetPage(){
        nextPage = 1;
        //movies.clear();
    }

    public void fetchMoviesFromServer(String sortBy){

            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            settingsBundle.putInt("nextPage",nextPage);
            settingsBundle.putString("sortBy",sortBy);
            ContentResolver.requestSync(mAccount, MovieContract.CONTENT_AUTHORITY, settingsBundle);


    }
    public Movie getMovieByPosition(int position){
        return movies.get(position);
    }

}

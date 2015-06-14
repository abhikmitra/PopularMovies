package android.abhik.popularmovies;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by abmitra on 6/14/2015.
 */
public class PopularMovies {
    private ArrayList<Movie> movies ;
    private int nextPage;
    private Boolean shouldFetchMore;
    private ImageAdapter imageAdapter;
    private boolean isLoading ;
    public PopularMovies(int nextPage, ImageAdapter imageAdapter){
        this.nextPage = nextPage;
        movies = new ArrayList<>();
        shouldFetchMore = true;
        this.imageAdapter = imageAdapter;
        isLoading = false;
    }
    public void populateMovies(ArrayList<Movie> movies){

        if(movies.size()<20){
            shouldFetchMore = false;
        }
        this.movies.addAll(movies);
        Log.e("popular movies","Size"+this.movies.size());
        nextPage++;
        isLoading = false;
        imageAdapter.setData(this.movies);


    }
    public void fetchMoviesFromServer(){
        if(!isLoading){
            new FetchMovieAsyncTask(this).execute(nextPage);
            isLoading = true;
        }

    }
}

package android.abhik.popularmovies;

import android.content.Context;

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
    private Context context;
    public PopularMovies(int nextPage, ImageAdapter imageAdapter, Context context){
        this.nextPage = nextPage;
        movies = new ArrayList<>();
        shouldFetchMore = true;
        this.imageAdapter = imageAdapter;
        isLoading = false;
        this.context = context;
    }
    public void populateMovies(ArrayList<Movie> movies){

        if(movies.size()<20){
            shouldFetchMore = false;
        }
        this.movies.addAll(movies);
        nextPage++;
        isLoading = false;
        imageAdapter.setData(this.movies);


    }

    public void resetPage(){
        nextPage = 1;
        movies.clear();
    }
    public void fetchMoviesFromServer(String sortBy){
        if(!isLoading){
            new FetchMovieAsyncTask(this).execute(""+nextPage, sortBy);
            isLoading = true;
        }

    }
    public Movie getMovieByPosition(int position){
        return movies.get(position);
    }
}

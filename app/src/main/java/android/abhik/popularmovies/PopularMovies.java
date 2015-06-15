package android.abhik.popularmovies;

import android.content.Context;
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
    private boolean isLoading ;
    private Context context;
    public PopularMovies(int nextPage, Boolean shouldFetchMore, ImageAdapter imageAdapter, Context context, ArrayList<Movie> movies){
        this.nextPage = nextPage;
        this.movies =movies;
        this.shouldFetchMore = shouldFetchMore;
        this.imageAdapter = imageAdapter;
        isLoading = false;
        this.context = context;
    }
    public void populateMovies(ArrayList<Movie> movies){

        if(movies.size()<20 && movies.size()!=0){
            shouldFetchMore = false;
        }
        this.movies.addAll(movies);
        nextPage++;
        isLoading = false;
        imageAdapter.setData(this.movies);
        if(movies.size()==0){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "Could not fetch data", duration);
            toast.show();
        }


    }
    public void reRenderGrid(){
        imageAdapter.setData(movies);
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

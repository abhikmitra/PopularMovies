package android.abhik.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String TAG = "PopularMovies - MainActivityFragment";
    PopularMovies popularMovies;
    ImageAdapter imageAdapter;
    boolean enableInfiniteScroll = true;
    String sortByChoice;
    public MainActivityFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String sorting = prefs.getString(getActivity().getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_popularity));
        //Reload only if there is a change in the sorting order
        Log.i(TAG, "Activity Started");
        if(sorting != sortByChoice){
            sortByChoice = sorting;
            popularMovies.resetPage();
            Log.i(TAG, "Resetting Page");
            popularMovies.fetchMoviesFromServer(sortByChoice);
        } else {
            popularMovies.reRenderGrid();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", imageAdapter.getMovieArray());
        outState.putInt("nextPage", popularMovies.getNextPage());
        outState.putBoolean("shouldFetchMore", popularMovies.isShouldFetchMore());
        outState.putString("sortByChoice", sortByChoice);
        imageAdapter.getMovieArray();
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState!=null && imageAdapter!=null){
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
            int nextPage = savedInstanceState.getInt("nextPage");
            Boolean shouldFetchMore = savedInstanceState.getBoolean("shouldFetchMore");
            String sortByChoice = savedInstanceState.getString("sortByChoice");

        } else {

        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Movie> movies = new ArrayList<Movie>() ;
        int nextPage = 1;
        Boolean shouldFetchMore = true;
        if(savedInstanceState!=null){
            movies = savedInstanceState.getParcelableArrayList("movies");
            nextPage = savedInstanceState.getInt("nextPage");
            shouldFetchMore = savedInstanceState.getBoolean("shouldFetchMore");
            sortByChoice = savedInstanceState.getString("sortByChoice");
        }
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        imageAdapter = new ImageAdapter(getActivity().getApplicationContext());
        popularMovies = new PopularMovies(nextPage, shouldFetchMore,
                imageAdapter,getActivity().getApplicationContext(),movies);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        if(gridview!=null){
            gridview.setAdapter(imageAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(),DetailActivity.class);
                    intent.putExtra(Movie.class.getName(),popularMovies.getMovieByPosition(position));
                    startActivity(intent);
                }
            });
            gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {


                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if ((totalItemCount - firstVisibleItem - visibleItemCount) <= 6
                            && enableInfiniteScroll
                            && sortByChoice!=null
                            && totalItemCount!=0) {
                        popularMovies.fetchMoviesFromServer(sortByChoice);


                    }
                }
            });

        } else {
           Log.e(TAG, "Grid View was not found");
        }

        return v;
    }
}

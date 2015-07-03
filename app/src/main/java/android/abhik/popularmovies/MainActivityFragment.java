package android.abhik.popularmovies;

import android.abhik.constants.Constants;
import android.abhik.data.MovieContract;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = "PopularMovies - MainActivityFragment";
    PopularMovies popularMovies;
    ImageAdapter imageAdapter;
    boolean enableInfiniteScroll = true;
    String sortByChoice;
    Account mAccount;
    private static final int MOVIE_POP_LOADER = 0;
    private static final int MOVIE_RATING_LOADER = 1;
    private static final int FAV_LOADER = 2;

    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = CreateSyncAccount(this.getActivity());

    }



    public static Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(
                Constants.ACCOUNT, Constants.ACCOUNT_TYPE);
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        Context.ACCOUNT_SERVICE);
       if (accountManager.addAccountExplicitly(newAccount, null, null)) {

        } else {

        }
        return newAccount;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder;
        switch (id){
            case FAV_LOADER:
                return new CursorLoader(getActivity(), MovieContract.FavoriteEntry.buildUriForMovies(), Constants.MOVIE_COLUMNS, null, null, null);

            case MOVIE_POP_LOADER:
                sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
                return new CursorLoader(getActivity(), MovieContract.MovieEntry.buildUriForMovies(), Constants.MOVIE_COLUMNS, null, null, sortOrder);
            case MOVIE_RATING_LOADER:
                sortOrder = MovieContract.MovieEntry.COLUMN_RATING + " DESC";
                return new CursorLoader(getActivity(), MovieContract.MovieEntry.buildUriForMovies(), Constants.MOVIE_COLUMNS, null, null, sortOrder);
            default: throw new UnsupportedOperationException("No such loader");
        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageAdapter.swapCursor(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String sorting = prefs.getString(getActivity().getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_popularity));
        //Reload only if there is a change in the sorting order
        Log.i(TAG, "Activity Started");

        if (sorting != sortByChoice) {
            sortByChoice = sorting;
            popularMovies.resetPage();
            Log.i(TAG, "Resetting Page");
            destroyAllLoaders();
            createLoaders(sorting);
        } else{
            restartLoaders(sortByChoice);
        }


    }
    private void restartLoaders(String sorting){
        String pref_favorites =  getString(R.string.pref_favorites);
        String pref_sort_popularity =  getString(R.string.pref_sort_popularity);
        String pref_sort_vote =  getString(R.string.pref_sort_vote);
        if(sorting.equals(pref_favorites)){
            getLoaderManager().restartLoader(FAV_LOADER, null, this);
            return;
        }
        if(sorting.equals(pref_sort_popularity)){
            getLoaderManager().restartLoader(MOVIE_POP_LOADER, null, this);
            return;
        }
        if(sorting.equals(pref_sort_vote)){
            getLoaderManager().restartLoader(MOVIE_RATING_LOADER, null, this);
            return;
        }
    }
    private void destroyAllLoaders(){
        getLoaderManager().destroyLoader(MOVIE_POP_LOADER);
        getLoaderManager().destroyLoader(MOVIE_RATING_LOADER);
        getLoaderManager().destroyLoader(FAV_LOADER);
    }
    private void createLoaders(String sorting){
        String pref_favorites =  getString(R.string.pref_favorites);
        String pref_sort_popularity =  getString(R.string.pref_sort_popularity);
        String pref_sort_vote =  getString(R.string.pref_sort_vote);
        if(sorting.equals(pref_favorites)){
            getLoaderManager().initLoader(FAV_LOADER, null, this);
            return;
        }
        if(sorting.equals(pref_sort_popularity)){
            getActivity().getContentResolver().delete(MovieContract.MovieEntry.buildUriForMovies(),null,null);
            popularMovies.fetchMoviesFromServer(sortByChoice);
            getLoaderManager().initLoader(MOVIE_POP_LOADER, null, this);
            return;
        }
        if(sorting.equals(pref_sort_vote)){
            getActivity().getContentResolver().delete(MovieContract.MovieEntry.buildUriForMovies(),null,null);
            popularMovies.fetchMoviesFromServer(sortByChoice);
            getLoaderManager().initLoader(MOVIE_RATING_LOADER, null, this);
            return;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("loaded", true);
//        outState.putParcelableArrayList("movies", imageAdapter.getMovieArray());
//        outState.putInt("nextPage", popularMovies.getNextPage());
//        outState.putBoolean("shouldFetchMore", popularMovies.isShouldFetchMore());
        outState.putString("sortByChoice", sortByChoice);
//        imageAdapter.getMovieArray();
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        if(savedInstanceState!=null && imageAdapter!=null){
//            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
//            int nextPage = savedInstanceState.getInt("nextPage");
//            Boolean shouldFetchMore = savedInstanceState.getBoolean("shouldFetchMore");
//            String sortByChoice = savedInstanceState.getString("sortByChoice");
//
//        } else {
//
//        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        int nextPage = 1;
        Boolean shouldFetchMore = true;
        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movies");
            nextPage = savedInstanceState.getInt("nextPage");
            shouldFetchMore = savedInstanceState.getBoolean("shouldFetchMore");
            sortByChoice = savedInstanceState.getString("sortByChoice");
        }else {

        }
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        imageAdapter = new ImageAdapter(getActivity().getApplicationContext(), null, 0);
        popularMovies = new PopularMovies(nextPage, shouldFetchMore,
                imageAdapter, getActivity().getApplicationContext(), movies, mAccount);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        if (gridview != null) {
            gridview.setAdapter(imageAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    ((Callback)getActivity()).onItemSelected(new Movie(cursor));
                }
            });
//            gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//
//                }
//
//                @Override
//                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                    if ((totalItemCount - firstVisibleItem - visibleItemCount) <= 6
//                            && enableInfiniteScroll
//                            && sortByChoice != null
//                            && totalItemCount != 0) {
//                        popularMovies.fetchMoviesFromServer(sortByChoice);
//
//
//                    }
//                }
//            });

        } else {
            Log.e(TAG, "Grid View was not found");
        }

        return v;
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Movie movie);

    }
}

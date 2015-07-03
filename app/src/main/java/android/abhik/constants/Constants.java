package android.abhik.constants;

import android.abhik.data.MovieContract;

/**
 * Created by abmitra on 6/28/2015.
 */
public class Constants {
    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_RATING
    };
    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_TMDB_ID= 2;
    public static final int COL_MOVIE_POPULARITY = 3;
    public static final int COL_MOVIE_POSTER = 4;
    public static final int COL_MOVIE_OVERVIEW= 5;
    public static final int COL_MOVIE_RELEASE_DATE= 6;
    public static final int COL_MOVIE_RATING= 7;
    public static final String ACCOUNT_TYPE = "abhik.popularmovies.com";
    public static final String ACCOUNT = "dummyaccount";
}

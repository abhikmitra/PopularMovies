package android.abhik.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by abmitra on 6/27/2015.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "android.abhik.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVORITES = "favorites";
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE= "release_date";
        public static final String COLUMN_RATING= "rating";
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static Uri buildUriForMovies() {
            return CONTENT_URI
                    .buildUpon()
                    .build();
        }
        public static Uri buildUriForMovie(long id) {
            return ContentUris
                    .withAppendedId(CONTENT_URI, id)
                  ;
        }
        public static long getIdFromURI(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }
    public static final class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE= "release_date";
        public static final String COLUMN_RATING= "rating";
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        public static Uri buildUriForMovies() {
            return CONTENT_URI
                    .buildUpon()
                    .build();
        }
        public static Uri buildUriForMovie(long id) {
            return ContentUris
                    .withAppendedId(CONTENT_URI, id)
                    ;
        }
        public static long getIdFromURI(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

}

package android.abhik.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by abmitra on 6/28/2015.
 */
public class MovieProvider extends ContentProvider {
    SQLiteOpenHelper mOpenHelper;
    private static SQLiteQueryBuilder queryBuilder;
    private static SQLiteQueryBuilder favQueryBuilder;
    static {
        queryBuilder = new SQLiteQueryBuilder();
        favQueryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
        favQueryBuilder.setTables(MovieContract.FavoriteEntry.TABLE_NAME);
    }

    static final int MOVIES = 100;
    static final int MOVIE_BY_ID = 101;
    static final int FAV_MOVIE_BY_ID = 102;
    static final int FAV_MOVIES = 103;
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES, FAV_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", MOVIE_BY_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/#", FAV_MOVIE_BY_ID);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_BY_ID:
            String idSelection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID + " = ? ";
                long _ID = MovieContract.MovieEntry.getIdFromURI(uri);
                retCursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        idSelection,
                        new String[]{Long.toString(_ID)},
                        null,
                        null,
                        sortOrder);
                retCursor.setNotificationUri(getContext().getContentResolver(),uri);

                break;
            case MOVIES:
                try{
                    retCursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                            projection,
                            null,
                            null,
                            null,
                            null,
                            sortOrder);
                }
                catch (Exception e){
                    Log.v("Provider",e.toString());
                }

                retCursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            case FAV_MOVIES:
                try {
                    retCursor = favQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                }
                catch (Exception e){
                    Log.v("Provider",e.toString());
                }
                retCursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            case FAV_MOVIE_BY_ID:
                String favIdSelection = MovieContract.FavoriteEntry.TABLE_NAME + "." + MovieContract.FavoriteEntry._ID + " = ? ";
                long fav_ID = MovieContract.MovieEntry.getIdFromURI(uri);
                retCursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        favIdSelection,
                        new String[]{Long.toString(fav_ID)},
                        null,
                        null,
                        sortOrder);
                retCursor.setNotificationUri(getContext().getContentResolver(),uri);

                break;
        }

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_BY_ID:return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case FAV_MOVIE_BY_ID:return MovieContract.FavoriteEntry.CONTENT_TYPE;
            case FAV_MOVIES:return MovieContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);
        if(match == MOVIES){
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            Uri returnUri;
            long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
            if(_id > 0){
                returnUri = MovieContract.MovieEntry.buildUriForMovie(_id);
                return returnUri;
            }

        } else  if(match == FAV_MOVIES){
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            Uri returnUri;
            long _id = db.insert(MovieContract.FavoriteEntry.TABLE_NAME,null,values);
            if(_id > 0){
                returnUri = MovieContract.FavoriteEntry.buildUriForMovie(_id);
                return returnUri;
            }
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        if(match == MOVIES) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            int _id = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
            return _id;
        }
         else  if(match == FAV_MOVIES){
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            int _id = db.delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
            return _id;
        }
        return -1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int match = sUriMatcher.match(uri);
        int returnCount = 0;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            for(ContentValues cv:values){
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}

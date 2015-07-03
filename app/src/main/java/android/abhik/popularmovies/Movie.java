package android.abhik.popularmovies;

import android.abhik.constants.Constants;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by abmitra on 6/14/2015.
 */
public class Movie implements Parcelable {
    public String getImageUrl() {
        return imageUrl;
    }

    private String imageUrl;

    public String getOriginal_title() {
        return original_title;
    }

    private String original_title;

    public String getSynopsis() {
        return synopsis;
    }

    private String synopsis;

    public Double getVote_average() {
        return vote_average;
    }

    private Double vote_average;

    public Date getRelease_date() {
        return release_date;
    }

    private Date release_date;

    public long getId() {
        return id;
    }

    public Double getPopularity() {
        return popularity;
    }

    private Double popularity;
    private final String PREFIX_URL = "http://image.tmdb.org/t/p/w185";

    long id;
    long _ID;
    static Date getDateFromString(String date){
       try{
           return new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(date);
        } catch (Exception e){
            return new Date();
        }
    }
    public Movie(Cursor c){
        this(
                c.getString(Constants.COL_MOVIE_POSTER),
                c.getString(Constants.COL_MOVIE_TITLE),
                c.getString(Constants.COL_MOVIE_OVERVIEW),
                c.getDouble(Constants.COL_MOVIE_RATING),
                getDateFromString(c.getString(Constants.COL_MOVIE_RELEASE_DATE)),
                c.getLong(Constants.COL_MOVIE_TMDB_ID),
                c.getDouble(Constants.COL_MOVIE_POPULARITY)

        );
        this._ID = c.getLong(Constants.COL_MOVIE_ID);
    }
    public Movie(String imageUrl, String original_title, String synopsis,Double vote_average, Date release_date, long id, Double popularity){
        if(imageUrl.indexOf("http")==-1){
            this.imageUrl = PREFIX_URL+imageUrl;
        } else {
            this.imageUrl = imageUrl;
        }

        this.original_title = original_title;
        this.synopsis = synopsis;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.id = id;
        this.popularity = popularity;
    }
    public Movie(Parcel in){
        imageUrl = in.readString();
        synopsis=in.readString();
        original_title = in.readString();
        vote_average = in.readDouble();
        try{
            release_date = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(in.readString());
        } catch (Exception e){
            release_date = new Date();
        }

        id = in.readLong();
        popularity = in.readDouble();
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(synopsis);
        dest.writeString(original_title);
        dest.writeDouble(vote_average);
        dest.writeString(new SimpleDateFormat("dd MMM yyyy").format(release_date));
        dest.writeLong(id);
        dest.writeDouble(popularity);
    }
}

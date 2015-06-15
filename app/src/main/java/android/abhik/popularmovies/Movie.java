package android.abhik.popularmovies;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by abmitra on 6/14/2015.
 */
public class Movie implements Serializable {
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
    public Movie(String imageUrl, String original_title, String synopsis,Double vote_average, Date release_date, long id, Double popularity){
        this.imageUrl = PREFIX_URL+imageUrl;
        this.original_title = original_title;
        this.synopsis = synopsis;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.id = id;
        this.popularity = popularity;
    }
}

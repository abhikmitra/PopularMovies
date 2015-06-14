package android.abhik.popularmovies;

import java.util.Date;

/**
 * Created by abmitra on 6/14/2015.
 */
public class Movie {
    public String getImageUrl() {
        return imageUrl;
    }

    private String imageUrl;

    public String getOriginal_title() {
        return original_title;
    }

    private String original_title;
    private String synopsis;
    private String vote_average;
    private Date release_date;

    public long getId() {
        return id;
    }


    long id;
    public Movie(String imageUrl, String original_title, String synopsis,String vote_average, Date release_date, long id){
        this.imageUrl = imageUrl;
        this.original_title = original_title;
        this.synopsis = synopsis;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.id = id;
    }
}

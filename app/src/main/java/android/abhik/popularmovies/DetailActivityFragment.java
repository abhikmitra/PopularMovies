package android.abhik.popularmovies;

import android.abhik.data.MovieContract;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements FetchTrailerAsyncTask.Event,FetchReviewsAsync.Event {


    public DetailActivityFragment() {
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        Movie movie = (Movie)getActivity().getIntent().getParcelableExtra(Movie.class.getName());
        if(movie==null){
            Bundle arguments = getArguments();
            if (arguments != null) {
                movie = arguments.getParcelable(Movie.class.getName());
            }
        }
        if(movie!=null){
            ((TextView)v.findViewById(R.id.movie_title)).setText(movie.getOriginal_title());
            ((TextView)v.findViewById(R.id.movie_synopsys)).setText(movie.getSynopsis());

            ((TextView)v.findViewById(R.id.movie_release_date)).setText(new SimpleDateFormat("dd MMM yyyy").format(movie.getRelease_date()));
            NumberFormat formatter = new DecimalFormat("#00.00");
            ((TextView)v.findViewById(R.id.movie_rating)).setText(getString(R.string.title_rating) + " " + (formatter.format(movie.getVote_average())).toString());
            Picasso
                    .with(getActivity().getApplicationContext())
                    .load(movie.getImageUrl())
                    .into((SquareImageView) v.findViewById(R.id.movie_image));
            String sortOrder = MovieContract.FavoriteEntry.COLUMN_POPULARITY + " DESC";
            Cursor c = getActivity().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,null, "movie_id =?",new String[]{movie.getId()+""},sortOrder);
            ImageButton ib = (ImageButton)v.findViewById(R.id.favorite);
            if(c.getCount() >0){
                int drbl = R.drawable.abc_btn_rating_star_on_mtrl_alpha;
                ib.setTag(R.string.isFavorite, true);
                ib.setImageDrawable(getActivity().getDrawable(drbl));
            }
            final Movie m = movie;
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton ib = (ImageButton) v;

                    if (v.getTag(R.string.isFavorite) == null) {
                        v.setTag(R.string.isFavorite, false);
                    }
                    int drbl;
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
                    String strDate = sdfDate.format(m.getRelease_date());
                    ContentValues movieDetails = new ContentValues();
                    movieDetails.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, m.getId() + "");
                    movieDetails.put(MovieContract.FavoriteEntry.COLUMN_OVERVIEW, m.getSynopsis());
                    movieDetails.put(MovieContract.FavoriteEntry.COLUMN_POPULARITY, m.getPopularity());
                    movieDetails.put(MovieContract.FavoriteEntry.COLUMN_POSTER, m.getImageUrl());
                    movieDetails.put(MovieContract.FavoriteEntry.COLUMN_RATING, m.getVote_average());
                    movieDetails.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE, strDate);
                    movieDetails.put(MovieContract.FavoriteEntry.COLUMN_TITLE, m.getOriginal_title());
                    Boolean status = (Boolean) v.getTag(R.string.isFavorite);
                    if (status) {
                        drbl = R.drawable.abc_btn_rating_star_off_mtrl_alpha;
                        v.setTag(R.string.isFavorite, false);
                        getActivity().getContentResolver().delete(MovieContract.FavoriteEntry.CONTENT_URI, "movie_id =?", new String[]{m.getId() + ""});
                    } else {
                        drbl = R.drawable.abc_btn_rating_star_on_mtrl_alpha;
                        v.setTag(R.string.isFavorite, true);
                        getActivity().getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, movieDetails);
                    }
                    ib.setImageDrawable(getActivity().getDrawable(drbl));
                }
            });
            new FetchTrailerAsyncTask(this).execute(movie.getId());
            new FetchReviewsAsync(this).execute(movie.getId());
        }
        view =v;
        return v;
    }

    @Override
    public void onTrailerLoaded(ArrayList<String> trailer) {

       LinearLayout ll = (LinearLayout) view.findViewById(R.id.trailer);
        int count = 1;
        for(String t:trailer){
            Button btn = new Button(getActivity());
            btn.setText("Trailer " + count);
            btn.setTag(R.string.trailerUrl, t);
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            count++;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String t = (String)  v.getTag(R.string.trailerUrl);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + t)));
                }
            });
            ll.addView(btn);
        }

    }
    @Override
    public void onReviewsLoaded(ArrayList<String> reviews) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.trailer);
        for(String r:reviews){
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setPadding(0, 10, 0, 10);
            tv.setText(r);
            ll.addView(tv);
        }

    }
}

package android.abhik.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        Movie movie = (Movie)getActivity().getIntent().getParcelableExtra(Movie.class.getName());
        if(movie!=null){
            ((TextView)v.findViewById(R.id.movie_title)).setText(movie.getOriginal_title());
            ((TextView)v.findViewById(R.id.movie_synopsys)).setText(movie.getSynopsis());

            ((TextView)v.findViewById(R.id.movie_release_date)).setText(new SimpleDateFormat("dd MMM yyyy").format(movie.getRelease_date()));
            NumberFormat formatter = new DecimalFormat("#00.00");
            ((TextView)v.findViewById(R.id.movie_rating)).setText(getString(R.string.title_rating) + " " + (formatter.format(movie.getVote_average())).toString());
            Picasso
                    .with(getActivity().getApplicationContext())
                    .load(movie.getImageUrl())
                    .into((SquareImageView)v.findViewById(R.id.movie_image));
        }
        return v;
    }
}

package android.abhik.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abmitra on 6/14/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater inflater;
    ArrayList<Movie> movies = new ArrayList<Movie>();
    public ImageAdapter(Context c) {
        mContext = c;
        inflater = LayoutInflater.from(c);
    }
    public void setData(ArrayList<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }
    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return movies.get(position).getId();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView pictureView;
        TextView titleView;
        View v;
        if (convertView == null) {
            v = inflater.inflate(R.layout.grid_item,parent,false);
            v.setTag(R.id.picture,v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        } else{
           v=convertView;
        }
        pictureView = (ImageView) v.getTag(R.id.picture);
        titleView = (TextView) v.getTag(R.id.text);
        Movie movie = movies.get(position);
        titleView.setText(movie.getOriginal_title());
        String url ="http://image.tmdb.org/t/p/w185"+movie.getImageUrl();
        Picasso
                .with(mContext)
                .load(url)
                .into(pictureView);
        return v;
    }


}
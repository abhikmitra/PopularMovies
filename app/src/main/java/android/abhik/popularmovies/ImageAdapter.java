package android.abhik.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by abmitra on 6/14/2015.
 */
public class ImageAdapter extends CursorAdapter {
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
       View view = LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false);
        view.setTag(R.id.picture, view.findViewById(R.id.picture));
        view.setTag(R.id.text, view.findViewById(R.id.text));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView pictureView = (ImageView) view.getTag(R.id.picture);
        TextView titleView = (TextView) view.getTag(R.id.text);
        Movie movie = new Movie(cursor);
        titleView.setText(movie.getOriginal_title());
        Picasso
                .with(context)
                .load(movie.getImageUrl())
                .into(pictureView);

    }

   public ImageAdapter(Context context,  Cursor c, int flags) {
        super(context, c, flags);

    }



}
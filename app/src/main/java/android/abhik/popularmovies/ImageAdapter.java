package android.abhik.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by abmitra on 6/14/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater inflater;
    public ImageAdapter(Context c) {
        mContext = c;
        inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return 15;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
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
        titleView.setText("Thsi si a test");
        Picasso
                .with(mContext)
                .load("http://i.imgur.com/DvpvklR.png")
                .into(pictureView);
        return v;
    }


}
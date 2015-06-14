package android.abhik.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String TAG = "PopularMovies - MainActivityFragment";
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        if(gridview!=null){
            gridview.setAdapter(new ImageAdapter(getActivity().getApplicationContext()));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                }
            });
        } else {
            Log.e(TAG,"Grid View was not found");
        }

        return v;
    }
}

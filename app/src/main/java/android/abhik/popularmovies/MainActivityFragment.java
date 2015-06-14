package android.abhik.popularmovies;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String TAG = "PopularMovies - MainActivityFragment";
    PopularMovies popularMovies;
    ImageAdapter imageAdapter;
    private int count =0;
    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        imageAdapter = new ImageAdapter(getActivity().getApplicationContext());
        popularMovies = new PopularMovies(1,imageAdapter);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        if(gridview!=null){
            gridview.setAdapter(imageAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                }
            });
            gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {


                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if((totalItemCount - firstVisibleItem-visibleItemCount) <=6){
                        popularMovies.fetchMoviesFromServer();
                        count++;

                    }
                }
            });
            popularMovies.fetchMoviesFromServer();
        } else {
           Log.e(TAG, "Grid View was not found");
        }

        return v;
    }
}

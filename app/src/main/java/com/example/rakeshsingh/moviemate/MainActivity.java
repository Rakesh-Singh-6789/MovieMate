package com.example.rakeshsingh.moviemate;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.rakeshsingh.moviemate.Adapters.PosterAdapter;
import com.example.rakeshsingh.moviemate.Database.FavoriteMovie;
import com.example.rakeshsingh.moviemate.Database.MainViewModel;
import com.example.rakeshsingh.moviemate.ModelClasses.MovieResult;
import com.example.rakeshsingh.moviemate.ModelClasses.Result;
import com.example.rakeshsingh.moviemate.retrofit.RetroInterface;
import com.example.rakeshsingh.moviemate.retrofit.RetrofitClient;
import com.example.rakeshsingh.moviemate.utils.AppConstants;
import com.example.rakeshsingh.moviemate.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.progressTextView)
    TextView progressTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.RecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolBar)
    Toolbar toolBar;




    private List<Result> movieList;
    private int spanCount;
    private String alreadySortedBy=AppConstants.POPULAR;
    private SharedPreferences.Editor editor;
    SharedPreferences sortPreferences;
    private PosterAdapter posterAdapter;


    //Favorite movies
    private List<FavoriteMovie> favMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //to set the moving text for Breathe in Breathe out
        progressTextView.setSelected(true);

        //fetching favorites into list
        favMovies =new ArrayList<FavoriteMovie>();

        sortPreferences=getSharedPreferences("SP_Name",MODE_PRIVATE);
        editor=sortPreferences.edit();

        movieList = new ArrayList<>();

        spanCount=2;

        if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
            spanCount=4;
        }
        setupRecyclerView(spanCount);


        //editor.putString(getResources().getString(R.string.already_sorted_by),AppConstants.TOP_RATED);

        //editor.apply();

        loadMovieData(sortPreferences.getString(getResources().getString(R.string.already_sorted_by),AppConstants.POPULAR));

        //setupViewModel();

    }

    private void setupRecyclerView(int spanCount) {

        PosterAdapter posterAdapter=new PosterAdapter(this,movieList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,spanCount));
        recyclerView.setAdapter(posterAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, dpToPx(4), true));
        posterAdapter.notifyDataSetChanged();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void ClearMovieItemList() {
        if (movieList != null) {
            movieList.clear();
        } else {
            movieList = new ArrayList<Result>();
        }
    }

    private void loadMovieData(String filter) {


        if (filter.equals("favorites")) {
            ClearMovieItemList();
            //Toast.makeText(this, "we are in favorites", Toast.LENGTH_LONG).show();
            recyclerView.setVisibility(View.INVISIBLE);
            for (int i = 0; i < favMovies.size(); i++) {

                Result mov = new Result(favMovies.get(i).getId(),
                        favMovies.get(i).getTitle(),
                        favMovies.get(i).getReleaseDate(),
                        favMovies.get(i).getVote(),
                        favMovies.get(i).getPopularity(),
                        favMovies.get(i).getSynopsis(),
                        favMovies.get(i).getImage(),
                        favMovies.get(i).getBackdrop()
                );
                movieList.add(mov);
            }

            if (movieList.isEmpty()){
                //Toast.makeText(this, "Your Favorite List Is Empty ", Toast.LENGTH_LONG).show();
            }else {

                progressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new PosterAdapter(MainActivity.this, movieList));

                 }

        } else {

            //Toast.makeText(this, "we are in else part", Toast.LENGTH_LONG).show();
            recyclerView.setVisibility(View.INVISIBLE);

            RetroInterface retroInterface = RetrofitClient.getRetrofit().create(RetroInterface.class);

            try {
                Call<MovieResult> movieResultCall = retroInterface.getMovieResultCall(filter, AppConstants.API_KEY);

                movieResultCall.enqueue(new Callback<MovieResult>() {
                    @Override
                    public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                        progressBar.setVisibility(View.GONE);
                        progressTextView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        movieList = response.body().getResults();
                        recyclerView.setAdapter(new PosterAdapter(MainActivity.this, movieList));
                    }

                    @Override
                    public void onFailure(Call<MovieResult> call, Throwable t) {
                        progressTextView.setVisibility(View.VISIBLE);
                        progressTextView.setText("Internet Connection Not Found :(");
                        Toast.makeText(MainActivity.this, "Please Check Your Internet Connection :(", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.sort_menu,menu);
        alreadySortedBy=sortPreferences.getString(getString(R.string.already_sorted_by),AppConstants.POPULAR);

        Log.i("Already____SORTED",alreadySortedBy);
        Log.i("In___pref",sortPreferences.getString(getString(R.string.already_sorted_by),AppConstants.POPULAR));

        if (alreadySortedBy.equals(AppConstants.POPULAR)){
            menu.getItem(0).getSubMenu().getItem(0).setChecked(true);
        }else{
            menu.getItem(0).getSubMenu().getItem(1).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {




        if (item.isChecked()==true){
            Toast.makeText(this, "Movies are already sorted by the selected preference", Toast.LENGTH_LONG).show();

            return super.onOptionsItemSelected(item);
        }else {

            switch (item.getItemId()) {
                case R.id.sort_by_popular:
                    item.setChecked(true);

                    alreadySortedBy = AppConstants.POPULAR;


                    editor.putString(getString(R.string.already_sorted_by), alreadySortedBy);
                    editor.apply();

                    onContentChanged(alreadySortedBy);
                    return true;
                case R.id.sort_by_topRated:
                    item.setChecked(true);

                    alreadySortedBy = AppConstants.TOP_RATED;
                    editor.putString(getString(R.string.already_sorted_by), alreadySortedBy);
                    editor.apply();

                    onContentChanged(alreadySortedBy);
                    return true;
                case R.id.sort_by_favorites:
                    item.setChecked(true);
                    alreadySortedBy = AppConstants.FAVORITES;
                    /*editor.putString(getString(R.string.already_sorted_by),alreadySortedBy);
                    editor.apply();*/
                    onContentChanged("favorites");

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    private void onContentChanged(String filter) {

        progressBar.setVisibility(View.VISIBLE);
        progressTextView.setVisibility(View.VISIBLE);

        loadMovieData(alreadySortedBy);


    }


    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favs) {
                if(favs.size()>0) {
                    favMovies.clear();
                    favMovies = favs;
                }
                for (int i=0; i<favMovies.size(); i++) {
                    Log.d("info_of___favorites",favMovies.get(i).getTitle());
                }
                loadMovieData(alreadySortedBy);
            }
        });
    }
}

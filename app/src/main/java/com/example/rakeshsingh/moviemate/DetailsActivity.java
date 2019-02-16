package com.example.rakeshsingh.moviemate;

import androidx.lifecycle.LiveData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.rakeshsingh.moviemate.Adapters.TrailerPagerAdapter;
import com.example.rakeshsingh.moviemate.Database.FavoriteMovie;
import com.example.rakeshsingh.moviemate.Database.MovieDatabase;
import com.example.rakeshsingh.moviemate.ModelClasses.Result;
import com.example.rakeshsingh.moviemate.ModelClasses.TrailerResponse;
import com.example.rakeshsingh.moviemate.ModelClasses.TrailerResult;
import com.example.rakeshsingh.moviemate.retrofit.RetroInterface;
import com.example.rakeshsingh.moviemate.retrofit.RetrofitClient;
import com.example.rakeshsingh.moviemate.utils.AppConstants;
import com.example.rakeshsingh.moviemate.utils.AppExecutors;
import com.example.rakeshsingh.moviemate.utils.Formatter;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.trailers_viewPager)
    ViewPager trailersViewPager;
    @BindView(R.id.trailerErroCardView)
    CardView trailerError;
    @BindView(R.id.backdropPoster)
    ImageView movieBackDrop;
    @BindView(R.id.releaseDateTextView)
    TextView releaseDateTextView;
    @BindView(R.id.movieTitle)
    TextView movieTitle;
    @BindView(R.id.plotTextview)
    TextView plotTextView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.durationTextView)
    TextView durationTextView;
    @BindView(R.id.favButton)
    ImageView favButton;


    private ArrayList<TrailerResult> trailerList;
    private Intent getDetailsFromPoster;
    private int movieID;

    //For favorite movies
    private MovieDatabase mDb;
    private Boolean isFav = false;
    //private Result getDetailsFromPoster;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        getDetailsFromPoster=getIntent();
        movieID=getDetailsFromPoster.getIntExtra("MovieID",0);
        trailerList=new ArrayList<>();

        mDb=MovieDatabase.getInstance(getApplicationContext());
        movieTitle.setSelected(true);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final FavoriteMovie fmov = mDb.movieDao().loadMovieById(movieID);
                setFavorite((fmov != null)? true : false);
            }
        });



        favButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Long dateReceived= getDetailsFromPoster.getLongExtra("ReleaseDate",0);
                Date dateConverted=new Date(dateReceived);
                final FavoriteMovie mov = new FavoriteMovie(getDetailsFromPoster.getIntExtra("MovieID",0),
                        getDetailsFromPoster.getStringExtra("MovieTitle"),
                        dateConverted,
                        getDetailsFromPoster.getIntExtra("VoteAverage",0),
                        getDetailsFromPoster.getDoubleExtra("Popularity",0),
                        getDetailsFromPoster.getStringExtra("PlotSypnosis"),
                        getDetailsFromPoster.getStringExtra("MoviePoster"),
                        getDetailsFromPoster.getStringExtra("MovieBackDropPath")
                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFav) {
                            // delete item
                            mDb.movieDao().deleteMovie(mov);
                        } else {
                            // insert item
                            mDb.movieDao().insertMovie(mov);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setFavorite(!isFav);
                            }
                        });
                    }

                });
            }
        });



            loadDetails();
    }

    private void setFavorite(Boolean fav){
        if (fav) {
            isFav = true;
            favButton.setImageResource(R.drawable.favorite_icon_selected);
        } else {
            isFav = false;
            favButton.setImageResource(R.drawable.favorite_icon);
        }
    }

    private void loadDetails() {

        movieTitle.setText(getDetailsFromPoster.getStringExtra("MovieTitle"));
        plotTextView.setText(getDetailsFromPoster.getStringExtra("PlotSypnosis"));


        setUpRatingsAndDurations();

        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_error_black_24dp)
                .priority(Priority.HIGH);

        Glide.with(DetailsActivity.this)
                .asDrawable()
                .load(Uri.parse(getDetailsFromPoster.getStringExtra("MovieBackDropPath")))
                .apply(options)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_cloud_download_black_24dp).error(R.drawable.ic_error_black_24dp))
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(movieBackDrop);


        RetroInterface retroInterface= RetrofitClient.getRetrofit().create(RetroInterface.class);

        final Call<TrailerResponse> trailerResponseCall= retroInterface.getMovieTrailers(movieID, AppConstants.API_KEY);


        trailerResponseCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                trailerList = response.body().getResults();

                if (trailerList.size()!=0){

                    loadTrailersPager();

                }else{

                    trailerError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                trailerError.setVisibility(View.VISIBLE);
                Toast.makeText(DetailsActivity.this, "Sorry Internet Connection Not Found :( ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpRatingsAndDurations() {


        /* ratingBar.setRating(getDetailsFromPoster.getFloatExtra("VoteAverage",0));
       durationTextView.setText(Formatter.getFormattedDurationTime(getDetailsFromPoster.getIntExtra("Duration",0)));
       releaseDateTextView.setText(Formatter.getFormattedReleaseDate((Date) getDetailsFromPoster.getStringExtra("ReleaseDate")));*/


        ratingBar.setRating((float) (getDetailsFromPoster.getFloatExtra("VoteAverage",0)*0.5));


        /*durationTextView.setText(Formatter.getFormattedDurationTime(getDetailsFromPoster.getIntExtra("Duration",0)));
        releaseDateTextView.setVisibility(View.VISIBLE);

        releaseDateTextView.setText("Release Date: "+Formatter.getFormattedReleaseDate(new Date(getDetailsFromPoster.getLongExtra("ReleaseDate",0))));
*/

        RetroInterface retroInterface=RetrofitClient.getRetrofit().create(RetroInterface.class);

        final Call<Result> movieResultCall=retroInterface.getMovieDetails(movieID,AppConstants.API_KEY);

        movieResultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result =response.body();

                //ratingBar.setRating((float) (result.getVoteAverage()*0.5));


                durationTextView.setText(Formatter.getFormattedDurationTime(result.getDuration()));

                releaseDateTextView.setVisibility(View.VISIBLE);
                releaseDateTextView.setText("Release Date: "+Formatter.getFormattedReleaseDate(result.getReleaseDate()));


            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                releaseDateTextView.setVisibility(View.VISIBLE);
                trailerError.setVisibility(View.VISIBLE);
                durationTextView.setText("N/A :(");

            }
        });




    }

    private void loadTrailersPager() {

        trailersViewPager.setAdapter(new TrailerPagerAdapter(DetailsActivity.this,trailerList));

        trailersViewPager.setClipToPadding(false);

        trailersViewPager.setPadding(40,8,40,8);

        trailersViewPager.setPageMargin(20);

    }




    public void backPressed(View view){

        super.onBackPressed();
    }
}

package com.example.rakeshsingh.moviemate.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.rakeshsingh.moviemate.DetailsActivity;
import com.example.rakeshsingh.moviemate.ModelClasses.Result;
import com.example.rakeshsingh.moviemate.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.constraintlayout.Constraints.TAG;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.MovieHolder> {


    private Activity activity;
    private List<Result> movieList;

    public PosterAdapter(Activity activity,List<Result> movieList){
        this.activity=activity;
        this.movieList=movieList;
        }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card,viewGroup,false);

        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder movieHolder, int i) {


        Glide.with(activity)
                .load(Uri.parse(movieList.get(i).getPosterPath()))
                //.apply(RequestOptions.placeholderOf(R.drawable.ic_cloud_download_black_24dp).error(R.drawable.ic_error_black_24dp))
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(movieHolder.moviePoster);

        movieHolder.movieTitle.setSelected(true);
        movieHolder.movieTitle.setText(movieList.get(i).getTitle());



    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.movieTitle)
        TextView movieTitle;
        @BindView(R.id.Poster_image)
        ImageView moviePoster;


       public MovieHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this,view);

        }

        @Override
        public void onClick(View view) {

            Log.i("debug.... ", "onClick: ");

            int moviePosition=getAdapterPosition();

            //Log.i("onClick---->", String.valueOf(movieList.get(moviePosition).getDuration()));

            Intent intent=new Intent(activity.getApplicationContext(), DetailsActivity.class);

            /*System.out.println("-------------------"+movieList.get(moviePosition).getId()+ "  "+ movieList.get(moviePosition).getTitle()+" "+
                    movieList.get(moviePosition).getDuration());
*/
            intent.putExtra("MovieID",movieList.get(moviePosition).getId());
            intent.putExtra("MovieTitle",movieList.get(moviePosition).getTitle());
            intent.putExtra("PlotSypnosis",movieList.get(moviePosition).getOverview());
            intent.putExtra("MoviePoster",movieList.get(moviePosition).getPosterPath());
            intent.putExtra("Duration",movieList.get(moviePosition).getDuration());
            intent.putExtra("MovieBackDropPath",movieList.get(moviePosition).getBackdropPath());
            intent.putExtra("VoteAverage",movieList.get(moviePosition).getVoteAverage());
            intent.putExtra("ReleaseDate",movieList.get(moviePosition).getReleaseDate());
            intent.putExtra("Popularity",movieList.get(moviePosition).getReleaseDate());

            activity.startActivity(intent);

        }





       /* @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int moviePosition=getAdapterPosition();
            Intent intent=new Intent(activity.getApplicationContext(), DetailsActivity.class);
            intent.putExtra("MovieID",movieList.get(moviePosition).getId());
            intent.putExtra("MovieTitle",movieList.get(moviePosition).getTitle());
            intent.putExtra("PlotSypnosis",movieList.get(moviePosition).getOverview());
            intent.putExtra("MoviePoster",movieList.get(moviePosition).getPosterPath());
            intent.putExtra("MovieBackDropPath",movieList.get(moviePosition).getBackdropPath());
            intent.putExtra("VoteAverage",movieList.get(moviePosition).getVoteAverage());
            //intent.putExtra("ReleaseDate",movieList.get(moviePosition).getReleaseDate());
            //intent.putExtra("Duration",movieList.get(moviePosition).getDuration());
            activity.startActivity(intent);
            return false;
        }*/


    }
}

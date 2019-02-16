package com.example.rakeshsingh.moviemate.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rakeshsingh.moviemate.DetailsActivity;
import com.example.rakeshsingh.moviemate.ModelClasses.TrailerResult;
import com.example.rakeshsingh.moviemate.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TrailerPagerAdapter extends PagerAdapter {

    @BindView(R.id.trailer_thumb)
    ImageView trailerThumb;
    @BindView(R.id.play_trailer_btn)
    ImageView playBtn;

    private ArrayList<TrailerResult> trailers;
    private String youtubeKey;
    private Activity activity;

    public TrailerPagerAdapter(DetailsActivity activity, ArrayList<TrailerResult> trailerList) {

        this.activity=activity;
        this.trailers=trailerList;

    }


    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater =
                (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.trailer_item, container, false);
        ButterKnife.bind(this, view);

        youtubeKey = trailers.get(position).getYoutubeKey();
        Glide.with(view)
                .load(Uri.parse("https://i3.ytimg.com/vi/"
                        + youtubeKey + "/hqdefault.jpg"))
                .transition(withCrossFade())
                .into(trailerThumb);

        container.addView(view);

        return view;
    }

    @OnClick(R.id.play_trailer_btn)
    void playTrailer() {
        Intent openInApp = new Intent(Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + youtubeKey));
        Intent openInBrowser = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + youtubeKey));
        try {
            activity.startActivity(openInApp);
        } catch (Exception e) {
            activity.startActivity(openInBrowser);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }
}

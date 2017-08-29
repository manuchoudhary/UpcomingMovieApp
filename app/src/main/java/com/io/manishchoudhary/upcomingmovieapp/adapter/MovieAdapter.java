package com.io.manishchoudhary.upcomingmovieapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.io.manishchoudhary.upcomingmovieapp.model.Movie;
import com.io.manishchoudhary.upcomingmovieapp.R;
import com.io.manishchoudhary.upcomingmovieapp.activity.DetailActivity;
import com.io.manishchoudhary.upcomingmovieapp.app.AppController;

import java.util.List;

/**
 * Created by manishchoudhary on 28/08/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader;

    public MovieAdapter(Activity activity, List<Movie> feedItems) {
        this.activity = activity;
        this.movieItems = feedItems;
        imageLoader = AppController.getInstance().getImageLoader();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public class TrackHolder extends ViewHolder {
        TextView movieName, releaseDate, adult;
        com.android.volley.toolbox.NetworkImageView moviePic;
        CardView card;

        public TrackHolder(View v) {
            super(v);
            this.card = (CardView) v.findViewById(R.id.card_view);
            this.movieName = (TextView) v.findViewById(R.id.movieName);
            this.releaseDate = (TextView) v.findViewById(R.id.releaseDate);
            this.adult = (TextView) v.findViewById(R.id.adult);
            this.moviePic = (com.android.volley.toolbox.NetworkImageView) v.findViewById(R.id.moviePic);
        }
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);
        return new TrackHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final TrackHolder holder = (TrackHolder) viewHolder;
        if (!TextUtils.isEmpty(movieItems.get(position).getTitle())) {
            holder.movieName.setText(movieItems.get(position).getTitle());
            holder.releaseDate.setText(movieItems.get(position).getReleaseDate());
            if(movieItems.get(position).isAdult()) {
                holder.adult.setText("A");
            }else{
                holder.adult.setText("U/A");
            }
            holder.moviePic.setImageUrl(movieItems.get(position).getImage(), imageLoader);
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetails(movieItems.get(position));
                }
            });
        } else {
            holder.movieName.setVisibility(View.GONE);
            holder.releaseDate.setVisibility(View.GONE);
            holder.adult.setVisibility(View.GONE);
        }
    }

    public void showDetails(Movie item){
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra("item", item);
        activity.startActivity(intent);
    }
}


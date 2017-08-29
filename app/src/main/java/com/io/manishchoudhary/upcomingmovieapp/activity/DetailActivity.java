package com.io.manishchoudhary.upcomingmovieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.io.manishchoudhary.upcomingmovieapp.model.Movie;
import com.io.manishchoudhary.upcomingmovieapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by manishchoudhary on 28/08/17.
 */

public class DetailActivity extends AppCompatActivity {

    TextView title, overview;
    RatingBar stars;
    List<String> listImageURL;
    BannerSlider bannerSlider;
    ActionBar actionBar;
    Movie item;
    private String URL_FEED = "https://api.themoviedb.org/3/movie/";
    private String api_key = "/images?api_key=b7cd3340a794e5a2f35e3abb820b497f";
    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        actionBar = getSupportActionBar();
        listImageURL = new ArrayList<String>();
        bannerSlider = (BannerSlider) findViewById(R.id.imageSlider);
        title = (TextView)findViewById(R.id.title);
        overview = (TextView)findViewById(R.id.overview);
        stars = (RatingBar)findViewById(R.id.stars);
        Intent intent = getIntent();
        populateData(intent);
        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(DetailActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setBannerImages(Double id){

        String URL = URL_FEED + id.toString() + api_key;

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    getImages(response);
                    addBannerImages();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonReq);
    }

    public void getImages(JSONObject response){
        try {
            JSONArray feedArray = response.getJSONArray("backdrops");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                String filePath = feedObj.getString("file_path");
                listImageURL.add("https://image.tmdb.org/t/p/original" + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBannerImages(){
        List<Banner> remoteBanners=new ArrayList<>();
        if(listImageURL.size() < 5){
            for(int i = 0 ; i < listImageURL.size() ; i++) {
                remoteBanners.add(new RemoteBanner(listImageURL.get(i)));
            }
        }else{
            for(int i = 0 ; i < 5 ; i++) {
                remoteBanners.add(new RemoteBanner(listImageURL.get(i)));
            }
        }

        bannerSlider.setBanners(remoteBanners);
    }

    public void populateData(Intent item){
        Movie movie = (Movie)item.getSerializableExtra("item");
        setBannerImages(movie.getId());
        title.setText("TITLE : " + movie.getTitle());
        actionBar.setTitle(movie.getTitle());
        overview.setText("OVERVIEW : " + movie.getOverview());
        float star = (float)(movie.getStars()/2);
        stars.setRating(star);
    }
}

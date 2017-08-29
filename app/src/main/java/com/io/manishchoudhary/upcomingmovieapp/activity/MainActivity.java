package com.io.manishchoudhary.upcomingmovieapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.io.manishchoudhary.upcomingmovieapp.model.Movie;
import com.io.manishchoudhary.upcomingmovieapp.adapter.MovieAdapter;
import com.io.manishchoudhary.upcomingmovieapp.R;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView listView;
    private MovieAdapter listAdapter;
    private List<Movie> movieItems;
    private String URL_FEED = "https://api.themoviedb.org/3/movie/upcoming?api_key=b7cd3340a794e5a2f35e3abb820b497f";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (RecyclerView) findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        movieItems = new ArrayList<Movie>();
        callURLFeed();
    }

    private void callURLFeed() {

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                    listAdapter = new MovieAdapter(MainActivity.this, movieItems);
                    listView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
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

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("results");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                Movie item = new Movie();
                item.setId(feedObj.getDouble("id"));
                item.setStars(feedObj.getDouble("vote_average"));
                item.setTitle(feedObj.getString("title"));
                item.setImage("https://image.tmdb.org/t/p/original" + feedObj.getString("poster_path"));
                item.setOverview(feedObj.getString("overview"));
                item.setAdult(feedObj.getBoolean("adult"));
                item.setReleaseDate(feedObj.getString("release_date"));
                movieItems.add(item);
            }
            Toast.makeText(MainActivity.this, "Json Loaded", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

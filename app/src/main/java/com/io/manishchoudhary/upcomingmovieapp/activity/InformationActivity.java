package com.io.manishchoudhary.upcomingmovieapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.io.manishchoudhary.upcomingmovieapp.R;

/**
 * Created by manishchoudhary on 29/08/17.
 */

public class InformationActivity extends AppCompatActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Information");
    }
}

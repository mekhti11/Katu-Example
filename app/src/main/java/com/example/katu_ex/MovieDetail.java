package com.example.katu_ex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MovieDetail extends AppCompatActivity {

    private final static String TAG = "MovieDetail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        String id = getIntent().getStringExtra("imdbID");
        Log.d(TAG, "onCreate: "+id);
    }
}

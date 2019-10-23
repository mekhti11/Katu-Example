package com.example.katu_ex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katu_ex.Adapter.MovieAdapter;
import com.example.katu_ex.Entity.Movie;
import com.example.katu_ex.Listener.MovieClickListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity {

    private final static String TAG = "MovieDetail";
    private SpinKitView spinKitView;
    private ImageView poster;
    private TextView title,runtime,imdb,genre,plot;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        poster = findViewById(R.id.movie_poster);
        title = findViewById(R.id.movie_title);
        runtime = findViewById(R.id.movie_runtime);
        imdb = findViewById(R.id.movie_imdb);
        genre = findViewById(R.id.movie_genre);
        plot = findViewById(R.id.movie_plot);

        spinKitView = findViewById(R.id.movie_spin_kit);
        createSpinKit();


        String id = getIntent().getStringExtra("imdbID");
        getMovieDetails(id);
    }


    public void createSpinKit(){
        CubeGrid cubeGrid = new CubeGrid();
        spinKitView.setIndeterminateDrawable(cubeGrid);
    }


    public void getMovieDetails(String id){
        final String apiUrl = getResources().getString(R.string.api_url_id)+id+"&"+getResources().getString(R.string.apikey);
        Log.d("Anasayfa", "getMovies: "+apiUrl);
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = "";
                Log.d(TAG, "doInBackground: "+strings[0]);

                try{
                    URL url = new URL(strings[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader
                            reader = new
                            BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = "";
                    while((line = reader.readLine()) != null){
                        response += line + "\n";
                    }
                } catch (Exception e){
                    Log.d(TAG, "doInBackground: "+e.getMessage());
                    return "Exception";
                }
                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d("Anasayfa", "onPostExecute: "+s);
                JsonObject jo = new Gson().fromJson(s, JsonObject.class);
                Log.d(TAG, "onPostExecute: "+jo.get("Response"));
                if(jo.get("Response").toString().compareTo("False") == 0){
                    Log.d(TAG, "onPostExecute: "+jo.get("Error"));
                }else{
                    GsonBuilder builder = new GsonBuilder();
                    builder.setPrettyPrinting();
                    Gson gson = builder.create();
                    Movie movie = gson.fromJson(jo,Movie.class);
                    Log.d(TAG, "onPostExecute: imdbRating: "+movie.getImdbRating());
                    setUI(movie);
                    addToFirebaseLog(movie);
                }
            }
        };
        task.execute(apiUrl);
    }

    private void addToFirebaseLog(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,movie.getClass().getName());
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movie.getImdbID());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, movie.getTitle());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,bundle );
    }

    private void setUI(Movie movie) {
        Picasso.get()
                .load(movie.getPoster())
                .into(poster);
        title.setText(movie.getTitle());
        genre.setText(movie.getGenre());
        runtime.setText(movie.getYear()+"    "+movie.getRuntime()+"    "+movie.getRated());
        imdb.setText("IMDB : " + movie.getImdbRating());
        plot.setText(movie.getPlot());
        spinKitView.setVisibility(View.INVISIBLE);
    }
}

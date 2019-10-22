package com.example.katu_ex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.katu_ex.Adapter.MovieAdapter;
import com.example.katu_ex.Entity.Movie;
import com.example.katu_ex.Listener.MovieClickListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Anasayfa extends AppCompatActivity {

    private static String TAG = "ANASAYFA";

    private MaterialSearchBar searchBar;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private SpinKitView spinKitView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        searchBar = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.movies_rv);
        spinKitView = findViewById(R.id.spin_kit);

        createSpinKit();

        createSearchBar();

    }

    public void createSpinKit(){
        CubeGrid cubeGrid = new CubeGrid();
        spinKitView.setIndeterminateDrawable(cubeGrid);
        spinKitView.setVisibility(View.INVISIBLE);
    }

    public void createSearchBar(){
        searchBar.setSpeechMode(false);
        searchBar.setHint("");
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public void getMovies(String title){
        final String apiUrl = getResources().getString(R.string.api_url)+title+"&"+getResources().getString(R.string.apikey);
        Log.d("Anasayfa", "getMovies: "+apiUrl);
        spinKitView.setVisibility(View.VISIBLE);
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
                ArrayList<Movie> movies = new ArrayList<>();
                JsonObject jo = new Gson().fromJson(s, JsonObject.class);
                Log.d(TAG, "onPostExecute: "+jo.get("Response"));
                if(jo.get("Response").toString().compareTo("False") == 0){
                    Log.d(TAG, "onPostExecute: "+jo.get("Error"));
                }else{
                    GsonBuilder builder = new GsonBuilder();
                    builder.setPrettyPrinting();
                    Gson gson = builder.create();
                    movies = gson.fromJson(jo.get("Search"),new TypeToken<ArrayList<Movie>>(){}.getType());
                    if(movies==null){
                        movies=new ArrayList<>();
                    }
                }
                final ArrayList<Movie> finalMovies = movies;
                adapter = new MovieAdapter(movies, new MovieClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i= new Intent(Anasayfa.this,MovieDetail.class);
                        i.putExtra("imdbID", finalMovies.get(position).getImdbID());
                        startActivity(i);
                    }
                });
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                spinKitView.setVisibility(View.INVISIBLE);

            }
        };
        task.execute(apiUrl);
    }
}

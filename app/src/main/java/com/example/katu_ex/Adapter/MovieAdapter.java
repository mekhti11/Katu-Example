package com.example.katu_ex.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katu_ex.Entity.Movie;
import com.example.katu_ex.Listener.MovieClickListener;
import com.example.katu_ex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private ArrayList<Movie> movies;
    private MovieClickListener listener;

    public MovieAdapter(ArrayList<Movie> movies, MovieClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    //TODO make list of movies grid

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_rv_cell,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.title.setText(movies.get(position).getTitle());
        holder.year.setText("("+movies.get(position).getYear()+")");
        Picasso.get()
                .load(movies.get(position).getPoster())
                .fit()
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        public ImageView poster;
        public TextView title,year;
        public CardView cv;

        public MovieViewHolder(@NonNull View view) {
            super(view);

            poster = view.findViewById(R.id.poster);
            title = view.findViewById(R.id.title);
            year = view.findViewById(R.id.year);
            cv = view.findViewById(R.id.cv);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v,getAdapterPosition());
                }
            });

        }
    }

}

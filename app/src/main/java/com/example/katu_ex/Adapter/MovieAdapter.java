package com.example.katu_ex.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katu_ex.Entity.Movie;
import com.example.katu_ex.Listener.MovieClickListener;
import com.example.katu_ex.R;

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
        holder.txt1.setText(movies.get(position).getTitle());
//        holder.txt2.setText(movies.get(2*position+1).getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        public ImageView img1,img2;
        public TextView txt1,txt2;
        public RelativeLayout rl1,rl2;

        public MovieViewHolder(@NonNull View view) {
            super(view);

            img1 = view.findViewById(R.id.img1);
            img2 = view.findViewById(R.id.img2);
            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
            rl1 = view.findViewById(R.id.rl1);
            rl2 = view.findViewById(R.id.rl2);

            rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v,getAdapterPosition());
                }
            });
            rl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v,getAdapterPosition());
                }
            });
        }
    }

}

package com.sahil.roomexample.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil.roomexample.R;
import com.sahil.roomexample.modelMovie.Result;

import java.util.ArrayList;

public class CustomRecyclerview extends RecyclerView.Adapter<CustomRecyclerview.ViewHolder> {

    Context context;
    ArrayList<Result> arrayList;

    public CustomRecyclerview(Context context, ArrayList<Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result movies = arrayList.get(position);
        holder.name.setText(movies.getTitle());
        holder.releaseDate.setText(String.valueOf(movies.getReleaseDate()));
        holder.averageScore.setText(" Overall: "+movies.getVoteAverage()+"/10");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, releaseDate,averageScore;
        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvTitle);
            releaseDate = itemView.findViewById(R.id.tvReleaseDate);
            averageScore = itemView.findViewById(R.id.tvRatings);
        }
    }
}

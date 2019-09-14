package com.sahil.roomexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sahil.roomexample.database.MovieDatabase;
import com.sahil.roomexample.modelMovie.MoviesResponse;
import com.sahil.roomexample.modelMovie.Result;
import com.sahil.roomexample.recyclerview.CustomRecyclerview;
import com.sahil.roomexample.retrofit.NetworkClient;
import com.sahil.roomexample.retrofit.UploadAPIs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String DATABASE_NAME = "movies_db.db";
    private MovieDatabase movieDatabase;
    private RecyclerView recyclerview;
    private ArrayList<Result> arrayList;
    private CustomRecyclerview adapter;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieDatabase = Room.databaseBuilder(getApplicationContext(), MovieDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        initViews();
        if(utils.isInternetAvailable()){
            getDatafromRetrofit();
        }
        else {
            fetchfromRoom();
        }
    }

    private void getDatafromRetrofit() {
        UploadAPIs apiService = NetworkClient.getRetrofitClient(MainActivity.this).create(UploadAPIs.class);
        final Call<MoviesResponse> result = apiService.getTMDBdata(BuildConfig.API_KEY,"en-US",1);
        result.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, final Response<MoviesResponse> response) {
//                Log.d("Retrofit", "onResponse: "+response.body().getResults().get(0).getTitle());

                if (response.isSuccessful()){
                    try{


                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<Result> moviesList=new ArrayList<>();
                                for (Result result: response.body().getResults()) {
                                    Result addresult = new Result();
                                    addresult.setId(result.getId());
                                    addresult.setTitle(result.getTitle());
                                    addresult.setAdult(result.getAdult());
                                    addresult.setBackdropPath(result.getBackdropPath());
                                    addresult.setOriginalLanguage(result.getOriginalLanguage());
                                    addresult.setOverview(result.getOverview());
                                    addresult.setPosterPath(result.getPosterPath());
                                    addresult.setReleaseDate(result.getReleaseDate());
                                    addresult.setVideo(result.getVideo());
                                    addresult.setVoteCount(result.getVoteCount());
                                    addresult.setVoteAverage(result.getVoteAverage());
                                    addresult.setPopularity(result.getPopularity());
                                    moviesList.add(addresult);
                                    Log.d("Retrofit", "Results: "+addresult);
                                }
                                movieDatabase.daoAccess().insertMultipleMovies (moviesList);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }
                        });
                        thread.start();

                        fetchfromRoom();
                        pb.setVisibility(View.GONE);

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                else{
                    pb.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Something went wrong!Please try again late.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                call.cancel();
                pb.setVisibility(View.GONE);
            }
        });
    }

    private void initViews() {
        pb = findViewById(R.id.pb);

        recyclerview = findViewById(R.id.recyclerview);
        arrayList = new ArrayList<>();
        adapter = new CustomRecyclerview(this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(adapter);
    }

    private void fetchfromRoom() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Result> getList = movieDatabase.daoAccess().getAll();
                Log.d("Fetch Room", "run: "+getList);
                arrayList.clear();
                arrayList.addAll(getList);
//                for (Result res: getList) {
//                    Result addresult = new Result();
//                    addresult.setId(res.getId());
//                    addresult.setTitle(res.getTitle());
//                    arrayList.add(addresult);
//                }
                // refreshing recycler view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }

}

package com.example.myrestaurants.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myrestaurants.network.YelpApi;
import com.example.myrestaurants.R;
import com.example.myrestaurants.adapters.RestaurantListAdapter;
import com.example.myrestaurants.models.Business;
import com.example.myrestaurants.models.YelpBusinessesSearchResponse;
import com.example.myrestaurants.network.YelpClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListActivity extends AppCompatActivity {
    public static final String TAG = RestaurantListActivity.class.getSimpleName();

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    private RestaurantListAdapter mAdapter;
    public List<Business> restaurants;
    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }
    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        ButterKnife.bind(this);



//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String restaurant = ((TextView)view).getText().toString();
//                Log.v("RestaurantListActivity", "In the onItemClickListener!");
//                Toast.makeText(RestaurantListActivity.this, restaurant, Toast.LENGTH_LONG).show();
////                Log.v(TAG, "In the onItemClickListener!");
//
//            }
//        });


        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

       YelpApi client = YelpClient.getClient();

        Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");

        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
            @Override
            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {

                hideProgressBar();

                if (response.isSuccessful()) {

                    restaurants = response.body().getBusinesses();
                    mAdapter = new RestaurantListAdapter(RestaurantListActivity.this, restaurants);
                    mRecyclerView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantListActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);


                    showRestaurants();

                }else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
             
                hideProgressBar();
                showFailureMessage();

            }



        });


    }
}

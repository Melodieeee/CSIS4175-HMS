package com.example.howsMyStylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.howsMyStylist.Adapter.PopularReviewAdapter;
import com.example.howsMyStylist.Adapter.PopularSalonAdapter;
import com.example.howsMyStylist.Adapter.PopularStylistAdapter;
import com.example.howsMyStylist.Model.Review;
import com.example.howsMyStylist.Model.Salon;
import com.example.howsMyStylist.Model.Stylist;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private static final String TAG = "test";

    DatabaseReference databaseReference;

    ArrayList<Stylist> stylistList;
    RecyclerView stylistRecyclerView;
    PopularStylistAdapter popularStylistAdapter;

//    ArrayList<Salon> salonList;
//    RecyclerView salonRecyclerView;
//    PopularSalonAdapter popularSalonAdapter;

    ArrayList<Review> reviewList;
    RecyclerView reviewRecyclerView;
    PopularReviewAdapter popularReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        popularStylist();

        //popularSalon();

        popularReview();

        // Initialize navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Home is selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        FloatingActionButton fab = findViewById(R.id.fab);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(HomePageActivity.this, HomePageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(HomePageActivity.this, UploadUserProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, UploadReviewActivity.class));
            }
        });
    }

    private void popularReview() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Review");

        reviewList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewRecyclerView = findViewById(R.id.popularReviewRecyclerView);
        reviewRecyclerView.setLayoutManager(linearLayoutManager);
        popularReviewAdapter = new PopularReviewAdapter(this, reviewList);
        reviewRecyclerView.setAdapter(popularReviewAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Review review = dataSnapshot.getValue(Review.class);
                    reviewList.add(review);
                }

                Gson gson = new Gson();
                String result = gson.toJson(reviewList);

                Log.d("reviewList", result);
                popularStylistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void popularSalon() {
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Salon");
//        salonList = new ArrayList<>();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        salonRecyclerView = findViewById(R.id.popularSalonRecyclerView);
//        salonRecyclerView.setLayoutManager(linearLayoutManager);
//        popularSalonAdapter = new PopularSalonAdapter(this, salonList);
//        salonRecyclerView.setAdapter(popularSalonAdapter);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    Salon salon = dataSnapshot.getValue(Salon.class);
//                    salonList.add(salon);
//                }
//                popularSalonAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void popularStylist() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Stylist");
        stylistList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        stylistRecyclerView = findViewById(R.id.popularStylistRecyclerView);
        stylistRecyclerView.setLayoutManager(linearLayoutManager);
        popularStylistAdapter = new PopularStylistAdapter(this, stylistList);
        stylistRecyclerView.setAdapter(popularStylistAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Stylist stylist = dataSnapshot.getValue(Stylist.class);
                    stylistList.add(stylist);
                }
                popularStylistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
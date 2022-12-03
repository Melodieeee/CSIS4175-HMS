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
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HomePageActivity extends AppCompatActivity implements PopularStylistAdapter.onButtonClick, PopularSalonAdapter.onButtonClick {

    private static final String TAG = "test";

    DatabaseReference databaseReference;
    SearchView searchView;

    Map<String, Stylist> stylistMap;
    ArrayList<String> userFavStylistList;
    RecyclerView stylistRecyclerView;
    PopularStylistAdapter popularStylistAdapter;

    Map<String, Salon> salonMap;
    ArrayList<String> userFavSalonList;
    RecyclerView salonRecyclerView;
    PopularSalonAdapter popularSalonAdapter;

    ArrayList<Review> reviewList;
    RecyclerView reviewRecyclerView;
    PopularReviewAdapter popularReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        fetchUserFavStylist();
        popularStylist();

        fetchUserFavSalon();
        popularSalon();

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

        searchView = findViewById(R.id.searchbox);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    //search for stylist and salon
    private void search(String str) {
        // search for stylist
        Map<String,Stylist> mStylistMap = stylistMap.entrySet()
                .stream()
                .filter(map -> map.getValue().getfName().toLowerCase().contains(str.toLowerCase()) ||
                        map.getValue().getlName().toLowerCase().contains(str.toLowerCase()) ||
                        map.getValue().getSalonName().toLowerCase().contains(str.toLowerCase())
                ).collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
        PopularStylistAdapter stylistAdapter = new PopularStylistAdapter(this, mStylistMap);
        stylistRecyclerView.setAdapter(stylistAdapter);

        // search for salon
        Map<String,Salon> mSalonMap = salonMap.entrySet()
                .stream()
                .filter(map -> map.getValue().getSalonName().toLowerCase().contains(str.toLowerCase())
                ).collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
        PopularSalonAdapter salonAdapter = new PopularSalonAdapter(this, mSalonMap);
        salonRecyclerView.setAdapter(salonAdapter);

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

    private void popularSalon() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Salon");
        salonMap = new HashMap<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        salonRecyclerView = findViewById(R.id.popularSalonRecyclerView);
        salonRecyclerView.setLayoutManager(linearLayoutManager);
        popularSalonAdapter = new PopularSalonAdapter(this, salonMap, userFavSalonList);
        salonRecyclerView.setAdapter(popularSalonAdapter);
        // for favorite changed
        popularSalonAdapter.setListener(HomePageActivity.this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Salon salon = dataSnapshot.getValue(Salon.class);
                    salonMap.put(dataSnapshot.getKey(), salon);
                }
                popularSalonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void popularStylist() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Stylist");
        stylistMap = new HashMap<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        stylistRecyclerView = findViewById(R.id.popularStylistRecyclerView);
        stylistRecyclerView.setLayoutManager(linearLayoutManager);
        popularStylistAdapter = new PopularStylistAdapter(this, stylistMap, userFavStylistList);
        stylistRecyclerView.setAdapter(popularStylistAdapter);
        // for favorite changed
        popularStylistAdapter.setListener(HomePageActivity.this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Stylist stylist = dataSnapshot.getValue(Stylist.class);
                    stylistMap.put(dataSnapshot.getKey(), stylist);
                }
                popularStylistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //userFavStylist
    public void fetchUserFavStylist() {
        userFavStylistList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favStylistList").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot eachChild : snapshot.getChildren()) {
                            userFavStylistList.add(eachChild.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    //userFavSalon
    public void fetchUserFavSalon() {
        userFavSalonList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favSalonList").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot eachChild : snapshot.getChildren()) {
                            userFavSalonList.add(eachChild.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    public void onFavoriteStylistChosen(String favoriteId, boolean favoriteStatus) {
        //debug
        Toast.makeText(this, (favoriteStatus? "Add to ": "Remove from ") + "your favorite list", Toast.LENGTH_SHORT).show();
        Log.d("StylistIdLiked", favoriteId + "  " + String.valueOf(favoriteStatus));
        //update to database
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("favStylistList");

        if (favoriteStatus == true) {
            //add id to user's favStylistList
            databaseReference.child(favoriteId).setValue("favStylistId");
        } else {
            //remove id from user's favStylistList
            databaseReference.child(favoriteId).removeValue();
        }
    }

    @Override
    public void onFavoriteSalonChosen(String favoriteId, boolean favoriteStatus) {
        //debug
        Toast.makeText(this, (favoriteStatus? "Add to ": "Remove from ") + "your favorite list", Toast.LENGTH_SHORT).show();
        Log.d("SalonIdLiked", favoriteId + "  " + String.valueOf(favoriteStatus));
        //update to database
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("favSalonList");

        if (favoriteStatus == true){
            databaseReference.child(favoriteId).setValue("favSalonId");
        } else {
            databaseReference.child(favoriteId).removeValue();
        }
    }
}
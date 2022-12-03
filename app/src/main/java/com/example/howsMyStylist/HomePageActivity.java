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
import java.util.stream.Stream;

public class HomePageActivity extends AppCompatActivity implements PopularStylistAdapter.onButtonClick, PopularSalonAdapter.onButtonClick {

    private static final String TAG = "test";

    DatabaseReference databaseReference;

    Map<String, Stylist> stylistMap;
    ArrayList<String> userFavStylistList;
    RecyclerView stylistRecyclerView;
    PopularStylistAdapter popularStylistAdapter;

    Map<String, Salon> salonListMap;
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
    }

    //search stylist and salon
//    private void search(String str) {
//        Map<String,Stylist> mStylistMap = new HashMap<>();
//        for (Stylist stylist: stylistMap.values()) {
//            if (stylist.getfName().toLowerCase().contains(str.toLowerCase()) ||
//                    stylist.getlName().toLowerCase().contains(str.toLowerCase()) ||
//                    stylist.getSalonName().toLowerCase().contains(str.toLowerCase())) {
//
//                Stream<String> keyStream = keys(stylistMap, stylist);
//                mStylistMap = keyStream.collect(Collectors.toMap(, ));
//            }
//        }
//        PopularStylistAdapter adapter = new PopularStylistAdapter(mStylistList);
//        stylistCardRecyclerView.setAdapter(adapter);
//    }

    public <K, V> Stream<K> keys(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
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
        salonListMap = new HashMap<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        salonRecyclerView = findViewById(R.id.popularSalonRecyclerView);
        salonRecyclerView.setLayoutManager(linearLayoutManager);
        popularSalonAdapter = new PopularSalonAdapter(this, salonListMap, userFavSalonList);
        salonRecyclerView.setAdapter(popularSalonAdapter);

        popularSalonAdapter.setListener(HomePageActivity.this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Salon salon = dataSnapshot.getValue(Salon.class);
                    salonListMap.put(dataSnapshot.getKey(), salon);
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
        //stylistList = new ArrayList<>();
        stylistMap = new HashMap<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        stylistRecyclerView = findViewById(R.id.popularStylistRecyclerView);
        stylistRecyclerView.setLayoutManager(linearLayoutManager);
        popularStylistAdapter = new PopularStylistAdapter(this, stylistMap, userFavStylistList);
        //popularStylistAdapter.setUlist(userFavStylistList);

        stylistRecyclerView.setAdapter(popularStylistAdapter);
        // favorite
        popularStylistAdapter.setListener(HomePageActivity.this);
        //stylistIdList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Stylist stylist = dataSnapshot.getValue(Stylist.class);
                    //stylistList.add(stylist);
                    //stylistIdList.add(dataSnapshot.getKey());
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

    @Override
    public void onFavoriteStylistChosen(String id, boolean favoriteStatus) {
        //debug
        Toast.makeText(HomePageActivity.this, id + "  " + String.valueOf(favoriteStatus), Toast.LENGTH_SHORT).show();
        //update to database
        //String favoriteId = stylistIdList.get(position);
        String favoriteId = id;

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
    public void onFavoriteSalonChosen(String id, boolean favoriteStatus) {
        //debug
        Toast.makeText(HomePageActivity.this, id + "  " + String.valueOf(favoriteStatus), Toast.LENGTH_SHORT).show();
        String favoriteId = id;
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
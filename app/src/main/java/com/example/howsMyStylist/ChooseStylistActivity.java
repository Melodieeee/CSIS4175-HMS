package com.example.howsMyStylist;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.howsMyStylist.Adapter.PopupStylistAdapter;
import com.example.howsMyStylist.Model.Stylist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseStylistActivity extends AppCompatActivity implements PopupStylistAdapter.onListItemClick {

    DatabaseReference databaseReference;
    List<Stylist> stylistList;
    RecyclerView stylistCardRecyclerView;
    SearchView searchView;
    String stylistName, salonName;
    Button btn_createStylist;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_stylist);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // recyclerview
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Stylist");
        stylistCardRecyclerView = findViewById(R.id.rv_stylistCard);
        searchView = findViewById(R.id.searchView_stylistAndSalon);
        // click txt to open create stylist page
        btn_createStylist = findViewById(R.id.btn_createStylist);
        btn_createStylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseStylistActivity.this, UploadStylistProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        stylistList = new ArrayList<>();
                        for(DataSnapshot ds: snapshot.getChildren()) {
                            stylistList.add(ds.getValue(Stylist.class));
                        }
                        PopupStylistAdapter adapter = new PopupStylistAdapter(stylistList);
                        stylistCardRecyclerView.setLayoutManager(new LinearLayoutManager(ChooseStylistActivity.this));
                        stylistCardRecyclerView.setAdapter(adapter);
                        // pass selected data to the activity
                        adapter.setmListenr(ChooseStylistActivity.this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ChooseStylistActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    private void search(String str) {
        ArrayList<Stylist> mStylistList = new ArrayList<>();
        for (Stylist stylist: stylistList) {
            if (stylist.getfName().toLowerCase().contains(str.toLowerCase()) ||
                    stylist.getlName().toLowerCase().contains(str.toLowerCase()) ||
                    stylist.getSalonName().toLowerCase().contains(str.toLowerCase())){
                mStylistList.add(stylist);
            }
        }
        PopupStylistAdapter adapter = new PopupStylistAdapter(mStylistList);
        stylistCardRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onStylistChosen(String stylistName, String salonName) {
        this.stylistName = stylistName;
        this.salonName = salonName;
        Intent intent = new Intent(ChooseStylistActivity.this, UploadReviewActivity.class);
        intent.putExtra("stylistName", stylistName);
        intent.putExtra("salonName", salonName);
        startActivity(intent);
        finish();
    }

}
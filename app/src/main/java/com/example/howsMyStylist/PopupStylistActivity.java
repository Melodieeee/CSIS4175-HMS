package com.example.howsMyStylist;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
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

public class PopupStylistActivity extends Activity implements PopupStylistAdapter.onListItemClick {

    DatabaseReference databaseReference;
    List<Stylist> stylistList;
    RecyclerView stylistCardRecyclerView;
    SearchView searchView;
    String stylistName, salonName;
    TextView txv_createStylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_stylist);

        // set the pop up window's size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

        // recyclerview
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Stylist");
        stylistCardRecyclerView = findViewById(R.id.rv_stylistCard);
        searchView = findViewById(R.id.searchView_stylistAndSalon);
        // click txt to open create stylist page
        txv_createStylist = findViewById(R.id.txv_createStylist);
        txv_createStylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopupStylistActivity.this, UploadStylistProfileActivity.class);
                startActivity(intent);
                finish();
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
//                             Stylist catchStylist = ds.getValue(Stylist.class);
//                             stylistList.add(catchStylist);
                             stylistList.add(ds.getValue(Stylist.class));
                         }
                         PopupStylistAdapter adapter = new PopupStylistAdapter(stylistList);
                         stylistCardRecyclerView.setLayoutManager(new LinearLayoutManager(PopupStylistActivity.this));
                         stylistCardRecyclerView.setAdapter(adapter);
                         // pass selected data to the activity
                         adapter.setmListenr(PopupStylistActivity.this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PopupStylistActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(PopupStylistActivity.this, UploadReviewActivity.class);
        intent.putExtra("stylistName", stylistName);
        intent.putExtra("salonName", salonName);
        startActivity(intent);
        finish();
    }

}
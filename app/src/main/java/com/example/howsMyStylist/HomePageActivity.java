package com.example.howsMyStylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        //For testing
        Button btnGoReview = findViewById(R.id.btnGoReview);
        btnGoReview.setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, UploadUserProfileActivity.class));
            finish();
        });

        Button btnGoProfile = findViewById(R.id.btnGoProfile);
        btnGoProfile.setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, UploadReviewActivity.class));
            finish();
        });

        Button btnGoCreateSalon = findViewById(R.id.btnGoCreateSalon);
        btnGoCreateSalon.setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, UploadSalonProfileActivity.class));
            finish();
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(HomePageActivity.this,"Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
            finish();
        });
    }
}
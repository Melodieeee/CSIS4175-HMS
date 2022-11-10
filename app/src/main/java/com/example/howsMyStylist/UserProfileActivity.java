package com.example.howsMyStylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private TextInputLayout edit_firstName, edit_lastName, edit_email, edit_password,
                            edit_confirm_password, edit_phone, edit_address,
                            edit_city, edit_state, edit_zip, edit_country;
    private  TextView usernameLabel;
    private EditText edit_birthday;
    private DatePickerDialog picker;
    private ImageView profile_img;

    String _USERNAME, _EMAIL, _PHONE, _PWD;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Reference to xml
        edit_firstName = findViewById(R.id.input_firstName);
        edit_lastName = findViewById(R.id.input_lastName);
        edit_email = findViewById(R.id.input_email);
        edit_birthday = findViewById(R.id.edit_birthday);
        //set up DatePicker on EditText
        edit_birthday.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            //Date Picker Dialog
            picker = new DatePickerDialog(UserProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    edit_birthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }, year, month, day);
            picker.show();
        });
        edit_phone = findViewById(R.id.input_phone);
        edit_address = findViewById(R.id.input_address);
        edit_city = findViewById(R.id.input_city);
        edit_zip = findViewById(R.id.input_zip);
        edit_country = findViewById(R.id.input_country);
        edit_state = findViewById(R.id.input_state);
        usernameLabel = findViewById(R.id.usernameLabel);

        // Get data from firebase
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null){
            Toast.makeText(UserProfileActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
        } else {
            showUserProfile(firebaseUser);
        }

        // Set onClickListener for profile imageView to change picture
        profile_img = findViewById(R.id.profile_img);
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        //Extracting User Reference from Database for "Registered User"
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterActivity.ReadWriteUserDetails readUserDetails = snapshot.getValue(RegisterActivity.ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    _USERNAME = firebaseUser.getDisplayName();
                    _EMAIL = firebaseUser.getEmail();
                    _PHONE = readUserDetails.phone;


                    usernameLabel.setText(_USERNAME);
                    edit_email.getEditText().setText(_EMAIL);
                    edit_phone.getEditText().setText(_PHONE);

                    // Set User profile picture (After uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.with(UserProfileActivity.this).load(uri).into(profile_img);


                }else {
                    Toast.makeText(UserProfileActivity.this, "something went wrong! " +
                            "Show profile was canceled", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "something went wrong! " +
                        "Show profile was canceled", Toast.LENGTH_LONG).show();
            }
        });
    }
}
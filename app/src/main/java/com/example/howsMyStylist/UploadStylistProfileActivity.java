package com.example.howsMyStylist;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.howsMyStylist.Adapter.PopupStylistAdapter;
import com.example.howsMyStylist.Model.Salon;
import com.example.howsMyStylist.Model.Stylist;
import com.example.howsMyStylist.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UploadStylistProfileActivity extends AppCompatActivity {

    private TextInputLayout edit_firstName, edit_lastName, edit_phone, edit_email;
    AutoCompleteTextView edit_salon;
    private Button btn_createProfile, btn_cancelCreation, btn_choosePic, btn_uploadPic;

    private String _FIRSTNAME, _LASTNAME, _EMAIL, _PHONE, _GENDER, _SALON;
    private String stylistId;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private ImageView profile_img;
    FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    ActivityResultLauncher<String> imgFilePicker;
    private Uri uriImage;
    private Boolean isUploaded = false;
    private List<String> salonNameList;

    // For radio button
    private static final String other = "Other";
    private static final String male = "Male";
    private static final String female = "Female";
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioOption_male:
                if (checked)
                    _GENDER = male;
                break;
            case R.id.radioOption_female:
                if (checked)
                    _GENDER = female;
                break;
            case R.id.radioOption_other:
                if (checked)
                    _GENDER = other;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_stylist_profile);

        // Reference to xml
        edit_firstName = findViewById(R.id.input_stylistFirstName);
        edit_lastName = findViewById(R.id.input_stylistLastName);
        edit_phone = findViewById(R.id.input_stylistPhone);
        edit_email = findViewById(R.id.input_stylistEmail);
        edit_salon = findViewById(R.id.input_stylistSalonName);
        btn_createProfile = findViewById(R.id.btn_createStylistProfile);
        btn_cancelCreation = findViewById(R.id.btn_stylistCreationCancel);
        btn_choosePic = findViewById(R.id.btn_stylistChoosePic);
        btn_uploadPic = findViewById(R.id.btn_stylistUploadPic);

//        //edit_salon clicked ---> not work
//        edit_salon.setOnClickListener( v -> {
//            //set salon name from firebase
//            DatabaseReference salonDatabase = FirebaseDatabase.getInstance().getReference("Salon");
//            if (salonDatabase != null) {
//                salonDatabase.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()) {
//                            salonNameList = new ArrayList<>();
//                            for(DataSnapshot ds: snapshot.getChildren()) {
//                                salonNameList.add(ds.getValue(Stylist.class).getSalonName());
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(UploadStylistProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });


        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(
                UploadStylistProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, salonNameList);
        edit_salon.setAdapter(stateAdapter);
        edit_salon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UploadStylistProfileActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: TO CANCEL THE ACTIVITY AND BACK TO HOME_ACTIVITY
        btn_cancelCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadStylistProfileActivity.this, HomePageActivity.class));
                finish();
            }
        });

        // Get Firebase Instance
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Stylist");

        // TODO: TO CREATE STYLIST PROFILE
        btn_createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain the entered data
                _FIRSTNAME = edit_firstName.getEditText().getText().toString();
                _LASTNAME = edit_lastName.getEditText().getText().toString();
                _EMAIL = edit_phone.getEditText().getText().toString();
                _PHONE = edit_email.getEditText().getText().toString();
                _SALON = edit_salon.getText().toString();

                if (TextUtils.isEmpty(_FIRSTNAME)) {
                    Toast.makeText(UploadStylistProfileActivity.this,
                            "Please enter stylist first name", Toast.LENGTH_SHORT).show();
                    edit_firstName.setError("First Name is required.");
                    edit_firstName.requestFocus();
                } else if (TextUtils.isEmpty(_LASTNAME)) {
                    Toast.makeText(UploadStylistProfileActivity.this,
                            "Please enter stylist last name", Toast.LENGTH_SHORT).show();
                    edit_lastName.setError("Last Name is required.");
                    edit_lastName.requestFocus();
//                } else if (uriImage != null){
//                    if(!isUploaded) {
//                        Toast.makeText(UploadStylistProfileActivity.this,
//                                "Please click upload button to upload image first", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(UploadStylistProfileActivity.this,
//                                "Request send.", Toast.LENGTH_SHORT).show();
//                        createStylistProfile(_FIRSTNAME, _LASTNAME, _EMAIL, _PHONE, _GENDER, _SALON, uriImage);
//                        Toast.makeText(UploadStylistProfileActivity.this,
//                                "File created.", Toast.LENGTH_SHORT).show();
//                        //start HomePage activity
//                        startActivity(new Intent(UploadStylistProfileActivity.this, HomePageActivity.class));
//                        finish();
//                    }
                } else {
                    Toast.makeText(UploadStylistProfileActivity.this,
                            "Request send.", Toast.LENGTH_SHORT).show();
                    createStylistProfile(_FIRSTNAME, _LASTNAME, _EMAIL, _PHONE, _GENDER, _SALON, uriImage, 0);
                    //start HomePage activity
                    startActivity(new Intent(UploadStylistProfileActivity.this, HomePageActivity.class));
                    finish();
                }
            }
        });

        //TODO: TO UPLOAD A PICTURE
//        btn_choosePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UploadStylistProfileActivity.this, UploadUserProfilePicActivity.class));
//            }
//        });

        profile_img = findViewById(R.id.stylistProfile_img);
        btn_choosePic = findViewById(R.id.btn_stylistChoosePic);
        btn_uploadPic = findViewById(R.id.btn_stylistUploadPic);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("StylistPhotos");
        // Select a image
        btn_choosePic.setOnClickListener(v -> {
            imgFilePicker.launch("image/*");
        });
        imgFilePicker = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    uriImage = result;
                    Picasso.with(UploadStylistProfileActivity.this).load(uriImage).into(profile_img);
                });
        // Upload
        btn_uploadPic.setOnClickListener(v -> {
            uploadPic(uriImage);
        });
    }

    private void createStylistProfile(String firstname, String lastname, String phone, String email, String gender, String salonName, Uri uriImage, double avgRating) {
        stylistId = mFirebaseDatabase.push().getKey();
        Stylist newStylist = new Stylist(firstname, lastname, phone, email, gender, salonName, String.valueOf(uriImage), avgRating);
        mFirebaseDatabase.child(stylistId).setValue(newStylist);

//        addUserChangeListener();
    }

    private void uploadPic(Uri uriImage) {

        if (uriImage != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() +
                    "." + getFileExtension(uriImage));
            // Upload Image to Storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(UploadStylistProfileActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                            isUploaded = true;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadStylistProfileActivity.this, "Uploading Failed", Toast.LENGTH_SHORT);
                }
            });
        }else {
            Toast.makeText(UploadStylistProfileActivity.this, "No Picture was selected!", Toast.LENGTH_SHORT).show();
        }
    }

    // Obtain File Extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

//    private void addUserChangeListener() {
//        mFirebaseDatabase.child(stylistId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Stylist stylist = snapshot.getValue(Stylist.class);
//
//                if (stylist == null) {
//                    Log.e(TAG, "User data is null");
//                    return;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "Failed to read user.");
//            }
//        });
//    }
}
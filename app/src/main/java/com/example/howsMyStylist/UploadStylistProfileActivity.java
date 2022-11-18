package com.example.howsMyStylist;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentResolver;
import android.content.DialogInterface;
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
    private Button btn_createProfile, btn_cancelCreation, btn_choosePic;

    private String _FIRSTNAME, _LASTNAME, _EMAIL, _PHONE, _GENDER, _SALON, _PHOTO;
    private String stylistId;

    private DatabaseReference stylistDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private ImageView profile_img;
    private StorageReference storageReference;
    ActivityResultLauncher<String> imgFilePicker;
    private Uri uriImage;
    private List<String> salonNameList;

    // For radio button
    private static final String other = "Other";
    private static final String male = "Male";
    private static final String female = "Female";


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
        profile_img = findViewById(R.id.stylistProfile_img);

        // TODO: Edit Salon Name
        //edit_salon clicked - list existed salon name
        edit_salon.setOnClickListener( v -> {
            //set salon name from firebase
            DatabaseReference salonDatabase = FirebaseDatabase.getInstance().getReference("Salon");

                salonDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            salonNameList = new ArrayList<>();
                            Log.d("DBsnapshot", String.valueOf(snapshot.getChildrenCount()));
                            for(DataSnapshot ds: snapshot.getChildren()) {
                                salonNameList.add(ds.getValue(Salon.class).getSalonName());
                            }
                            Log.d("DBsnapshot", String.valueOf(salonNameList.size()));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UploadStylistProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            if (salonNameList != null) {
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(
                        UploadStylistProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, salonNameList);
                edit_salon.setAdapter(stateAdapter);
            }
        });

        edit_salon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UploadStylistProfileActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: TO CANCEL THE ACTIVITY AND BACK TO LAST STEP(POPUPSTYLISTACTIVITY)
        btn_cancelCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();// back to last step
            }
        });

        // Get Firebase Instance
        mFirebaseInstance = FirebaseDatabase.getInstance();
        stylistDatabase = mFirebaseInstance.getReference("Stylist");

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
                } else if (!_SALON.equals("") && !salonNameList.contains(_SALON)) { // salon not in DB -> ask user to create first
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadStylistProfileActivity.this);
                        builder.setMessage(R.string.dialog_createSalon_message)
                                .setTitle(R.string.dialog_createSalon_title)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        Intent intent = new Intent(UploadStylistProfileActivity.this, UploadSalonProfileActivity.class);
                                        intent.putExtra("stylistName", _SALON);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        AlertDialog dialogCreateSalon = builder.create();
                        dialogCreateSalon.show();
                } else {
                    stylistId = stylistDatabase.push().getKey();
                    uploadPic(uriImage);
                    Toast.makeText(UploadStylistProfileActivity.this,
                            "Request send.", Toast.LENGTH_SHORT).show();
                    createStylistProfile(_FIRSTNAME, _LASTNAME, _EMAIL, _PHONE, _GENDER, _SALON, _PHOTO, 0);
                    finish();
                }
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("StylistPhotos");
        // Select a image and set to imgView
        btn_choosePic.setOnClickListener(v -> {
            imgFilePicker.launch("image/*");
        });
        imgFilePicker = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    uriImage = result;
                    Picasso.with(UploadStylistProfileActivity.this).load(uriImage).into(profile_img);
                });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {  //-1: not selected; 1,2,3
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

    private void createStylistProfile(String firstname, String lastname, String phone, String email, String gender, String salonName, String stylistPhoto, double avgRating) {
//        stylistId = stylistDatabase.push().getKey();
        Stylist newStylist = new Stylist(firstname, lastname, phone, email, gender, salonName, stylistPhoto, avgRating);
        stylistDatabase.child(stylistId).setValue(newStylist);
    }

    private void uploadPic(Uri uriImage) {

        if (uriImage != null){
            StorageReference fileReference = storageReference.child(stylistId +
                    "." + getFileExtension(uriImage));
            _PHOTO = stylistId + "." + getFileExtension(uriImage);
            // Upload Image to Storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(UploadStylistProfileActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
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
            _PHOTO = "";
            Toast.makeText(UploadStylistProfileActivity.this, "No Picture was selected!", Toast.LENGTH_SHORT).show();
        }
    }

    // Obtain File Extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
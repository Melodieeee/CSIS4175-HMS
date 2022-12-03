package com.example.howsMyStylist;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.howsMyStylist.Model.Salon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadSalonProfileActivity extends AppCompatActivity {

    private TextInputLayout edit_phone, edit_address, edit_city, edit_zip, edit_webLink, edit_salonName;
    AutoCompleteTextView  edit_country, edit_state;
    private Button btn_createProfile, btn_cancelCreation, btn_choosePic;

    private String _NAME, _PHONE, _WEB, _ADDRESS, _CITY, _ZIP, _COUNTRY, _STATE, _PHOTO;
    private String salonId;

    private DatabaseReference firebaseDatabase;
    private FirebaseDatabase firebaseInstance;
    private StorageReference storageReference;

    private ImageView profile_img;
    ActivityResultLauncher<String> imgFilePicker;
    private Uri uriImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_salon_profile);

        // Reference to xml
        edit_salonName = findViewById(R.id.input_salonName);
        edit_phone = findViewById(R.id.input_salonPhone);
        edit_webLink = findViewById(R.id.input_salonWebLink);
        edit_address = findViewById(R.id.input_salonAddress);
        edit_city = findViewById(R.id.input_salonCity);
        edit_zip = findViewById(R.id.input_salonZip);

        //set salon name from UploadStylistProfile
        edit_salonName.getEditText().setText(getIntent().getStringExtra("stylistName"));

        // Set spinner for countries and states
        // Countries
        edit_country = findViewById(R.id.input_salonCountry);

        String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(
                UploadSalonProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, countries);

        edit_country.setAdapter(countryAdapter);
        edit_country.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(UploadSalonProfileActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show());
        // States (Canada)
        edit_state = findViewById(R.id.input_salonState);
        String[] states = new String[]{"Alberta", "British Columbia", "Manitoba", "New Brunswick",
                "Newfoundland and Labrador", "Northwest Territories", "Nova Scotia", "Nunavut",
                "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan", "Yukon"};
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(
                UploadSalonProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, states);

        edit_state.setAdapter(stateAdapter);
        edit_state.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(UploadSalonProfileActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show());


        // Get Firebase Instance
        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("Salon");

        // TODO: TO CREATE A SALON PROFILE BASED ON USER ENTERED
        btn_createProfile = findViewById(R.id.btn_salonCreateProfile);
        btn_createProfile.setOnClickListener(v -> {

            // Obtain the entered data
            _NAME = edit_salonName.getEditText().getText().toString();
            _PHONE = edit_phone.getEditText().getText().toString();
            _WEB = edit_webLink.getEditText().getText().toString();
            _ADDRESS = edit_address.getEditText().getText().toString();
            _CITY = edit_city.getEditText().getText().toString();
            _ZIP = edit_zip.getEditText().getText().toString();
            _STATE = edit_state.getText().toString();
            _COUNTRY = edit_country.getText().toString();

            if (TextUtils.isEmpty(_NAME)) {
                Toast.makeText(UploadSalonProfileActivity.this,
                        "Please enter salon name", Toast.LENGTH_SHORT).show();
                edit_salonName.setError("Salon Name is required.");
                edit_salonName.requestFocus();
            } else if (TextUtils.isEmpty(_ADDRESS)) {
                Toast.makeText(UploadSalonProfileActivity.this,
                        "Please enter salon address", Toast.LENGTH_SHORT).show();
                edit_address.setError("Address is required.");
                edit_address.requestFocus();
            } else if (TextUtils.isEmpty(_CITY)) {
                Toast.makeText(UploadSalonProfileActivity.this,
                        "Please enter salon located city", Toast.LENGTH_SHORT).show();
                edit_city.setError("City is required.");
                edit_city.requestFocus();
            } else if (TextUtils.isEmpty(_COUNTRY)) {
                Toast.makeText(UploadSalonProfileActivity.this,
                        "Please enter salon located country", Toast.LENGTH_SHORT).show();
                edit_country.setError("Country is required.");
                edit_country.requestFocus();
            } else if (TextUtils.isEmpty(_STATE)) {
                Toast.makeText(UploadSalonProfileActivity.this,
                        "Please enter salon located state", Toast.LENGTH_SHORT).show();
                edit_state.setError("State is required.");
                edit_state.requestFocus();
            } else {
                salonId = firebaseDatabase.push().getKey();
                uploadPic(uriImage);
                Toast.makeText(UploadSalonProfileActivity.this,
                        "Request send.", Toast.LENGTH_SHORT).show();
                createSalonProfile(_NAME, _PHONE, _WEB, _ADDRESS, _CITY, _ZIP, _STATE, _COUNTRY, _PHOTO, 0);
                finish();
            }

        });

        // TODO: A CANCEL ACTIVITY AND BACK TO HOME_ACTIVITY
        btn_cancelCreation = findViewById(R.id.btn_salonCancel);
        btn_cancelCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TODO: TO CHOOSE & UPLOAD A PHOTO
        profile_img = findViewById(R.id.salonProfile_img);
        btn_choosePic = findViewById(R.id.btn_salonChoosePic);

        storageReference = FirebaseStorage.getInstance().getReference("SalonPhotos");

        // Select a image
        btn_choosePic.setOnClickListener(v -> {
            imgFilePicker.launch("image/*");
        });
        imgFilePicker = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    uriImage = result;
                    Picasso.with(UploadSalonProfileActivity.this).load(uriImage).into(profile_img);
                });
    }

    private void uploadPic(Uri uriImage) {

        if (uriImage != null){
            StorageReference fileReference = storageReference.child(salonId +
                    "." + getFileExtension(uriImage));
            _PHOTO = salonId + "." + getFileExtension(uriImage);
            // Upload Image to Storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(UploadSalonProfileActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadSalonProfileActivity.this, "Uploading Failed", Toast.LENGTH_SHORT);
                }
            });
        }else {
            _PHOTO = "";
            //Toast.makeText(UploadSalonProfileActivity.this, "No Picture was selected!", Toast.LENGTH_SHORT).show();
        }
    }

    // Obtain File Extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void createSalonProfile(String name, String phone, String web, String address, String city, String zip, String state, String country, String salonPhoto, double avgRating) {
        Salon newSalon = new Salon(name, phone, address, country, state, city, zip, web, salonPhoto, avgRating);
        firebaseDatabase.child(salonId).setValue(newSalon);
    }

}
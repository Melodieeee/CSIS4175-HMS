package com.example.howsMyStylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howsMyStylist.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private TextInputLayout edit_firstName, edit_lastName, edit_email, edit_password,
                            edit_confirm_password, edit_phone, edit_address,
                            edit_city, edit_zip;
    AutoCompleteTextView edit_state, edit_country;
    private TextView usernameLabel;
    private EditText edit_birthday;
    private DatePickerDialog picker;
    private ImageView profile_img;
    Button btn_update;

    String _USERNAME, _EMAIL, _PHONE, _PWD, _DOB,
            _FIRSTNAME, _LASTNAME, _ADDRESS, _CITY, _STATE, _ZIP, _COUNTRY;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Reference to xml
        usernameLabel = findViewById(R.id.usernameLabel);
        edit_firstName = findViewById(R.id.input_firstName);
        edit_lastName = findViewById(R.id.input_lastName);
        edit_email = findViewById(R.id.input_email);
        edit_birthday = findViewById(R.id.edit_birthday);
        //set up DatePicker on EditText
        edit_birthday.setOnClickListener(v -> {
            // Extracting to dd,mm,yyyy by /
            String textSADoB[] = _DOB.split("/");
            int day = Integer.parseInt(textSADoB[0]);
            int month = Integer.parseInt(textSADoB[1]) - 1;
            int year = Integer.parseInt(textSADoB[2]);
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

        // Spinner for countries and states
        // Countries
        String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(
                UserProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, countries);
        edit_country.setAdapter(countryAdapter);
        edit_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UserProfileActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        // States (Canada)
        String[] states = new String[]{"Alberta", "British Columbia", "Manitoba", "New Brunswick",
                                       "Newfoundland and Labrador", "Northwest Territories", "Nova Scotia", "Nunavut",
                                       "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan", "Yukon"};
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(
                UserProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, states);
        edit_state.setAdapter(stateAdapter);
        edit_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UserProfileActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

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

        // Update data
        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        //Extracting User Reference from Database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterActivity.ReadWriteUserDetails readUserDetails = snapshot.getValue(RegisterActivity.ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    // Set data for each fields
                    _USERNAME = firebaseUser.getDisplayName();
                    _EMAIL = firebaseUser.getEmail();
                    _PHONE = readUserDetails.phone;
//                    _PWD = readUserDetails.pwd;
//      See if need to let users change their pwd in this page or set a btn and let them change in forgot password page???
                    _FIRSTNAME = readUserDetails.firstname;
                    _LASTNAME = readUserDetails.lastname;
                    _DOB = readUserDetails.birthday;
                    _ADDRESS = readUserDetails.address;
                    _CITY = readUserDetails.city;
                    _ZIP = readUserDetails.zip;
                    _COUNTRY = readUserDetails.country;
                    _STATE = readUserDetails.state;


                    usernameLabel.setText(_USERNAME);
                    edit_email.getEditText().setText(_EMAIL);
                    edit_phone.getEditText().setText(_PHONE);
//                    edit_password.getEditText().setText(_PWD);
                    edit_firstName.getEditText().setText(_FIRSTNAME);
                    edit_lastName.getEditText().setText(_LASTNAME);
                    edit_birthday.setText(_DOB);
                    edit_address.getEditText().setText(_ADDRESS);
                    edit_city.getEditText().setText(_CITY);
                    edit_zip.getEditText().setText(_ZIP);
                    edit_country.setText(_COUNTRY);
                    edit_state.setText(_STATE);

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

    private void updateProfile(FirebaseUser firebaseUser) {

        // Obtain the entered data
        _USERNAME = usernameLabel.getText().toString();
        _FIRSTNAME = edit_firstName.getEditText().getText().toString();
        _LASTNAME = edit_lastName.getEditText().getText().toString();
        _DOB = edit_birthday.getText().toString();
        _PHONE = edit_phone.getEditText().getText().toString();
        _ADDRESS = edit_address.getEditText().getText().toString();
        _CITY = edit_city.getEditText().getText().toString();
        _ZIP = edit_zip.getEditText().getText().toString();
        _STATE = edit_state.getText().toString();
        _COUNTRY = edit_country.getText().toString();

        RegisterActivity.ReadWriteUserDetails writeUserDetails =
                new RegisterActivity.ReadWriteUserDetails(_FIRSTNAME, _LASTNAME, _DOB, _PHONE, _ADDRESS, _CITY, _STATE, _ZIP, _COUNTRY);

        //Extracting User Reference from Database for "User"
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        String userId = firebaseUser.getUid();
        reference.child(userId).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // Setting new fields
                    UserProfileChangeRequest updatableProfileField = new UserProfileChangeRequest.Builder().setDisplayName(_USERNAME).build();
                    firebaseUser.updateProfile(updatableProfileField);

                    Toast.makeText(UserProfileActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e){
                        Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
}
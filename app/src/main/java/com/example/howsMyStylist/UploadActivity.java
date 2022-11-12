package com.example.howsMyStylist;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.cottacush.android.currencyedittext.CurrencyInputWatcher;
import com.example.howsMyStylist.Model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UploadActivity extends AppCompatActivity {

    private CurrencyEditText currencyEditText;
    private EditText edit_stylistName, edit_salonName, edit_serviceName,
                        edit_serviceDate, edit_review;
    private RatingBar ratingBar;
    private ImageView imgView_photoAdded;
    private Button btn_addPhoto, btn_submit;

    private DatePickerDialog picker;
    private String date;

    private FirebaseAuth auth;
    private StorageReference storageReference;
    private DatabaseReference firebaseDatabase;
    private FirebaseDatabase firebaseInstance;
    private FirebaseUser firebaseUser;
    ActivityResultLauncher<String> imgFilePicker;
    private Uri uriUploadImg;

    private String reviewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        edit_stylistName = findViewById(R.id.input_stylistName);
        edit_salonName = findViewById(R.id.input_salonName);
        edit_serviceName = findViewById(R.id.input_serviceName);
        edit_review = findViewById(R.id.input_review);
        ratingBar = findViewById(R.id.ratingBar);

        //price formatter
        currencyEditText = findViewById(R.id.input_price);

        //Date picker
        edit_serviceDate = findViewById(R.id.input_serviceDate);
        edit_serviceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extracting to dd,mm,yyyy by /
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(UploadActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edit_serviceDate.setText(date);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //add photo
        imgView_photoAdded = findViewById(R.id.imgView_photoAdded);
        btn_addPhoto = findViewById(R.id.btnAddPhoto);
        btn_addPhoto.setOnClickListener(v -> {
            imgFilePicker.launch("image/*");
            imgView_photoAdded.setVisibility(View.VISIBLE);
            btn_addPhoto.setText("Choose others");
        });

        imgFilePicker = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        uriUploadImg = result;
                        //imgView_photoAdded.setImageURI(uriUploadImg);
                        Picasso.with(UploadActivity.this).load(uriUploadImg).into(imgView_photoAdded);
                    }
                });

        //click the img to delete it
        imgView_photoAdded.setOnClickListener(v->{
            uriUploadImg = null;
            imgView_photoAdded.setImageURI(uriUploadImg);
            imgView_photoAdded.setVisibility(View.GONE);
            btn_addPhoto.setText("Add Photo");
            Toast.makeText(UploadActivity.this, "Photo deleted", Toast.LENGTH_SHORT).show();
        });

        //submit
        btn_submit = findViewById(R.id.btnSubmitReview);
        btn_submit.setOnClickListener(v -> {
            String stylistName = edit_stylistName.getText().toString();
            String salonName = edit_salonName.getText().toString();
            String serviceName = edit_serviceName.getText().toString();
            Double price = currencyEditText.getNumericValue();
            //date: we have already assigned when picking the date from the calendar
            String review = edit_review.getText().toString();
            double rating =  ratingBar.getRating();

            //check all required info
            if (TextUtils.isEmpty(stylistName)) {
                Toast.makeText(UploadActivity.this,
                        "Please enter your stylist.", Toast.LENGTH_SHORT).show();
                edit_stylistName.setError("Stylist's name is required.");
                edit_stylistName.requestFocus();
            } else if (TextUtils.isEmpty(serviceName)) {
                Toast.makeText(UploadActivity.this,
                        "Please enter the service name.", Toast.LENGTH_SHORT).show();
                edit_serviceName.setError("Service is required.");
                edit_serviceName.requestFocus();
            } else if (TextUtils.isEmpty(date)) { //?
                Toast.makeText(UploadActivity.this,
                        "Please select the date.", Toast.LENGTH_SHORT).show();
                edit_serviceName.setError("Date is required.");
                edit_serviceName.requestFocus();
            } else if (TextUtils.isEmpty(price.toString())) {
                Toast.makeText(UploadActivity.this,
                        "Please enter the price.", Toast.LENGTH_SHORT).show();
                currencyEditText.setError("Price is required.");
                currencyEditText.requestFocus();
            } else if (rating == 0) {
                Toast.makeText(UploadActivity.this,
                        "Please rate your stylist.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UploadActivity.this,
                        "Request send.", Toast.LENGTH_SHORT).show();
                uploadReview(stylistName, salonName, serviceName, price, date,
                                review, rating, uriUploadImg);
            }
        });
    }

    private void uploadReview(String stylistName, String salonName, String serviceName, double price, String date,
                              String review, double rating, Uri uriUploadImg) {

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        Review newReview = new Review(stylistName, serviceName, price, date, rating, firebaseUser.getUid());
        //salon
        if (salonName != null) {
            newReview.setSalonName(salonName);
        }
        //review
        if (review != null) {
            newReview.setReview(review);
        }
        //uploadImg
        if (uriUploadImg != null) {
            newReview.setUriImage(uriUploadImg);
            uploadToFirebase(uriUploadImg);

        }

        firebaseInstance = FirebaseDatabase.getInstance(); //root
        firebaseDatabase = firebaseInstance.getReference("Review");  //Review -> reviewId -> newReview
        if(TextUtils.isEmpty(reviewId)) {
            reviewId = firebaseDatabase.push().getKey();
        }

        firebaseDatabase.child(reviewId).setValue(newReview).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    // update to user's reviewList
                    firebaseDatabase = firebaseInstance.getReference("User");
                    firebaseDatabase.child(firebaseUser.getUid()).child("reviewIdList").child(reviewId).setValue("stylist id?");

                    Toast.makeText(UploadActivity.this, "Review posted successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UploadActivity.this, HomePageActivity.class);
                    // Prevent user back
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UploadActivity.this, "Review posted failed! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadToFirebase(Uri uriUploadImg){
        storageReference = FirebaseStorage.getInstance().getReference("UploadedPhotos");
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() +"." + getFileExtension(uriUploadImg));
        fileReference.putFile(uriUploadImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(UploadActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Uploading Failed", Toast.LENGTH_SHORT);
            }
        });
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

}
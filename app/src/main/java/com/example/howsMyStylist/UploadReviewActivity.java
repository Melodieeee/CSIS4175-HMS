package com.example.howsMyStylist;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.howsMyStylist.Adapter.ImagesAdapter;
import com.example.howsMyStylist.Model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UploadReviewActivity extends AppCompatActivity {

    private CurrencyEditText currencyEditText;
    private EditText edit_stylistName, edit_salonName, edit_serviceName,
                        edit_serviceDate, edit_review;
    private RatingBar ratingBar;
    private ViewPager viewPager;
    private final int REQUEST_PERMISSION_CODE = 35;
    private Button btn_addPhoto, btn_submit;

    private DatePickerDialog picker;
    private String date;

    private FirebaseAuth auth;
    private StorageReference storageReference;
    private DatabaseReference firebaseDatabase;
    private FirebaseDatabase firebaseInstance;
    private FirebaseUser firebaseUser;
    private ActivityResultLauncher<String> imgFilePicker;
    private List<Uri> uriUploadImgs;
    private ProgressBar progressBar;
    private int uploadImgCount = 0;
    private byte[] imageBytes;

    private String reviewId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_review);

        //onEditListener and PopupWindow
        edit_stylistName = findViewById(R.id.input_stylistName);
        edit_salonName = findViewById(R.id.input_salonName);
        edit_stylistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PopupStylistActivity.class);
                startActivity(intent);
            }
        });

        edit_serviceName = findViewById(R.id.input_serviceName);
        edit_review = findViewById(R.id.input_review);
        ratingBar = findViewById(R.id.ratingBar);

        //getString from popup activity
        Intent intent = getIntent();
        edit_stylistName.setText(intent.getStringExtra("stylistName"));
        edit_salonName.setText(intent.getStringExtra("salonName"));

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
                picker = new DatePickerDialog(UploadReviewActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edit_serviceDate.setText(date);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //add images
        viewPager = findViewById(R.id.viewPager);
        btn_addPhoto = findViewById(R.id.btnAddImages);
        progressBar = new ProgressBar(this);
        uriUploadImgs = new ArrayList<>();

        btn_addPhoto.setOnClickListener(v -> {
            checkUserPermission();
            if (uriUploadImgs == null) {
                btn_addPhoto.setText("Add Images");
                viewPager.setVisibility(View.GONE);
            } else {
                btn_addPhoto.setText("Choose others");
                viewPager.setVisibility(View.VISIBLE);
            }
        });

        imgFilePicker = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback<List<Uri>>() {
            @Override
            public void onActivityResult(List<Uri> result) {
                uriUploadImgs = result;
                setAdapter();
            }
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
                Toast.makeText(UploadReviewActivity.this,
                        "Please enter your stylist.", Toast.LENGTH_SHORT).show();
                edit_stylistName.setError("Stylist's name is required.");
                edit_stylistName.requestFocus();
            } else if (TextUtils.isEmpty(serviceName)) {
                Toast.makeText(UploadReviewActivity.this,
                        "Please enter the service name.", Toast.LENGTH_SHORT).show();
                edit_serviceName.setError("Service is required.");
                edit_serviceName.requestFocus();
            } else if (TextUtils.isEmpty(date)) {
                Toast.makeText(UploadReviewActivity.this,
                        "Please select the date.", Toast.LENGTH_SHORT).show();
                edit_serviceName.setError("Date is required.");
                edit_serviceName.requestFocus();
            } else if (TextUtils.isEmpty(price.toString())) {
                Toast.makeText(UploadReviewActivity.this,
                        "Please enter the price.", Toast.LENGTH_SHORT).show();
                currencyEditText.setError("Price is required.");
                currencyEditText.requestFocus();
            } else if (rating == 0) {
                Toast.makeText(UploadReviewActivity.this,
                        "Please rate your stylist.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UploadReviewActivity.this,
                        "Request send.", Toast.LENGTH_SHORT).show();
                uploadReview(stylistName, salonName, serviceName, price, date,
                                review, rating, uriUploadImgs);
            }
        });
    }

    private void checkUserPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        } else {
            pickImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImages();
            } else {
                Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pickImages() {
        imgFilePicker.launch("image/*");
        if (uriUploadImgs != null) {
            uriUploadImgs.clear();
        }
    }

    private void setAdapter() {
        ImagesAdapter imagesAdapter = new ImagesAdapter(this, uriUploadImgs);
        viewPager.setAdapter(imagesAdapter);
    }

    private void compressImages(List<Uri> uriUploadImgs) {
        for (int i = 0; i < uriUploadImgs.size(); i++) {
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriUploadImgs.get(i));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                imageBytes = stream.toByteArray();
                uploadImagesToFireStorage(imageBytes, uriUploadImgs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadReview(String stylistName, String salonName, String serviceName, double price, String date,
                              String review, double rating, List<Uri> uriUploadImgs) {

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        Review newReview = new Review(stylistName, serviceName, price, date, rating, firebaseUser.getUid());

        if (salonName != null) {
            newReview.setSalonName(salonName);
        }
        if (review != null) {
            newReview.setReview(review);
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
                    //uploadImg to be children of review
                    if (uriUploadImgs != null) {
                        compressImages(uriUploadImgs);
                        for (Uri u: uriUploadImgs) {
                            firebaseDatabase.child(reviewId).child("images").setValue(String.valueOf(u));
                        }
                    }
                    // update the review to user's reviewList
                    firebaseDatabase = firebaseInstance.getReference("User");
                    firebaseDatabase.child(firebaseUser.getUid()).child("reviewIdList").child(reviewId).setValue("stylist id?");

                    Toast.makeText(UploadReviewActivity.this, "Review posted successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UploadReviewActivity.this, HomePageActivity.class);
                    // Prevent user back
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UploadReviewActivity.this, "Review posted failed! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadImagesToFireStorage(byte[] imageBytes, List<Uri> uriUploadImgs) {
        storageReference = FirebaseStorage.getInstance().getReference()
                .child("ReviewsPhotos");
        StorageReference fileReference = storageReference
                .child("images" + System.currentTimeMillis() + ".jpg");
        fileReference.putBytes(imageBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadImgCount += 1;
                if(uploadImgCount == uriUploadImgs.size()) {
                    Log.d("upload", "uploaded done");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UploadReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

}
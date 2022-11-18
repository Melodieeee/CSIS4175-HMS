package com.example.howsMyStylist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadUserProfilePicActivity extends AppCompatActivity {

    private ImageView imageViewUploadPic;
    FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_user_profile_pic);

        Button btnUploadPicChoose = findViewById(R.id.btn_choosePic);
        Button btnUploadPic = findViewById(R.id.btn_upload);
        imageViewUploadPic = findViewById(R.id.img_display_preview);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("UserPics");

        Uri uri = firebaseUser.getPhotoUrl();

        // Set User's current preview image in ImageView (if uploaded already)
        // Regular URIs.
//        Picasso.with(UploadUserProfilePicActivity.this).load(uri).into(imageViewUploadPic);
        Glide.with(UploadUserProfilePicActivity.this).load(uri).into(imageViewUploadPic);
        // Choose image to upload
        btnUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Upload image
        btnUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPic();
            }
        });

    }

    private void uploadPic() {
        if (uriImage != null){
            // Save the image with uid of the currently logged user
            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid() +
                    "." + getFileExtension(uriImage));

            // Upload Image to Storage
            fileReference.delete();
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            //Set the display image of the user after uploaded
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                    .Builder().setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });

                    Toast.makeText(UploadUserProfilePicActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadUserProfilePicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UploadUserProfilePicActivity.this, "No Picture was selected!", Toast.LENGTH_SHORT).show();
        }
    }

    // Obtain File Extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            imageViewUploadPic.setImageURI(uriImage);

        }
    }
}
package com.example.howsMyStylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.howsMyStylist.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_email, edit_password;
    private Button btn_login, btn_register, btn_forgotPassword;
    private FirebaseAuth auth;
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.edit_loginEmail);
        edit_password = findViewById(R.id.edit_loginPassword);
        btn_login = findViewById(R.id.btn_login);

        // Register page
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Register page clicked", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

        });

        // Reset password
        btn_forgotPassword = findViewById(R.id.btn_forgotPassword);
        btn_forgotPassword.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "You can reset your password now!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // Auth
        auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(v -> {
            String text_loginEmail = edit_email.getText().toString();
            String text_loginPassword = edit_password.getText().toString();
            if (TextUtils.isEmpty(text_loginEmail)){
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                edit_email.setError("Email is required.");
                edit_email.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(text_loginEmail).matches()) {
                Toast.makeText(LoginActivity.this,
                        "Please enter your email.", Toast.LENGTH_SHORT).show();
                edit_email.setError("Valid email is required.");
                edit_email.requestFocus();
            } else if (TextUtils.isEmpty(text_loginPassword)) {
                Toast.makeText(LoginActivity.this,
                        "Please enter your password.", Toast.LENGTH_SHORT).show();
                edit_password.setError("Password is required.");
                edit_password.requestFocus();
            } else {
                loginUser(text_loginEmail,text_loginPassword);
            }
        });

    }

    private void loginUser(String loginEmail, String loginPassword) {

        auth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // Get instance of current user
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    // Check if the user is verified before user can access their profile
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                        // Open Home page
                        startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        auth.signOut();
                        showAlertDialog();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        edit_email.setError("User is not existed. Please re-enter the email or register.");
                        edit_email.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        edit_password.setError("Invalid password. Please check and re-enter.");
                        edit_password.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showAlertDialog() {
        //Set up alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme);
        builder.setTitle("You account is not verified!");
        builder.setMessage("Please verify your email now. You cannot login without email verification.");
        //Open email app if "continue" clicked
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //open in a new window
                startActivity(intent);
            }
        });
        //Create AlertDialog
        AlertDialog alertDialog = builder.create();
        //Show AlertDialog
        alertDialog.show();
    }

    //check if user is logg in now. -> take to ???Activity
    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this, "Already logged in!", Toast.LENGTH_SHORT).show();
            //start HomePage activity
            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "You can log in now!", Toast.LENGTH_SHORT).show();

        }
    }

}
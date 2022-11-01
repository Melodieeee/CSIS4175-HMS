package com.example.howsMyStylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText text_username, text_email, text_phone,
                     text_password, text_confirm_password;
    private Button btn_createAccount, btn_login;
    private CheckBox checkBoxAgree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        text_username = findViewById(R.id.edit_username);
        text_email = findViewById(R.id.edit_email);
        text_phone = findViewById(R.id.edit_phone);
        text_password = findViewById(R.id.edit_password);
        text_confirm_password = findViewById(R.id.edit_password_confirm);
        checkBoxAgree = findViewById(R.id.checkbox_agree);

        // Back to login
        btn_login = findViewById(R.id.btn_registerLogin);
        btn_login.setOnClickListener(v -> {
            Toast.makeText(RegisterActivity.this, "Join us today", Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        });

        // Create Account
        btn_createAccount = findViewById(R.id.btn_createAccount);
        btn_createAccount.setOnClickListener(v -> {
            // Obtain the entered data
            String username = text_username.getText().toString();
            String email = text_email.getText().toString();
            String phone = text_phone.getText().toString();
            String pwd = text_password.getText().toString();
            String confirm_pwd = text_confirm_password.getText().toString();

            if (TextUtils.isEmpty(username)){
                Toast.makeText(RegisterActivity.this,
                        "Please enter your username.", Toast.LENGTH_SHORT).show();
                text_username.setError("Username is required.");
                text_username.requestFocus();
            }  else if (TextUtils.isEmpty(email)){
                Toast.makeText(RegisterActivity.this,
                        "Please enter your email.", Toast.LENGTH_SHORT).show();
                text_email.setError("Email is required.");
                text_email.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(RegisterActivity.this,
                        "Please enter your email.", Toast.LENGTH_SHORT).show();
                text_email.setError("Valid email is required.");
                text_email.requestFocus();
            } else if (TextUtils.isEmpty(phone)){
                Toast.makeText(RegisterActivity.this,
                        "Please enter your phone number.", Toast.LENGTH_SHORT).show();
                text_phone.setError("Phone number is required.");
                text_phone.requestFocus();
            } else if (phone.length() != 10){
                Toast.makeText(RegisterActivity.this,
                        "Please re-enter enter your phone number.", Toast.LENGTH_SHORT).show();
                text_phone.setError("Phone number should be 10 digits.");
                text_phone.requestFocus();
            } else if (TextUtils.isEmpty(pwd)){
                Toast.makeText(RegisterActivity.this,
                        "Please enter your password.", Toast.LENGTH_SHORT).show();
                text_password.setError("Password is required.");
                text_password.requestFocus();
            } else if (pwd.length() < 6){
                Toast.makeText(RegisterActivity.this,
                        "Password should be at least 6 digits.", Toast.LENGTH_SHORT).show();
                text_password.setError("Password too weak.");
                text_password.requestFocus();
            }  else if (TextUtils.isEmpty(confirm_pwd)){
                Toast.makeText(RegisterActivity.this,
                        "Please enter your password again.", Toast.LENGTH_SHORT).show();
                text_confirm_password.setError("Password confirmation is required.");
                text_confirm_password.requestFocus();
            } else if (!pwd.equals(confirm_pwd)){
                Toast.makeText(RegisterActivity.this,
                        "Please enter same password.", Toast.LENGTH_SHORT).show();
                text_confirm_password.setError("Password confirmation is required.");
                text_confirm_password.requestFocus();
                // Clean the entered passwords
                text_password.clearComposingText();
                text_confirm_password.clearComposingText();
            } else if (!checkBoxAgree.isChecked()){
                Toast.makeText(RegisterActivity.this,
                        "Please confirm term and policy.", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(RegisterActivity.this,
                        "Request send.", Toast.LENGTH_SHORT).show();
                registerUser(username, email, phone, pwd, confirm_pwd);
            }
        });
    }

    private void registerUser(String username, String email, String phone, String pwd, String confirm_pwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    // Send Verification Email
                    firebaseUser.sendEmailVerification();

//                    // Open User Profile after successful registration
//                    Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
//                    // Prevent user back to Register activity
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();

                }
            }
        });
    }
}
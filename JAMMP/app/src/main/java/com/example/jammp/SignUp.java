package com.example.jammp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etDob, etContact;
    private Button btnSignup;
    private TextView tvLoginLink;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Initialize Firebase Auth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        etUsername = findViewById(R.id.username);  // Change from fullname to username in layout
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.confirm_password);
        etDob = findViewById(R.id.dob);
        etContact = findViewById(R.id.contact);
        btnSignup = findViewById(R.id.signup_button);
        tvLoginLink = findViewById(R.id.login_link);

        // Set up button click listeners
        btnSignup.setOnClickListener(view -> checkUsername());
        tvLoginLink.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
        });
    }

    private void checkUsername() {
        String username = etUsername.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            return;
        }

        // Check if username contains special characters or spaces
        if (!username.matches("^[a-zA-Z0-9_]*$")) {
            etUsername.setError("Username can only contain letters, numbers, and underscore");
            return;
        }

        // Query the database to check if username exists
        mDatabase.child("users").orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            etUsername.setError("Username already exists");
                        } else {
                            // Username is available, proceed with registration
                            registerUser(username);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SignUp.this, "Database Error: " +
                                databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void registerUser(String username) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String usertype = "user";  // Default usertype

        // Validate remaining user input
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }
        if (TextUtils.isEmpty(dob)) {
            etDob.setError("Date of Birth is required");
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            etContact.setError("Contact number is required");
            return;
        }

        // Show progress dialog
        // You might want to add a ProgressDialog here

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the user ID
                        String userId = mAuth.getCurrentUser().getUid();

                        // Create a User object with username
                        User user = new User(username, email, dob, contact, usertype);

                        // Store user data in Firebase Realtime Database
                        mDatabase.child("users").child(userId).setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    // Create a separate node for username lookup
                                    mDatabase.child("usernames").child(username).setValue(userId)
                                            .addOnSuccessListener(aVoid2 -> {
                                                Toast.makeText(SignUp.this,
                                                        "Registration Successful!",
                                                        Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUp.this, SignIn.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(SignUp.this,
                                                        "Failed to register username: " + e.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(SignUp.this,
                                            "Failed to save user data: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });
                    } else {
                        Toast.makeText(SignUp.this, "Registration Failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // User class with modified fields (username instead of name)
    public static class User {
        public String username;
        public String email;
        public String dob;
        public String contact;
        public String usertype;

        // Default constructor required for Firebase
        public User() {
        }

        public User(String username, String email, String dob, String contact, String usertype) {
            this.username = username;
            this.email = email;
            this.dob = dob;
            this.contact = contact;
            this.usertype = usertype;
        }
    }
}
package com.example.jammp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddMember extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etDob, etContact, etUserType;
    private Button btnSignup;
    private TextView tvLoginLink;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);

        // Initialize Firebase Auth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        etUsername = findViewById(R.id.username);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etDob = findViewById(R.id.dob);
        etContact = findViewById(R.id.contact);
        etUserType = findViewById(R.id.UserType);  // New user type field
        btnSignup = findViewById(R.id.signup_button);
        tvLoginLink = findViewById(R.id.login_link);
        // Set up button click listeners
        btnSignup.setOnClickListener(view -> checkUsername());
        tvLoginLink.setOnClickListener(view -> {
            startActivity(new Intent(AddMember.this, SignIn.class));
        });
        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(AddMember.this, AdminPage.class);
            startActivity(intent);
            finish();
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
                        Toast.makeText(AddMember.this, "Database Error: " +
                                databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void registerUser(String username) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String usertype = etUserType.getText().toString().trim();

        // Validate remaining user input
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
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
        if (TextUtils.isEmpty(usertype)) {
            etUserType.setError("User Type is required");
            return;
        }

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the user ID
                        String userId = mAuth.getCurrentUser().getUid();

                        // Create a User object with username and user type
                        User user = new User(username, email, dob, contact, usertype);

                        // Store user data in Firebase Realtime Database
                        mDatabase.child("users").child(userId).setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    // Create a separate node for username lookup
                                    mDatabase.child("usernames").child(username).setValue(userId)
                                            .addOnSuccessListener(aVoid2 -> {
                                                Toast.makeText(AddMember.this,
                                                        "Registration Successful!",
                                                        Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AddMember.this, SignIn.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(AddMember.this,
                                                        "Failed to register username: " + e.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AddMember.this,
                                            "Failed to save user data: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });
                    } else {
                        Toast.makeText(AddMember.this, "Registration Failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // User class with modified fields (username and user type)
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

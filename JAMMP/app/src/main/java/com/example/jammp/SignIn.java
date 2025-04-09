package com.example.jammp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.widget.LinearLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private EditText etLoginField, etPassword;
    private Button btnLogin;
    private TextView tvSignupLink, tvForgotPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        etLoginField = findViewById(R.id.login_field);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login_button);
        tvSignupLink = findViewById(R.id.signup_link);
        tvForgotPassword = findViewById(R.id.forgot_password_link);

        // Set up button click listeners
        btnLogin.setOnClickListener(view -> validateAndLogin());
        tvSignupLink.setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
        });
        tvForgotPassword.setOnClickListener(view -> showForgotPasswordDialog());
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        // Set up the input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Enter your email");
        layout.addView(emailInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Reset", (dialog, which) -> {
            String email = emailInput.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignIn.this,
                        "Please enter your email",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(SignIn.this,
                        "Please enter a valid email",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Send password reset email
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignIn.this,
                                    "Password reset email sent! Check your email inbox.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignIn.this,
                                    "Failed to send reset email: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validateAndLogin() {
        String loginField = etLoginField.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(loginField)) {
            etLoginField.setError("Email or username is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        // Check if input is email or username
        if (Patterns.EMAIL_ADDRESS.matcher(loginField).matches()) {
            loginWithEmail(loginField, password);
        } else {
            loginWithUsername(loginField, password);
        }
    }

    private void loginWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        onLoginSuccess();
                    } else {
                        Toast.makeText(SignIn.this,
                                "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loginWithUsername(String username, String password) {
        mDatabase.child("users")
                .orderByChild("username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                            User user = userSnapshot.getValue(User.class);

                            if (user != null && user.email != null) {
                                loginWithEmail(user.email, password);
                            } else {
                                Toast.makeText(SignIn.this,
                                        "Error retrieving user data",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SignIn.this,
                                    "No user found with this username",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SignIn.this,
                                "Database Error: " + databaseError.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void onLoginSuccess() {
        Toast.makeText(SignIn.this, "Login Successful", Toast.LENGTH_SHORT).show();

        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            // Check usertype and redirect accordingly
                            if ("admin".equals(user.usertype)) {
                                startActivity(new Intent(SignIn.this, AdminPage.class));
                            } else {
                                startActivity(new Intent(SignIn.this, MainActivity.class));
                            }
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SignIn.this,
                                "Error loading user data: " + databaseError.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    public static class User {
        public String username;
        public String email;
        public String dob;
        public String contact;
        public String usertype;

        public User() {
            // Required empty constructor for Firebase
        }
    }
}
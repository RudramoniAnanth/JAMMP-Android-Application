package com.example.jammp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class DocReg extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView docImageView;
    private EditText etDocName, etTitle, etWorkplace, etNumber, etLocation;
    private Button btnSubmit;
    private ImageView backArrow;
    private DatabaseReference databaseReference;
    private String base64Image; // Store the Base64 image string
    private ProgressDialog progressDialog; // Progress dialog for loading indicator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docreg);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Doctors");

        // Initialize UI elements
        docImageView = findViewById(R.id.docImageView);
        etDocName = findViewById(R.id.etDocName);
        etTitle = findViewById(R.id.etTitle);
        etWorkplace = findViewById(R.id.etWorkplace);
        etNumber = findViewById(R.id.etNumber); // Contact number EditText
        etLocation = findViewById(R.id.etLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        backArrow = findViewById(R.id.back_arrow);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading profile...");
        progressDialog.setCancelable(false);

        // Back arrow click to return to AdminPage
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(DocReg.this, AdminPage.class);
            startActivity(intent);
            finish();
        });

        // Image selection from gallery
        docImageView.setOnClickListener(v -> openImagePicker());

        // Submit button click to upload profile
        btnSubmit.setOnClickListener(v -> uploadProfile());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // Get the image as a Bitmap
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                // Convert Bitmap to Base64 string
                base64Image = encodeImageToBase64(bitmap);

                // Set the selected image in ImageView
                docImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void uploadProfile() {
        String docName = etDocName.getText().toString().trim();
        String title = etTitle.getText().toString().trim();
        String workplace = etWorkplace.getText().toString().trim();
        String contactNumber = etNumber.getText().toString().trim(); // Get contact number
        String location = etLocation.getText().toString().trim();

        // Check if all fields and image are selected
        if (!docName.isEmpty() && !title.isEmpty() && !workplace.isEmpty() && !contactNumber.isEmpty() && !location.isEmpty() && base64Image != null) {
            // Show the progress dialog
            progressDialog.show();

            saveDoctorProfile(docName, title, workplace, contactNumber, location, base64Image);
        } else {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDoctorProfile(String name, String title, String workplace, String contactNumber, String location, String base64Image) {
        String doctorId = databaseReference.push().getKey();
        DoctorProfile doctorProfile = new DoctorProfile(name, title, workplace, contactNumber, location, base64Image);

        if (doctorId != null) {
            databaseReference.child(doctorId).setValue(doctorProfile)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss(); // Dismiss the progress dialog
                        Toast.makeText(DocReg.this, "Profile uploaded successfully!", Toast.LENGTH_SHORT).show();
                        // Redirect to AdminPage
                        startActivity(new Intent(DocReg.this, AdminPage.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss(); // Dismiss the progress dialog
                        Toast.makeText(DocReg.this, "Profile upload failed!", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Inner class for the DoctorProfile data model
    public static class DoctorProfile {
        public String name, title, workplace, contactNumber, location, imageBase64;

        public DoctorProfile() {
            // Default constructor required for calls to DataSnapshot.getValue(DoctorProfile.class)
        }

        public DoctorProfile(String name, String title, String workplace, String contactNumber, String location, String imageBase64) {
            this.name = name;
            this.title = title;
            this.workplace = workplace;
            this.contactNumber = contactNumber; // Initialize contact number
            this.location = location;
            this.imageBase64 = imageBase64;
        }
    }
}

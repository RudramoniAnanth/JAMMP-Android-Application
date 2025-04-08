package com.example.jammp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookAppointment extends AppCompatActivity {
    private static final int REQUEST_CALL_PERMISSION = 100;

    private ImageView backArrow;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private DatabaseReference databaseReference;
    private List<DoctorProfile> doctorList;
    private DoctorAdapter doctorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_appointment);

        // Initialize UI components
        backArrow = findViewById(R.id.back_arrow);
        recyclerView = findViewById(R.id.news_recycler_view);
        searchView = findViewById(R.id.search_view);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Doctors");

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(doctorList);
        recyclerView.setAdapter(doctorAdapter);

        // Fetch doctor data from Firebase
        fetchDoctorsFromFirebase();

        // Set up back arrow functionality
        backArrow.setOnClickListener(v -> finish());

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterDoctors(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDoctors(newText);
                return false;
            }
        });
    }

    private void fetchDoctorsFromFirebase() {
        LinearProgressIndicator progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctorList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DoctorProfile doctor = dataSnapshot.getValue(DoctorProfile.class);
                    if (doctor != null) {
                        doctorList.add(doctor);
                    }
                }
                // Sort doctorList alphabetically by doctor name
                Collections.sort(doctorList, new Comparator<DoctorProfile>() {
                    @Override
                    public int compare(DoctorProfile d1, DoctorProfile d2) {
                        return d1.getName().compareToIgnoreCase(d2.getName());
                    }
                });
                doctorAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookAppointment.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterDoctors(String query) {
        List<DoctorProfile> filteredList = new ArrayList<>();
        for (DoctorProfile doctor : doctorList) {
            if (doctor.getName().toLowerCase().contains(query.toLowerCase()) ||
                    doctor.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    doctor.getWorkplace().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(doctor);
            }
        }
        doctorAdapter.updateList(filteredList);
    }

    private void initiatePhoneCall(String contactNumber) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactNumber)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted. Tap again to make the call.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied. Cannot make a call.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // DoctorAdapter as an inner class
    private class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
        private List<DoctorProfile> doctorList;

        public DoctorAdapter(List<DoctorProfile> doctorList) {
            this.doctorList = doctorList;
        }

        @NonNull
        @Override
        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the separate doc_item_layout for each RecyclerView item
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_item_layout, parent, false);
            return new DoctorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
            DoctorProfile doctor = doctorList.get(position);
            holder.docName.setText("NAME : Dr. " + doctor.getName());
            holder.docTitle.setText("Speciality : " +doctor.getTitle());
            holder.docClinic.setText("Clinic : " + doctor.getWorkplace());
            holder.docLoc.setText("Address : " + doctor.getLocation());

            // Decode Base64 image string and display
            byte[] decodedString = Base64.decode(doctor.getImageBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.docImageView.setImageBitmap(decodedByte);

            // Set up click listener for phone call
            holder.bookAppointmentBtn.setOnClickListener(v -> {
                String contactNumber = doctor.getContactNumber();
                if (contactNumber != null && !contactNumber.isEmpty()) {
                    initiatePhoneCall(contactNumber);
                } else {
                    Toast.makeText(v.getContext(), "Contact number not available", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return doctorList.size();
        }

        public void updateList(List<DoctorProfile> filteredList) {
            doctorList = filteredList;
            notifyDataSetChanged();
        }

        public class DoctorViewHolder extends RecyclerView.ViewHolder {
            ImageView docImageView;
            TextView docName, docTitle, docClinic, docLoc;
            Button bookAppointmentBtn;

            public DoctorViewHolder(@NonNull View itemView) {
                super(itemView);
                docImageView = itemView.findViewById(R.id.doc_image_view);
                docName = itemView.findViewById(R.id.doc_name);
                docTitle = itemView.findViewById(R.id.doc_title);
                docClinic = itemView.findViewById(R.id.doc_clinic);
                docLoc = itemView.findViewById(R.id.doc_loc);
                bookAppointmentBtn = itemView.findViewById(R.id.book_appointment_btn);
            }
        }
    }

    // DoctorProfile as an inner class
    private static class DoctorProfile {
        private String name;
        private String title;
        private String workplace;
        private String location;
        private String contactNumber;
        private String imageBase64;

        public DoctorProfile() {
            // Default constructor required for Firebase
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public String getWorkplace() {
            return workplace;
        }

        public String getLocation() {
            return location;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public String getImageBase64() {
            return imageBase64;
        }
    }
}

package com.example.jammp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FrameLayout symptomCheckerFrame, newsFrame, fitnessFrame, appointmentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        // Find all FrameLayouts
        symptomCheckerFrame = findViewById(R.id.symptomCheckerFrame);
        newsFrame = findViewById(R.id.newsFrame);
        fitnessFrame = findViewById(R.id.fitnessFrame);
        appointmentFrame = findViewById(R.id.appointmentFrame);
    }

    private void setupClickListeners() {
        // Set click listeners for each FrameLayout
        symptomCheckerFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SymptomChecker.class));
            }
        });

        newsFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, News.class));
            }
        });

        fitnessFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StayFit.class));
            }
        });

        appointmentFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BookAppointment.class));
            }
        });
    }
}
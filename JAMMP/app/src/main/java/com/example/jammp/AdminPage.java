package com.example.jammp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPage extends AppCompatActivity {

    private FrameLayout addMemberFrame;
    private FrameLayout docRegFrame;
    private FrameLayout fetchVideoFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page); // Make sure this matches your XML layout file name

        // Initialize the frame layouts
        addMemberFrame = findViewById(R.id.AddMemberFrame);
        docRegFrame = findViewById(R.id.DocRegFrame);
        fetchVideoFrame=findViewById(R.id.Vid_Fetch_Frame);
        // Set click listeners for each frame
        addMemberFrame.setOnClickListener(view -> {
            // Redirect to AddMemberActivity
            Intent intent = new Intent(AdminPage.this, AddMember.class);
            startActivity(intent);
        });

        docRegFrame.setOnClickListener(view -> {
            // Redirect to DocRegistrationActivity
            Intent intent = new Intent(AdminPage.this, DocReg.class);
            startActivity(intent);
        });
        fetchVideoFrame.setOnClickListener(view -> {
            // Redirect to DocRegistrationActivity
            Intent intent = new Intent(AdminPage.this, FitnessVideoFetch.class);
            startActivity(intent);
        });
    }
}

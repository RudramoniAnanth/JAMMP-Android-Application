package com.example.jammp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymptomChecker extends AppCompatActivity {

    private GridLayout symptomsContainer;
    LinearLayout containerLayout ;
    private Button submitButton;
    private TextView resultTextView;
    private ImageView  backArrow;
    private final List<String> allergiesSymptoms = Arrays.asList("Sneezing", "Itchy eyes", "Congestion", "Watery eyes");
    private final List<String> athletesfootSymptoms = Arrays.asList("Itchy feet", "Red rash", "Dry skin", "Peeling skin");
    private final List<String> cankersoreSymptoms = Arrays.asList("Small painful ulcer", "Difficulty eating or drinking", "Tingling sensation", "Swollen lymph nodes");
    private final List<String> commoncoldSymptoms = Arrays.asList("Runny nose", "Sore throat", "Cough", "Mild fatigue");
    private final List<String> commonfluSymptoms = Arrays.asList("Fever", "Body aches", "Fatigue", "Chills");
    private final List<String> heartburnSymptoms = Arrays.asList("Burning sensation in chest", "Difficulty swallowing", "Chest pain", "Sour taste in mouth");
    private final List<String> indigestionSymptoms = Arrays.asList("Bloating", "Nausea", "Belching", "Abdominal discomfort");
    private final List<String> insomniaSymptoms = Arrays.asList("Difficulty falling asleep", "Waking up during the night", "Feeling unrefreshed", "Daytime fatigue");
    private final List<String> mildacneSymptoms = Arrays.asList("Pimples", "Blackheads", "Whiteheads", "Skin inflammation");
    private final List<String> mildanxietySymptoms = Arrays.asList("Restlessness", "Increased heart rate", "Sweating", "Difficulty concentrating");
    private final List<String> mildbronchitisSymptoms = Arrays.asList("Persistent cough", "Mucus production", "Chest discomfort", "Mild shortness of breath");
    private final List<String> mildburnsSymptoms = Arrays.asList("Redness", "Pain", "Swelling", "Blistering");
    private final List<String> mildbursitisSymptoms = Arrays.asList("Joint pain", "Stiffness", "Swelling", "Warmth around joint");
    private final List<String> mildcarpaltunnelsyndromeSymptoms = Arrays.asList("Wrist pain", "Numbness in fingers", "Tingling sensation", "Weakness in hand");
    private final List<String> mildconjunctivitisSymptoms = Arrays.asList("Red eyes", "Itchy eyes", "Watery discharge", "Gritty feeling in eyes");
    private final List<String> mildconstipationSymptoms = Arrays.asList("Infrequent bowel movements", "Straining during bowel movements", "Hard stools", "Abdominal discomfort");
    private final List<String> mildcoughSymptoms = Arrays.asList("Tickling sensation in throat", "Dry cough", "Mild soreness", "Occasional throat clearing");
    private final List<String> mildcrampsSymptoms = Arrays.asList("Muscle pain", "Spasms", "Tightness", "Soreness");
    private final List<String> milddiarrheaSymptoms = Arrays.asList("Loose stools", "Frequent bowel movements", "Abdominal cramps", "Urgent need to pass stool");
    private final List<String> milddryeyesSymptoms = Arrays.asList("Itching", "Burning sensation", "Redness", "Blurry vision");
    private final List<String> milddrymouthSymptoms = Arrays.asList("Dryness in mouth", "Sticky feeling in mouth", "Bad breath", "Difficulty swallowing");
    private final List<String> mildfatigueSymptoms = Arrays.asList("Low energy", "Difficulty concentrating", "Sleepiness", "Decreased motivation");
    private final List<String> mildfeverSymptoms = Arrays.asList("Slightly elevated temperature", "Chills", "Sweating", "Mild fatigue");
    private final List<String> mildgastritisSymptoms = Arrays.asList("Stomach pain", "Nausea", "Indigestion", "Bloating");
    private final List<String> mildgoutSymptoms = Arrays.asList("Joint pain", "Swelling", "Redness", "Warmth around joint");
    private final List<String> mildhayfeverSymptoms = Arrays.asList("Sneezing", "Runny nose", "Itchy eyes", "Nasal congestion");
    private final List<String> mildhemorrhoidsSymptoms = Arrays.asList("Itching in rectal area", "Discomfort during bowel movements", "Small lumps around anus", "Mild bleeding");
    private final List<String> mildmigrainesSymptoms = Arrays.asList("Throbbing head pain", "Sensitivity to light", "Nausea", "Aura (flashing lights)");
    private final List<String> mildnauseaSymptoms = Arrays.asList("Queasy feeling in stomach", "Sweating", "Increased saliva production", "Dizziness");
    private final List<String> mildseasonalaffectivedisorderSymptoms = Arrays.asList("Fatigue", "Oversleeping", "Appetite changes", "Difficulty concentrating");
    private final List<String> mildshinglesSymptoms = Arrays.asList("Localized pain", "Burning sensation", "Rash", "Sensitivity to touch");
    private final List<String> mildsinusitisSymptoms = Arrays.asList("Facial pain", "Nasal congestion", "Reduced sense of smell", "Headache");
    private final List<String> mildsprainSymptoms = Arrays.asList("Localized pain", "Swelling", "Bruising", "Limited flexibility");
    private final List<String> mildstyeSymptoms = Arrays.asList("Swollen eyelid", "Red bump on eyelid", "Eye pain", "Watery eye");
    private final List<String> mildsunburnSymptoms = Arrays.asList("Reddened skin", "Skin feels warm", "Mild pain", "Skin sensitivity");
    private final List<String> mildtendinitisSymptoms = Arrays.asList("Localized pain", "Tenderness", "Mild swelling", "Pain with movement");
    private final List<String> mildtonsillitisSymptoms = Arrays.asList("Sore throat", "Difficulty swallowing", "Swollen tonsils", "Fever");
    private final List<String> mildtoothacheSymptoms = Arrays.asList("Tooth pain", "Sensitivity to hot/cold", "Swollen gums", "Difficulty chewing");
    private final List<String> mildurinarytractinfectionSymptoms = Arrays.asList("Frequent urination", "Burning sensation when urinating", "Cloudy urine", "Lower abdominal discomfort");
    private final List<String> mildvertigoSymptoms = Arrays.asList("Dizziness", "Feeling of spinning", "Loss of balance", "Nausea");
    private final List<String> mildyeastinfectionSymptoms = Arrays.asList("Itching in genital area", "Burning sensation", "Redness", "Thick white discharge");
    private final List<String> motionsicknessSymptoms = Arrays.asList("Nausea", "Dizziness", "Cold sweats", "Fatigue");
    private final List<String> sunburnSymptoms = Arrays.asList("Red skin", "Skin tenderness", "Mild swelling", "Skin peeling (later)");
    private final List<String> tensionheadacheSymptoms = Arrays.asList("Dull head pain", "Pressure around forehead", "Tight neck muscles", "Mild sensitivity to light");
    // Add all symptoms to the combined list
    List<List<String>> symptomLists = Arrays.asList(
            allergiesSymptoms, athletesfootSymptoms, cankersoreSymptoms, commoncoldSymptoms,
            commonfluSymptoms, heartburnSymptoms, indigestionSymptoms, insomniaSymptoms,
            mildacneSymptoms, mildanxietySymptoms, mildbronchitisSymptoms, mildburnsSymptoms,
            mildbursitisSymptoms, mildcarpaltunnelsyndromeSymptoms, mildconjunctivitisSymptoms,
            mildconstipationSymptoms, mildcoughSymptoms, mildcrampsSymptoms, milddiarrheaSymptoms,
            milddryeyesSymptoms, milddrymouthSymptoms, mildfatigueSymptoms, mildfeverSymptoms,
            mildgastritisSymptoms, mildgoutSymptoms, mildhayfeverSymptoms, mildhemorrhoidsSymptoms,
            mildmigrainesSymptoms, mildnauseaSymptoms, mildseasonalaffectivedisorderSymptoms,
            mildshinglesSymptoms, mildsinusitisSymptoms, mildsprainSymptoms, mildstyeSymptoms,
            mildsunburnSymptoms, mildtendinitisSymptoms, mildtonsillitisSymptoms, mildtoothacheSymptoms,
            mildurinarytractinfectionSymptoms, mildvertigoSymptoms, mildyeastinfectionSymptoms,
            motionsicknessSymptoms, sunburnSymptoms, tensionheadacheSymptoms
    );
    List<String> problems = Arrays.asList(
            "Allergies","Athlete'sFoot","CankerSore","CommonCold","CommonFlu","Heartburn","Indigestion",
            "Insomnia","MildAcne","MildAnxiety","MildBronchitis","MildBurns","MildBursitis","MildCarpalTunnelSyndrome",
            "MildConjunctivitis","MildConstipation","MildCough","MildCramps","MildDiarrhea","MildDryEyes"
            ,"MildDryMouth","MildFatigue","MildFever","MildGastritis","MildGout","MildHayFever","MildHemorrhoids",
            "MildMigraines","MildNausea","MildSeasonalAffectiveDisorder","MildShingles","MildSinusitis",
            "MildSprain","MildStye","MildSunburn","MildTendinitis","MildTonsillitis","MildToothache",
            "MildUrinaryTractInfection","MildVertigo","MildYeastInfection","MotionSickness","Sunburn","TensionHeadache"
    );

    // Combined list of all symptoms
    private final List<String> allSymptoms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptom_checker);
        symptomsContainer = findViewById(R.id.symptomsContainer);
        submitButton = findViewById(R.id.submitButton);
        resultTextView = findViewById(R.id.resultTextView);
        backArrow = findViewById(R.id.back_arrow);
        containerLayout = (LinearLayout) findViewById(R.id.containerLayout);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(SymptomChecker.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Combine all symptoms into allSymptoms list
        for (List<String> symptoms : symptomLists) {
            allSymptoms.addAll(symptoms);
        }
        createCheckboxes();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedLists();
            }
        });

        // Replace the existing SearchView code in onCreate with this:
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false); // Make it always expanded
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterSymptoms(newText);
                return true;
            }
            private void filterSymptoms(String query) {
                query = query.toLowerCase().trim();

                for (int i = 0; i < symptomsContainer.getChildCount(); i++) {
                    View view = symptomsContainer.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        String symptom = checkBox.getText().toString().toLowerCase();
                        if (query.isEmpty() || symptom.contains(query)) {
                            checkBox.setVisibility(View.VISIBLE);
                        } else {
                            checkBox.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    private void createCheckboxes () {
        for (String symptom : allSymptoms) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(symptom);
            symptomsContainer.addView(checkBox);
        }
    }

    @SuppressLint("SetTextI18n")
    private void displaySelectedLists() {
        List<String> checkedSymptoms = new ArrayList<>();
        for (int i = 0; i < symptomsContainer.getChildCount(); i++) {
            View view = symptomsContainer.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    checkedSymptoms.add(checkBox.getText().toString());
                }
            }
        }

        List<String> selectedLists = new ArrayList<>();
        for (int i = 0; i < symptomLists.size(); i++) {
            if (calculateSimilarity(symptomLists.get(i), checkedSymptoms) >= 50) {
                selectedLists.add(problems.get(i));
            }
        }

        if (checkedSymptoms.isEmpty()) {
            resultTextView.setText("Select the Symptoms bro..!");
            containerLayout.removeAllViews(); // Remove any existing buttons
        } else if (selectedLists.isEmpty()) {
            resultTextView.setText("We Are Unable To Determine Your Problems With the Provided Symptoms. " +
                    "Why Don't You Try Consulting A Doctor : ");
            containerLayout.removeAllViews(); // Remove any existing buttons
            addRedirectButton();
        } else {
            resultTextView.setText("Possible issues: \n" + String.join(", ", selectedLists) +
                    "\nAren't Satisfied Yet..!? " + "\n Why Don't You Try Consulting A Doctor : ");
            containerLayout.removeAllViews(); // Remove any existing buttons
            addRedirectButton();
        }
    }

    private void addRedirectButton() {
        // Create a new button dynamically
        Button redirectButton = new Button(this);
        redirectButton.setText("Book Doctor Appointment");
        redirectButton.setBackground(new GradientDrawable() {{
            setShape(RECTANGLE);
            setCornerRadius(50f); // Adjust as needed
            setColor(Color.parseColor("#3498DB")); // Desired color
        }});

        // Set OnClickListener for the button
        redirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SymptomChecker.this, BookAppointment.class);
                startActivity(intent);
            }
        });

        // Add the button to the container layout
        containerLayout.addView(redirectButton);
    }

    private double calculateSimilarity (List < String > list1, List < String > list2){
        List<String> intersection = new ArrayList<>(list1);
        intersection.retainAll(list2);
        return (double) intersection.size() / list1.size() * 100;
    }

}


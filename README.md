## JAMMP Android Application For Holistic HealthCare

## Overview

JAMMP is an Android mobile application designed to address the healthcare management challenges faced by busy individuals, particularly professionals. It provides a centralized, user-friendly platform integrating essential health tools to empower users to take proactive control of their well-being.This Android application provides a platform focused on health and wellness, offering users tools for symptom checking, appointment booking, accessing health news, and viewing fitness videos. It also includes an administrative interface for managing users, doctor listings, and content curation. The application leverages Firebase for backend services and integrates external APIs for news and video content, utilizing standard Android components and libraries for the user interface and functionality.


**Problem Addressed:** 
Busy schedules often make it difficult to prioritize health, leading to challenges in accessing timely symptom assessment, convenient appointment scheduling, and relevant health information. This can result in delayed care and increased health anxiety.

**Our Solution:** 
JAMMP offers a holistic mobile health management solution by integrating:
* Symptom assessment tools
* Real-time appointment booking
* Access to fitness resources
* Timely health news and updates

This aims to streamline healthcare processes, enhance health awareness, and promote a more balanced lifestyle.

## Features and Technical Implementation

### For Regular Users:

* **User Authentication:**
    * **Functionality:** Secure Sign-Up, Sign-In, and Password Reset.
    * **Technical Implementation:**
        * Uses **Firebase Authentication** for core operations (`createUserWithEmailAndPassword`, `signInWithEmailAndPassword`, `sendPasswordResetEmail`).
        * **Firebase Realtime Database** is queried during sign-up to ensure username uniqueness and during sign-in (if a username is provided) to retrieve the associated email for authentication. User details (including user type) are stored here upon registration.

* **Symptom Guidance:**
    * **Check Your Symptoms:** Select how you're feeling from a comprehensive list. A handy search helps you find symptoms quickly.
    * **Get Potential Insights:** Based on your selections, the app suggests possible health conditions by matching your symptoms against common patterns. *Note: This feature provides general guidance and is not a substitute for professional medical advice.*
    * **Next Steps:** If needed, the app can guide you towards booking a doctor's appointment right after checking your symptoms.

* **Connect with Doctors:**
    * **Find Healthcare Professionals:** Browse through a directory of doctors. You can see their specialty, clinic information, location, and photo.
    * **Search Made Easy:** Quickly find doctors by name, specialty, or clinic using the search bar.
    * **Book Appointments:** Simply tap a button within a doctor's profile to initiate a phone call using your device's calling feature, making it easier to schedule a visit. Doctor profiles are kept up-to-date via the application's database (Firebase).

* **Stay Informed with Health News:**
    * **Latest Updates:** Get current health news articles delivered directly within the app, sourced reliably via the GNews service.
    * **Read & Explore:** View article summaries and images in a clean, scrollable list. Filter news that interests you or search for specific topics. Click to read more details and access the original source online.

* **Get Fit with Curated Videos:**
    * **Fitness Library:** Explore a collection of fitness videos suitable for various interests, sourced via the YouTube API.
    * **Browse & Watch:** Search for videos by title and view them instantly (opens conveniently in your YouTube app or web browser). Video lists are maintained through the application's database (Firebase).

### For Administrators:

* **Easy Management Hub:**
    * A dedicated section provides access to all administrative controls after logging in.
      
* **Manage Users:**
    * Administrators can directly add new users to the system and define their roles (e.g., standard user or another administrator). This process interacts directly with the application's user database (Firebase).

* **Maintain Doctor Directory:**
    * Admins are responsible for adding and updating doctor profiles, including their professional details, contact information, and profile pictures, ensuring the user-facing directory is accurate. All data is stored in the application's database (Firebase).

* **Update Fitness Content:**
    * Admins can refresh the fitness video library. This feature connects to the YouTube service (via its API) to find new, relevant fitness videos and adds their details to the application's database (Firebase) for users to access.

## Technical Stack Summary

* **Platform:** Android (Java)
* **Backend:** Firebase (Authentication, Realtime Database)
* **External APIs:** GNews API, YouTube Data API v3
* **Networking:** OkHttp
* **Image Loading:** Picasso, Glide
* **Data Handling:** Base64 encoding (for images)

## Usage

*(Refer to the "Features and Technical Implementation" section above for detailed usage patterns integrated with technical aspects.)*

1.  **Users:** Sign up/in, then use the main dashboard to access features like Symptom Checker, News, Fitness Videos, and Doctor Booking. Interactions trigger underlying Firebase operations, API calls, and internal logic as described above.
2.  **Administrators:** Sign in with admin credentials to access the Admin Dashboard, then navigate to manage members, register doctors, or curate fitness videos, utilizing specific Firebase and API integrations.

## Usage Guide

1.  **Getting Started:** Download and install the application. Sign up for a new account or log in.
2.  **Exploring Features:** Use the main screen to navigate between the Symptom Checker, Doctor Booking, Health News, and Fitness Video sections. Interact with the lists, search bars, and buttons as described in the "Features" section.
3.  **Admin Access:** Users designated as administrators will be directed to the Admin Hub after logging in, where they can manage users, doctors, and video content.

## Setup 

* Ensure you have Android Studio installed.
* Clone the repository.
* Set up a Firebase project (including Authentication and Realtime Database) and add the generated `google-services.json` file to the `app` directory.
* Obtain API keys for GNews and YouTube Data API v3. Configure these securely within the project (e.g., via `local.properties` and `build.gradle` or similar mechanism – avoid hardcoding keys in source files).
* Build and run the application on an emulator or physical device.

## 🔮 Future Work
As part of our ongoing commitment to improve JAMMP into a smart, AI-powered healthcare assistant, we plan to integrate the following features in upcoming versions. These enhancements aim to offer intelligent, inclusive, and accessible healthcare support to users in remote and underserved communities.

### 🧠 1. AI Chatbot for Mental Health Support  
We plan to integrate an AI-powered chatbot using NLP models like **BERT** or **LSTM** to detect signs of stress, anxiety, or depression from user inputs. The chatbot will offer real-time support including relaxation tips and motivational responses. **Speech-to-text** and **multilingual processing** will be added to improve accessibility and engagement for diverse users.

### 🏃 2. AI-Driven Personalized Fitness & Wellness Recommendations  
Our system will collect user health metrics like **age**, **weight**, and **activity level** to deliver AI-generated workout and wellness plans. **Reinforcement learning** and **collaborative filtering** will personalize recommendations, while **MediaPipe-based pose detection** will offer posture feedback during exercise.

### 📢 3. AI-Generated Health Education & Awareness  
We aim to use **transformer-based models** such as **T5** or **Pegasus** to summarize verified health news and generate region-specific public health content. This will be translated into local languages using **pretrained translation models**, with delivery through a dedicated **in-app news section**.

### 🚨 4. AI-Based Emergency Response System  
This feature will use the device’s **GPS** and **Google Maps API** to locate nearby healthcare centers in real time. allowing the app to guide users to appropriate care or trigger emergency alerts.

### 🧠🔗 Integration of Neuro-Symbolic Thinking  
We plan to integrate **Neuro-Symbolic AI**, which combines the strengths of deep learning (neural networks) with symbolic reasoning (knowledge graphs and logic rules). This hybrid approach will improve **explainability**, **reasoning over medical rules**, and **decision-making transparency** in our system. For example, symbolic logic can help the chatbot or emergency module follow medically approved protocols, while neural models handle complex perception like emotion detection or pattern recognition. This fusion will make JAMMP more robust, interpretable, and medically reliable for both users and healthcare professionals.

## JAMMP Android Application For Holistic HealthCare

## Overview

JAMMP is an Android mobile application designed to address the healthcare management challenges faced by busy individuals, particularly professionals. It provides a centralized, user-friendly platform integrating essential health tools to empower users to take proactive control of their well-being.This Android application provides a platform focused on health and wellness, offering users tools for symptom checking, appointment booking, accessing health news, and viewing fitness videos. It also includes an administrative interface for managing users, doctor listings, and content curation. The application leverages Firebase for backend services and integrates external APIs for news and video content, utilizing standard Android components and libraries for the user interface and functionality.


**Problem Addressed:** 
Busy schedules often make it difficult to prioritize health, leading to challenges in accessing timely symptom assessment, convenient appointment scheduling, and relevant health information. This can result in delayed care and increased health anxiety.

# JAMMP Mobile Health Solution

**Our Solution:** JAMMP integrates symptom assessment, appointment booking, fitness resources, and health news to streamline healthcare and promote healthier lifestyles.

## Features

### For Users:

- **User Authentication:**
  - Secure sign-up/in via Firebase Authentication
  - Firebase Realtime Database stores user details and ensures username uniqueness

- **Symptom Guidance:**
  - Select symptoms from searchable list
  - Receive potential health insights based on symptom patterns
  - Book doctor appointments based on results

- **Doctor Connections:**
  - Browse doctor directory with specialties and clinic information
  - Search by name, specialty, or clinic
  - Book appointments through in-app calling feature
  - Firebase maintains doctor profiles

- **Health News:**
  - Current health articles via GNews service
  - Browse summaries with images
  - Filter/search topics of interest

- **Fitness Videos:**
  - Curated fitness content via YouTube API
  - Search by title
  - Videos open in YouTube app/browser

### For Administrators:

- **Management Hub:**
  - Centralized admin controls

- **User Management:**
  - Add new users and define roles

- **Doctor Directory:**
  - Add/update doctor profiles and information

- **Fitness Content:**
  - Refresh fitness library via YouTube API

## Technical Stack

- **Platform:** Android (Java)
- **Backend:** Firebase (Authentication, Realtime Database)
- **APIs:** GNews, YouTube Data API v3
- **Libraries:** OkHttp, Picasso, Glide
- **Data:** Base64 encoding for images

## Usage

1. **Users:** Access dashboard features after sign-up/in
2. **Administrators:** Manage members, doctors, and fitness content

## Setup

- Install Android Studio
- Clone repository
- Configure Firebase project with Authentication and Realtime Database
- Add `google-services.json` to app directory
- Obtain API keys for GNews and YouTube
- Build and run on emulator/device

## 🔮 Future Work

### 🧠 AI Chatbot for Mental Health
- NLP models (BERT/LSTM) for emotional support
- Speech-to-text and multilingual processing for accessibility

### 🏃 AI-Driven Fitness & Wellness
- Personalized workout plans based on user metrics
- Reinforcement learning and collaborative filtering
- MediaPipe-based pose detection for feedback

### 📢 AI-Generated Health Education
- Transformer models (T5/Pegasus) for health content
- Translation to local languages
- Region-specific public health information

### 🚨 AI-Based Emergency Response
- GPS and Google Maps API integration
- Real-time healthcare center location
- Emergency alerts system

### 🧠🔗 Neuro-Symbolic AI Integration
- Combines deep learning with symbolic reasoning
- Improves explainability and decision transparency
- Follows medical protocols while handling complex perception

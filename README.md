# CampusFix

CampusFix is a modern, versatile Android application designed to streamline the maintenance and repair request process within any campus environment. It serves as a centralized communication bridge between students/faculty and maintenance staff, ensuring that campus issues are reported and resolved efficiently.

## 🎨 Design & Branding
While CampusFix is built to be adaptable for any institution, this implementation features a design system inspired by **Cebu Institute of Technology – University (CIT-U)**:
- **Maroon**: Primary brand color (CIT-U inspired).
- **Gold**: Accent and highlight color (CIT-U inspired).

## ✨ Key Features

### 🔐 Authentication Suite
- **Secure Login & Registration**: Role-based access for Students/Faculty and Maintenance Staff.
- **Forgot Password**: Secure password reset flow using Student/Staff ID.
- **Modern UI**: Card-based layouts with high-quality icons and input validation.

### 📋 Unified Dashboard
- **Request Tracking**: Real-time view of active and pending repair requests.
- **Role-Based Experience**: 
  - **Reporters**: Can create new requests and edit their own pending ones.
  - **Staff**: Can view, manage, and update the status of incoming requests.
- **Detailed Insights**: View full request details including location, description, and attached photos.

### 📸 Repair Reporting
- **Photo Attachments**: Mandatory photo upload for every repair request to provide visual context.
- **Flexible Capture**: Support for both real-time Camera capture and Gallery selection.
- **Internal Persistence**: Images are saved securely to the app's internal storage to survive session restarts.

### 👤 Profile Personalization
- **Profile Management**: View account details and ID information.
- **Custom Profile Photo**: Personalize your account with a profile picture, persisted across the entire application.
- **Facebook-style Top Nav**: Quick access to profile settings and logout via a modern popup menu.

## 🛠️ Tech Stack
- **Language**: Kotlin
- **UI Framework**: Android XML with Material Design 3 (MD3)
- **Data Persistence**: SharedPreferences with GSON for complex object serialization.
- **Image Handling**: `ActivityResultContracts`, `FileProvider`, and `Internal Storage` for secure file management.
- **Architecture**: Transitions from DataManager-based state to MVP (Model-View-Presenter).

## 🚀 Getting Started
1. Clone the repository.
2. Open the project in **Android Studio**.
3. Build and run the app on an emulator or a physical device (API 24+ recommended).

---
*Developed as part of the Mobile Development course at Cebu Institute of Technology - University.*

# NutriTrack – Android Health & Nutrition Tracking App

NutriTrack is an Android health application built with **Jetpack Compose, MVVM architecture, and Room Database**. It supports **patients** in logging their dietary habits, viewing nutrition scores, and receiving AI-driven tips, while **clinicians** can monitor group averages and patterns.

---

## Features

### Patient Module

- **Login & Registration:** Secure account claiming with user ID, phone number, and password.
- **Food Intake Questionnaire:** Records daily habits, persona type, and meal/sleep timings.
- **Nutrition Scoring:** Displays HEIFA-based total and category-level scores (fruits, vegetables, grains, protein, fats, etc.).
- **NutriCoach:**
  - Fetches fruit data using the **Fruity API**.
  - Generates personalized motivational tips via **Google Gemini AI**, stored in Room for future retrieval.
- **Settings:** Displays user profile, allows logout, and access to clinician mode.

### Clinician Module

- **Clinician Login:** Secure access with a key (`dollar-entry-apples`).
- **Dashboard:** Views average male/female scores and receives AI-generated insights about patient patterns.

---

## Technical Stack

- **Architecture:** MVVM + Repository pattern
- **UI:** Jetpack Compose (Material 3)
- **Database:** Room (entities: Patient, FoodIntake, NutriCoachTip)
- **Data Access:** DAOs for patients, food intake, and AI tips
- **Networking:** Retrofit (Fruity API)
- **AI Integration:** Google Gemini API for motivational feedback & clinician insights
- **Persistence:** SharedPreferences for sessions & questionnaire state
- **Tools:** Kotlin, Coroutines, LiveData, Android Studio

---

## Results

- Built a **fully functional mobile app** supporting both patient and clinician workflows.
- Achieved **data persistence** with Room Database and SharedPreferences.
- Integrated **real-time nutrition data** from the Fruity API.
- Delivered **AI-powered coaching and insights** with Gemini API.
- Created a modern, responsive UI with **Jetpack Compose**.

---

## Project Structure

- **Activities:** MainActivity, LoginActivity, UserRegisterActivity, QuestionnaireActivity, ScoreActivity, ClinicianLoginActivity, ClinicianDashboardActivity.
- **ViewModels:** LoginViewModel, RegisterViewModel, ScoreViewModel, QuestionnaireViewModel, NutriCoachViewModel, AIViewModel, ClinicianViewModel, InsightsViewModel, SettingsViewModel.
- **Repositories:** PatientRepository, QuestionnaireRepository, ScoreRepository.
- **Entities:** Patient, FoodIntake, NutriCoachTip, Fruit.
- **Database & DAOs:** AppDatabase, PatientDao, FoodIntakeDao, NutriCoachTipDao.

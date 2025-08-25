# NutriTrack – Android Health & Nutrition Tracking App

NutriTrack is an Android health application built with **Jetpack Compose, MVVM architecture, and Room Database**.  
It supports **patients** in logging their dietary habits, viewing nutrition scores, and receiving AI-driven tips, while **clinicians** can monitor group averages and patterns.

---

## Features

### Patient Module
- **Login & Registration:** Secure account claiming with user ID, phone number, and password:contentReference[oaicite:0]{index=0}.  
- **Food Intake Questionnaire:** Records daily habits, persona type, and meal/sleep timings:contentReference[oaicite:1]{index=1}.  
- **Nutrition Scoring:** Displays HEIFA-based total and category-level scores (fruits, vegetables, grains, protein, fats, etc.):contentReference[oaicite:2]{index=2}:contentReference[oaicite:3]{index=3}.  
- **NutriCoach:**  
  - Fetches fruit data using the **Fruity API**:contentReference[oaicite:4]{index=4}.  
  - Generates personalized motivational tips via **Google Gemini AI**, stored in Room for future retrieval:contentReference[oaicite:5]{index=5}:contentReference[oaicite:6]{index=6}.  
- **Settings:** Displays user profile, allows logout, and access to clinician mode:contentReference[oaicite:7]{index=7}.  

### Clinician Module
- **Clinician Login:** Secure access with a key (`dollar-entry-apples`):contentReference[oaicite:8]{index=8}.  
- **Dashboard:** Views average male/female scores and receives AI-generated insights about patient patterns:contentReference[oaicite:9]{index=9}:contentReference[oaicite:10]{index=10}.  

---

## Technical Stack
- **Architecture:** MVVM + Repository pattern:contentReference[oaicite:11]{index=11}:contentReference[oaicite:12]{index=12}:contentReference[oaicite:13]{index=13}  
- **UI:** Jetpack Compose (Material 3)  
- **Database:** Room (entities: Patient, FoodIntake, NutriCoachTip):contentReference[oaicite:14]{index=14}:contentReference[oaicite:15]{index=15}:contentReference[oaicite:16]{index=16}  
- **Data Access:** DAOs for patients, food intake, and AI tips:contentReference[oaicite:17]{index=17}:contentReference[oaicite:18]{index=18}:contentReference[oaicite:19]{index=19}  
- **Networking:** Retrofit (Fruity API):contentReference[oaicite:20]{index=20}  
- **AI Integration:** Google Gemini API for motivational feedback & clinician insights:contentReference[oaicite:21]{index=21}  
- **Persistence:** SharedPreferences for sessions & questionnaire state:contentReference[oaicite:22]{index=22}  
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

---

## Author
**Nashmia Shakeel**  

<div align="center">

# MentiVita 🌱
**Project/App prototype focused on cognitive stimulation, medication management & daily routine companion for individuals in the early stages of Alzheimer's disease**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android SDK](https://img.shields.io/badge/Android%20SDK-26%20--%2035-3DDC84.svg?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Offline--First-FF6F00.svg?style=for-the-badge)](#architecture--technical-highlights)

</div>

<!-- table of contents -->
<details>
  <summary><b>📋 Table of Contents</b> (click to expand)</summary>
  <ol>
    <li><a href="#-about-the-project">About The Project</a></li>
    <li><a href="#️-built-with">Built With</a></li>
    <li><a href="#-how-to-run">How to run?</a></li>
    <li>
      <a href="#-features">Features</a>
      <ul>
        <li><a href="#1-cognitive-stimulation-games">1. Cognitive stimulation games</a></li>
        <li><a href="#-2-medication-reminder-system">2. Medication reminder system</a></li>
        <li><a href="#-3-daily-routine--task-manager">3. Daily routine & task manager</a></li>
        <li><a href="#-4-mind-diet-recipes">4. MIND diet recipes</a></li>
        <li><a href="#-5-gamification-motivational-module">5. Gamification (Motivational module)</a></li>
        <li><a href="#-6-alzheimer-friendly-accessibility--uiux">6. Alzheimer-friendly accessibility & UI/UX</a></li>
      </ul>
    </li>
    <li><a href="#️-architecture--technical-highlights">Architecture & technical highlights</a></li>
    <li><a href="#-verification--device-testing">Verification & device testing</a></li>
    <li><a href="#️-a-couple-of-future-possible-enhancements">Future possible enhancements</a></li>
    <li><a href="#-acknowledgments--license">Acknowledgments & license</a></li>
  </ol>
</details>

## 📖 About The Project

Effective care for Mild Cognitive Impairment (MCI) and early-stage Alzheimer's disease requires a holistic approach that goes beyond standard pharmacotherapy. While medications offer temporary symptom relief, engaging non-pharmacological interventions—such as cognitive stimulation, structured daily routines, and a neuroprotective diet - play a vital role in building cognitive reserve and prolonging patient independence.

The app was built specifically for seniors with early cognitive decline, MentiVita attempts to eliminate common digital barriers such as complex registration flows, paywalls, stressful countdown timers, cluttered interfaces, tiny UI components and fonts. As for now, the project only supports polish language.

---

## 🛠️ Built With

* **Language:** [Kotlin](https://kotlinlang.org/) (`2.0.21`)
* **Target & min SDK:** Android SDK 35 (Android 15) / Min SDK 26 (Android 8.0)
* **UI framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose), **Material Design 3**, `Material Icons Extended`, `Compose Google Fonts`
* **Architecture & reactive programming:** MVVM (*Model-View-ViewModel*), **Kotlin Coroutines**, **Kotlin Flow**
* **Dependency injection:** [Dagger Hilt](https://dagger.dev/hilt/) with **KSP**
* **Local persistence & serialization:** [Room Database](https://developer.android.com/training/data-storage/room) (SQLite) + **Kotlinx Serialization**
* **Navigation:** Jetpack Compose **Type-Safe Navigation**
* **Image loading:** [Coil](https://coil-kt.github.io/coil/)
* **Background processing & alarms:** Android `AlarmManager` (`setExactAndAllowWhileIdle`), `BroadcastReceiver` (`ACTION_BOOT_COMPLETED` with `goAsync()`)

---

## 🚀 How to run?

1. **Install and launch Android Studio** on a machine that meets the hardware and system requirements.
2. **Clone the repository:**
   ```bash
   git clone https://github.com/yola512/MentiVita.git
   
2. In Android Studio, go to **File -> Open** and select the cloned `MentiVita` project folder.
3. **Wait for Gradle sync:** Allow Android Studio to download the required libraries and configure the environment.
4. Prepare a target device for testing:
   - **Option A:** Using a physical mobile device:
      - On your phone, go to Settings -> Developer options, then enable USB debugging and Install via USB. (If you don't see Developer options, follow the official guide at developer.android.com/studio/debug/dev-options).
      - Connect your phone to the computer via a USB cable. Android Studio should automatically detect the device.
   - **Option B:** Using an emulator:
      - In Android Studio, open Device Manager, click Create Virtual Device (+ icon), select a device model (e.g., Pixel 7a), and download a system image (e.g., API 34, or any version from API 26 upwards).
5. **Run the project:** On the top toolbar, select your target device and make sure the app module is selected. Click Run (green play triangle) and wait for the application to install and launch on the device.

---
---

## 🎯 Features

### 1. Cognitive stimulation games
Four cognitive games targeting distinct areas of brain function in a stress-free, untimed environment:

- **📬 Postman (Working memory - Single N-back):** 
  - **Mechanics:** Players take on the role of a postman, viewing a sequence of 15 colorful houses and determining whether the current house matches the color seen *n* steps earlier. Features immediate, encouraging visual feedback explaining both correct answers and mistakes.
  - **Why this game?** Classic *Dual N-back* tasks (tracking abstract squares on a grid) are overly complex and frustrating for individuals with cognitive decline. Because **color recognition remains intact much longer than spatial orientation** in Alzheimer's disease, *Postman* replaces abstract spatial grids with a friendly, relatable theme and color matching mechanics.
  <br>
  <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/46b213b6-ca3c-42c9-b4d2-a9de068ba7d6" /> <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/8a18d634-3576-4049-bfd0-ef9da923cf89" />

- **🔢 Sudoku (Logic, executive function & short-term memory):** 
  - **Mechanics:** Adapted and refactored from the open-source *Neurodoku* project (MIT License). Tailored for Alzheimer's patients by hiding stressful on-screen timers, increasing initial prefilled cells on the 9x9 grid, providing at-a-glance access to rules at any moment during gameplay, and adding a post game board comparison mode.
  - **Why this game?** Early-stage Alzheimer's marks the beginning of difficulties with **Instrumental Activities of Daily Living (IADL)**, such as planning, decision-making, and managing finances—though patients still retain partial independence. Sudoku is a familiar pastime for seniors that directly trains **executive functions** (planning the next move, evaluating consequences against a set of rules) while exercising short term memory.
    
  <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/c3c2dab1-d9fc-4b6c-814b-f203d0f50f46" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/3fec3a66-6553-4191-bbeb-add052e01339" />



- **🕵️ Eye of the spy (Attention, visual perception & agnosia prevention):** 
  - **Mechanics:** A hidden object game where players match written item names to (everyday) objects hidden within an illustration. The system dynamically scales touch coordinates across phone and tablet screens, circles found items, and autosaves progress in real time.
  - **Why this game?** As Alzheimer's progresses toward the moderate stage, patients begin to experience **visual agnosia** - e.g. the inability to recognize and identify the purpose of everyday objects. Matching written labels to visual representations of common household items helps stimulate visual-spatial perception and maintain object recognition pathways.
 
  <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/d5a1eec9-321a-4619-a791-4f4664969bff" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/818b0d01-befa-413b-8cab-d854d89190af" />



- **📜 Hidden proverbs (Language, long-term memory & aphasia mitigation):** 
  - **Mechanics:** A cryptogram puzzle based on Polish proverbs with 'category' hints. Includes auto filling of matching letter tiles, disabling of completed keyboard keys, a hint system, and **multi-sensory error feedback** (red tile highlight, shake animation, and haptic vibration).
  - **Why this game?** Early-stage Alzheimer's frequently brings language deficits (**aphasia**), such as difficulties with finding a 'correct' word and a shrinking vocabulary. Inspired by seniors' fondness for crosswords, this game uses **culturally rooted, well-known proverbs**. Because proverbs are deeply embedded in long-term memory, patients can successfully unlock and retrieve them even if the sentences are long. Moreover, reconstructing common phrases reinforces everyday vocabulary and supports verbal communication.
  
  <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/7216eea2-626d-48e7-821a-f897de827765" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/26e5405a-964a-4315-9e3a-5ceb30718412" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/4b0921c1-3865-4c1f-9c67-5418be8227bd" />



- **💬 Interactive step-by-step tutorials:** Guided bubble speech overlays that progressively unlock UI buttons and walk the user through game rules in practice.
  
### 💊 2. Medication reminder system
- **Why?:** Individuals in the early stages of Alzheimer's begin struggling with Instrumental Activities of Daily Living (IADL) and distinguishing times of the day, which significantly increases the risk of medication errors, skipped doses, or accidental double-dosing. This module is designed as an attempt to prolong safe independent medication management.
- **Comprehensive CRUD & form validation:** Users can add, view, edit, and delete medications via a structured form defining the **medication name, dosage, date range, specific days of the week, and intake times**. To prevent incomplete schedules, the system blocks saving if required fields (name, dose, or time) are missing. Deleting or editing a medication automatically clears or updates all associated system alarms.
- **In-app guidance dialog:** Includes a dedicated informational modal providing instructions on how to add medications, how to handle **multiple/different dosages of the same medication**, and how the recurring reminder mechanism works.
- **Automatic time-of-day grouping:** Scheduled doses are automatically sorted and categorized into four distinct periods based on the form's intake hours: **Morning, Afternoon, Evening, and Night**.
- **Dynamic card states & 1-hour safety window:** Medication cards dynamically change their visual appearance and interactivity based on real-time status:
  1. *Upcoming dose (Locked):* Informs the user of an approaching dose while **blocking premature logging** before the scheduled hour arrives.
  2. *Active window (Up to 1 hour):* Allows the user to check or uncheck the dose as *"Taken"* (*Przyjęty*).
  3. *Missed dose (>1 hour overdue):* If the medication is not marked as taken within 1 hour of the scheduled notification, **interaction is locked** and the card displays an explicit warning suggesting **contacting the caregiver** (attempt to protect the patient from taking two doses too close together).
- **10-minute snooze:** At the scheduled time, the app triggers both an **audible notification and an interactive dialog window** requiring user action. If ignored, the system automatically repeats the reminder **every 10 minutes** until the dose is logged.
- **Doze mode bypass & reboot recovery:** Uses `AlarmManager` with `setExactAndAllowWhileIdle()` to wake the CPU even in Android's deep sleep (*Doze Mode*). After a device reboot, the system asynchronously restores all scheduled medication alarms and pending snoozes from the Room database without requiring the user to open the app.
- **Deep-linking & guiding pulse animation:** Tapping either the system notification or the dialog window launches the app, navigates directly to the Medication module, **automatically scrolls the list** to the exact medication, and highlights its card with a **pulsing animation** so the user immediately knows which pill to take.
- **Interactive calendar slider:** Allows browsing the medication schedule for any selected date and features a return button to quickly jump back to the **current day**.
- (!) **Known limitations:** Because a rigid 1-hour logging window is not safe for all prescriptions (e.g., strict multi-dose regimes where taking a pill even 30 minutes late can dangerously shorten the interval before the next dose), this module should be treated as an underdeveloped idea and expanded with e.g. customizable, drug-specific time windows to ensure full clinical safety.

  <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/c09e0c81-eab7-4886-b333-c1e2b4761433" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/b246a9a5-944c-4c2b-a1df-2e5d5717f5b1" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/1ebd92ee-00e7-46d1-864e-d1f51d4e8674" /> <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/bbc38b9f-3809-4c89-9c12-3f29fd6cf08a" />



---

### 📅 3. Daily routine & task manager
- **Unified visual layout:** The task manager intentionally mirrors the visual structure, navigation, and form layouts of the Medication module. Because cognitive decline makes learning new interfaces wearing, this visual consistency ensures users only need to learn one interaction pattern to use both modules effortlessly.
- **Two-tier task categorization:** Supports full CRUD operations (add, preview, edit, delete) for two distinct types of daily activities:
  * **Important Events:** Time-critical tasks that require completion at a specific hour.
  * **All-Day Tasks:** General daily errands and household routines without a strict hourly deadline.
- **Alarm lifecycle & past-time filtering:**
  * Alerts are generated exclusively for time-specific *"Important Events"*.
  * Unlike medications, task notifications are **strictly one-time alerts without a 10-minute snooze loop**, preventing notification fatigue for non medical errands.
  * The system **automatically skips scheduling notifications** for tasks whose execution time on a given day has already passed.
  * Deleting a task, modifying its scheduled hour, or marking it as **completed** automatically cancels any pending system notifications tied to it.
- **Flexible advance reminders** Users can choose to be notified either **on time** or with an advance heads-up (**15 or 30 minutes prior**). When an advance option is selected, the system automatically schedules **two separate notifications**: the first warning the user 15/30 minutes before the event, and the second firing punctually at the scheduled hour.
- **Morning & Evening batch reviews:** To build a habit of checking the daily plan, the system sends two daily summary prompts:
  * **Morning review (8:00 AM):** Displays a tailored message dynamically adapted to the exact number of tasks scheduled for that day.
  * **Evening review (5:00 PM:** Sent **conditionally**. It triggers *only* if there are still uncompleted tasks remaining on the user's list for that day.
- **Deep-linking, pulse animation & reboot persistence:** Just like the Medication module, tapping a task alert navigates directly to the task list, scrolls to the target item, and highlights it with a **pulsing animation**. All scheduled task alarms are persisted in the local Room database and automatically rescheduled upon device reboot.
- **Interactive calendar slider:** Enables reviewing past or future daily plans with a single-tap return to today's schedule.

  <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/fcd564ab-f54a-44de-a6a9-d2ff3f64afeb" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/f8024aac-1289-4408-b373-34720a38b211" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/3f80828e-c1c2-40fe-8da2-37a820f09b4a" />



### 🥗 4. MIND diet recipes
* Offline recipe book based on the neuroprotective **MIND Diet** (Mediterranean-DASH Intervention for Neurodegenerative Delay), organized into 5 categories (*Vegetarian*, *With Meat*, *Fish & Seafood*, *Salads & Snacks*, *Something sweet*).
* Global and category-scoped text search.
* **Executive function support:** Every recipe displays estimated preparation time, difficulty level, ingredients, numbered instructions, and *required kitchen utensils* checklist so users can prepare their workspace before cooking.

<img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/ce674061-f409-4b70-8b11-e13104a84831" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/3c478c4e-499c-413d-ba7d-74c9cbaa3a00" /><img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/9ed09994-34e3-422d-88ba-37e42b1930a9" />



### 🌱 5. Gamification (Motivational module)
* **My Wonderful Garden:** Completing at least one cognitive game per day increments the user's daily streak and drives a **growing plant animation**, fostering daily engagement.
* **My Book of Medals:** Trophy and achievement system rewarding milestones across games, medication adherence, and task completion.

<img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/79f0fa75-83c0-4a66-b6d1-c125d9b8c3ba" />
<img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/882d20d0-c0ea-4817-9376-e9379cff71a8" />
<img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/2960968e-99b9-48e7-84e6-2cc09fbad76c" />

### 🧓🏼 6. Alzheimer-friendly accessibility & UI/UX
* **Scalable typography:** Global font size selector in Settings module coupled with generous line spacing.

  <img width="220" height="476" alt="image" src="https://github.com/user-attachments/assets/60fa633d-1cd4-454b-8e81-9790bb5cd223" />

- **Toned down, high-contrast Material 3 palette:** Uses a calm, non-overwhelming color scheme with high contrast between UI elements so components stand out clearly without causing visual fatigue.
- **100% offline:** All featuresm, including cognitive games, the MIND diet recipes, and medication/task schedulers - work completely offline without requiring an internet connection. The app can be used reliably anywhere and at any time, eliminating confusing network error messages or loading screens.
- **Enlarged touch targets & explicit text labels:** Oversized clickable areas compensate for reduced fine motor control in seniors. Ambiguous icon-only buttons are avoided in favor of large, explicitly labeled actions (e.g., a prominent button labeled with the word *"Powrót"* / *"Back"* instead of just an arrow).
- **No login, registration, or paywalls:** In response to limitations identified in commercial apps, MentiVita completely removes complex multi-step authentication, user accounts, and subscriptions. Launching the app takes the user straight to the Main (Games) screen, featuring a 5-tab Bottom Navigation Bar.
- **Guiding animations & progressive disclosure:** Instead of leaving the user wondering what changed or where to look, the UI actively guides their attention using **pulsing card animations** after tapping a notification, **gradual button unlocking** during interactive speech-bubble tutorials, and **multi-sensory feedback** (color shifts, shake animations, and haptic vibrations).
- **Standard reactive UI without manual refreshing:** Powered by `Kotlin Flow`, any database change (such as checking off a medication or task) updates the screen instantaneously, removing the need for manual pull-to-refresh gestures that are unintuitive.
- **Stress-free interactions:** Eliminates anxiety-inducing mechanics on screen. Timers in Sudoku are hidden (measured only in the background), starting grids have more prefilled numbers to prevent discouragement, and error messages in *Postman* gently explain mistakes rather than penalizing the player.
- **Natural language & dynamic Polish grammar:** Full support for dynamic grammatical inflection in UI messages (e.g., proper pluralization of days, tasks, or points), ensuring messages always sound natural and clear.
- **Forced portrait orientation:** Locks the screen in portrait mode across both smartphones and tablets, preventing sudden layout shifts and spatial disorientation when a user accidentally tilts the device.

---

## 🏗️ Architecture & technical highlights

- **MVVM (Model-View-ViewModel) architecture:** The codebase is divided into distinct layers to ensure maintainability and separation of concerns.
- **Local Room database & custom serialization:** Built around a 10-table SQLite schema.
  * **Handling complex types:** Because SQLite only supports primitive types, a custom `Converters` class powered by **Kotlinx Serialization** (`@TypeConverter`) serializes 2D Sudoku arrays, cryptogram cipher maps, and lists of found objects into JSON strings.
- **Architectural refactoring of the Sudoku module (*Neurodoku* adaptation):** Integrating the open-source *Neurodoku* project into MentiVita required structural refactoring to unify both architectures:
  * Removed the optional domain layer (`UseCase` classes) to match MentiVita's MVVM structure, consolidating board generation logic into a single `SudokuGenerator` class.
  * Merged the domain model (`SudokuGame`) and database entity (`SudokuEntity`) into a unified `SudokuEntity`, eliminating boilerplate mapping overhead (`SudokuMapper`).
  * Replaced the legacy **Gson** library with **Kotlinx Serialization** for consistency across the data layer, and stripped out unused bloat (login/registration, blog, developer contact, notes, and statistics).
- **Dependency injection & type-safe navigation:** Uses **Dagger Hilt** (accelerated at compile time via **KSP**) for automated dependency injection across ViewModels, Repositories, and DAOs. Screen transitions rely on **Compose Type-Safe Navigation**, leveraging Kotlin serialization to guarantee compile time safety when passing arguments (such as medication or task IDs from deep-linked notifications).

---

## 🧪 Verification & device testing

The application underwent a manual verification across physical hardware and Android emulators to validate UI scaling, offline persistence, and background alarm reliability:

| Device type | Model | OS / API level | Resolution |
| :--- | :--- | :--- | :--- |
| **Physical smartphone** | Xiaomi Redmi Note 8T | Android 11.0 (API 30) | 1080 × 2340 px |
| **Smartphone emulator** | Google Pixel 7a | Android 14.0 (API 34) | 1080 × 2400 px |
| **Tablet emulator** | Google Pixel Tablet | Android 15.0 (API 35) | 2500 × 1600 px |

### Key verification findings:
1. Verified responsive layouts across phone and tablet aspect ratios at all font sizes without text clipping.
2. Confirmed seamless mid-game state restoration (*Sudoku*, *Hidden Proverbs*, *Eye of the Spy*) and snooze persistence after process death.
3. **OEM reboot behaviour (MIUI Case Study):** Testing on the physical Xiaomi Redmi Note 8T revealed that aggressive OEM battery management (MIUI) blocks `ACTION_BOOT_COMPLETED` by default. Manually enabling the system **Autostart** permission for MentiVita resolved the issue, allowing system to restore all medication and task schedules immediately after reboot.

---

## 🛠️ (A couple of...) Future possible enhancements

* [ ] **Enhanced sudoku accessibility:** Add an interactive step-by-step tutorial and smaller grid options (`4x4` and `6x6`) for beginners.
* [ ] **Expanded virtual garden:** Unlock new plant species after each completed week, add a plant showcase cabinet, and introduce an interactive watering calendar.
* [ ] **Dynamic achievement notifications:** Implement full unlocking logic and real-time in-app banners for all trophies in the *Book of Medals*.
* [ ] **Customizable medication time windows:** Allow user to define custom safe-dosing windows per medication.
* [ ] **Caregiver dashboard:** Optional cloud sync allowing caregivers to remotely monitor adherence, cognitive progress, and daily schedules (though this would require moving away from a purely offline model).
* [ ] **Automated testing** Add unit and instrumentation tests.

---

## 📄 Acknowledgments & license

The Sudoku module is adapted and refactored from **[Neurodoku](https://github.com/KatarinaKovacova/Neurodoku)** by Katarina Kovacova, licensed under the **MIT License**.

Copyright 2026 Jolanta Majcher. Default copyright laws apply [learn more here](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/licensing-a-repository).

# IronWalk - Women's Safety Application

## 🛡️ Overview
**IronWalk** is a comprehensive women's safety Android application designed to provide real-time location tracking, emergency alerts, safe zone mapping, and quick access to emergency services. The app empowers women with tools to stay safe and connected with trusted contacts during emergencies.

---

## 📱 Project Information
- **Project ID**: 2447216_Appathon_IronWalk
- **Application ID**: `com.example.menus7`
- **App Name**: IronWalk
- **Version**: 1.0 (Version Code: 1)
- **Min SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 34 (Android 14)
- **Compile SDK**: API 34

---

## 🎯 Key Features

### 1. **Emergency SOS System**
- One-tap SOS button with multiple emergency categories:
  - Harassment
  - Stalking
  - Unsafe Situation
  - Medical Emergency
  - Other emergencies
- Flexible alert distribution options:
  - Send to saved emergency contacts
  - Send to specific number
  - Share with all contacts
- Automatic location sharing via SMS with Google Maps link
- Phone vibration alert on SOS activation

### 2. **Real-Time Location Tracking**
- Live GPS tracking with Google Maps integration
- Continuous location updates every 10 seconds
- Location sharing with trusted contacts
- Visual map display of current location

### 3. **Safe Zones Mapping**
- Pre-defined safe zones on interactive map
  - Police stations
  - Hospitals
  - Women's shelters
  - Shopping malls
  - Public transportation hubs
- Search functionality for nearby safe locations
- Visual markers and zone boundaries on map

### 4. **Emergency Contacts Management**
- Add and manage multiple emergency contacts
- Primary contact designation
- Store contact name, phone, and email
- Quick dial emergency numbers:
  - Police (100)
  - Women's Helpline (1091)
  - Emergency Services (112)
  - Ambulance (108)
  - Fire (101)

### 5. **Transport Options**
- Quick access to ride-sharing services:
  - Ola
  - Uber
  - IRCTC (Indian Railways)
- Direct integration for safe transportation booking

### 6. **User Authentication**
- Secure login and signup system
- Email-based authentication
- Password-protected accounts
- Persistent login sessions
- User profile management

### 7. **FAQ & Support**
- Comprehensive FAQ section
- App usage guidelines
- Emergency procedures
- Privacy policy access
- Support contact information

---

## 🏗️ Tech Stack

### **Programming Language**
- **Kotlin** (100%)
  - Kotlin Standard Library 1.9.22
  - Kotlin Coroutines for asynchronous operations

### **Android Framework**
- **Android SDK API 24-34**
- **AndroidX Libraries**:
  - Core KTX 1.12.0
  - AppCompat 1.6.1
  - ConstraintLayout 2.1.4
  - RecyclerView (via Material Design)

### **UI/UX**
- **Material Design Components** 1.11.0
  - Material3 theme
  - FloatingActionButton
  - Cards, Dialogs, Snackbars
- **ViewBinding** for type-safe view access
- **Custom Themes** (Light/Dark mode support)

### **Database**
- **Room Database** 2.6.1
  - Room Runtime
  - Room KTX (Kotlin Extensions)
  - Room Compiler (KAPT)
- **Local SQLite Database** for:
  - User accounts
  - Emergency contacts
  - App preferences

### **Location Services**
- **Google Play Services**:
  - Maps SDK 18.2.0
  - Location Services 21.1.0
- **FusedLocationProviderClient** for location updates
- **Google Maps API** for map visualization

### **Architecture Components**
- **Lifecycle Components** 2.7.0
  - ViewModel KTX
  - LiveData KTX
  - Lifecycle Common Java8
- **Kotlin Coroutines** 1.7.3 for async operations
- **Flow** for reactive data streams

### **Data Handling**
- **Gson** 2.10.1 for JSON parsing
- **Type Converters** for Room database

### **Build System**
- **Gradle** 7.4.2
- **Android Gradle Plugin** 7.4.2
- **Kotlin Gradle Plugin** 1.9.22

---

## 🗄️ Database Schema

### **Room Database: `app_database`**

#### **Tables:**

1. **users**
   ```kotlin
   - id: Long (Primary Key, Auto-generated)
   - username: String
   - email: String
   - password: String
   - isLoggedIn: Boolean
   ```

2. **emergency_contacts**
   ```kotlin
   - id: Long (Primary Key, Auto-generated)
   - name: String
   - phone: String
   - email: String (Nullable)
   - isPrimary: Boolean
   ```

#### **DAOs (Data Access Objects):**

- **UserDao**: User authentication and session management
  - Insert, update, query users
  - Login/logout operations
  - User verification by email/username

- **EmergencyContactDao**: Contact management
  - Insert, delete contacts
  - Query all/primary contacts
  - Contact list retrieval

---

## 🔌 APIs & Integrations

### **Google APIs**
1. **Google Maps API**
   - API Key: `AIzaSyDSrc32DR4GmP7_wKy-wiAPlqwFW4q1-UE`
   - Services: Map rendering, markers, location display
   - Features: Zoom controls, My Location button

2. **Google Location Services API**
   - FusedLocationProviderClient
   - LocationRequest with high accuracy priority
   - Location updates every 10 seconds

### **External Services**
- **SMS Manager API** for emergency alerts
- **Intent-based integrations**:
  - Phone dialer (tel: URI)
  - Web browser (https: URI)
  - Share functionality (ACTION_SEND)

### **Third-Party Services Links**
- Ola Cabs: https://www.olacabs.com
- Uber: https://m.uber.com
- IRCTC: https://www.irctc.co.in
- UN Women Safety: https://www.unwomen.org/en/what-we-do/ending-violence-against-women

---

## 📦 Modules & Components

### **Core Modules**

#### 1. **Authentication Module**
- `LoginActivity.kt` - User login interface
- `SignupActivity.kt` - User registration
- `SplashScreenActivity.kt` - App initialization

#### 2. **Main Application Module**
- `MainActivity.kt` - Primary app interface
  - WebView for safety resources
  - Google Maps integration
  - SOS button functionality
  - Menu navigation

#### 3. **Safety Features Module**
- `SafeZonesActivity.kt` - Safe location mapping
- `EmergencyContactsActivity.kt` - Contact management
- Emergency alert system with SMS integration

#### 4. **Information Module**
- `FAQActivity.kt` - Frequently asked questions
- `ProfileActivity.kt` - User profile management

#### 5. **Data Layer**
- `data/` package:
  - `AppDatabase.kt` - Room database configuration
  - `User.kt` - User entity
  - `EmergencyContact.kt` - Contact entity
  - `UserDao.kt` - User data operations
  - `EmergencyContactDao.kt` - Contact operations
  - `Converters.kt` - Type converters

#### 6. **Utility Module**
- `utils/` package:
  - `SnackbarUtils.kt` - UI feedback utilities
  - `FileUtils.kt` - File management utilities

#### 7. **UI Components**
- `EmergencyContactAdapter.kt` - RecyclerView adapter
- Custom dialogs:
  - SOS alert dialog
  - Contact selection dialog
  - Transport options dialog
  - Add contact dialog

---

## 🔐 Permissions

### **Required Permissions:**
- `INTERNET` - Network communication
- `ACCESS_FINE_LOCATION` - GPS location
- `ACCESS_COARSE_LOCATION` - Network-based location
- `ACCESS_NETWORK_STATE` - Network connectivity check
- `SEND_SMS` - Emergency SMS alerts
- `READ_CONTACTS` - Contact access
- `VIBRATE` - Alert vibrations
- `FOREGROUND_SERVICE` - Background location tracking
- `POST_NOTIFICATIONS` - Alert notifications

### **Storage Permissions (Legacy):**
- `WRITE_EXTERNAL_STORAGE` (Max SDK 29)
- `READ_EXTERNAL_STORAGE` (Max SDK 32)
- `MANAGE_EXTERNAL_STORAGE`

---

## 🎨 UI/UX Design

### **Themes**
- Custom Material Design 3 theme
- NoActionBar variants for full-screen activities
- Light and night mode support
- Transparent status bar with light icons

### **Layout Resources**
- `activity_main.xml` - Main interface with map and WebView
- `activity_login.xml` - Login screen
- `activity_signup.xml` - Registration screen
- `activity_safe_zones.xml` - Safe zones map
- `activity_emergency_contacts.xml` - Contacts list
- `activity_faq.xml` - FAQ list
- Multiple custom dialog layouts

### **Visual Assets**
- Custom app icon (`lalbagh.xml/png`)
- Material icons and drawables
- Multiple density support (hdpi, xhdpi, xxhdpi, xxxhdpi)

---

## 🛠️ Build Configuration

### **Gradle Configuration**
```gradle
- Application Plugin: com.android.application
- Kotlin Android Plugin
- Kotlin KAPT Plugin (for Room)
```

### **Compile Options**
- Source Compatibility: Java 8
- Target Compatibility: Java 8
- Kotlin JVM Target: 1.8

### **Build Features**
- ViewBinding enabled
- Lint: abortOnError = false

### **ProGuard**
- Configured for release builds
- Rules defined in `proguard-rules.pro`

---

## 🚀 Getting Started

### **Prerequisites**
1. Android Studio (Arctic Fox or later)
2. JDK 8 or higher
3. Android SDK with API 24-34
4. Google Play Services installed
5. Valid Google Maps API key

### **Setup Instructions**

1. **Clone the repository**
   ```bash
   cd C:\Users\91766\Desktop\2447216_Appathon_IronWalk\Menus7
   ```

2. **Configure Google Maps API**
   - Obtain a Google Maps API key from Google Cloud Console
   - Replace the API key in `AndroidManifest.xml`:
     ```xml
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="YOUR_API_KEY_HERE" />
     ```

3. **Sync Gradle**
   ```bash
   ./gradlew sync
   ```

4. **Build the project**
   ```bash
   ./gradlew build
   ```

5. **Run on device/emulator**
   - Connect Android device or start emulator
   - Click "Run" in Android Studio or:
   ```bash
   ./gradlew installDebug
   ```

---

## 📂 Project Structure

```
Menus7/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/menus7/
│   │   │   │   ├── data/                    # Database entities & DAOs
│   │   │   │   ├── utils/                   # Utility classes
│   │   │   │   ├── MainActivity.kt          # Main app screen
│   │   │   │   ├── LoginActivity.kt         # Authentication
│   │   │   │   ├── SafeZonesActivity.kt     # Safe zones map
│   │   │   │   ├── EmergencyContactsActivity.kt
│   │   │   │   ├── FAQActivity.kt
│   │   │   │   └── ...
│   │   │   ├── res/
│   │   │   │   ├── layout/                  # XML layouts
│   │   │   │   ├── drawable/                # Vector assets
│   │   │   │   ├── values/                  # Strings, colors, themes
│   │   │   │   └── mipmap/                  # App icons
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/                     # Instrumented tests
│   │   └── test/                            # Unit tests
│   ├── build.gradle                         # App-level Gradle
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml                   # Dependency versions
│   └── wrapper/
├── build.gradle                             # Project-level Gradle
├── settings.gradle                          # Gradle settings
└── gradle.properties                        # Gradle configuration
```

---

## 🧪 Testing

### **Test Infrastructure**
- **JUnit 4.13.2** for unit testing
- **AndroidX Test** for instrumented testing
  - JUnit Extension 1.1.5
  - Espresso Core 3.5.1

### **Test Locations**
- Unit tests: `app/src/test/java/`
- Instrumented tests: `app/src/androidTest/java/`

---

## 🔒 Security Features

1. **Data Encryption**: Local database with encrypted storage
2. **Secure Authentication**: Password-based login
3. **Permission Management**: Runtime permission requests
4. **Network Security**: HTTPS for external communications
5. **Location Privacy**: User-controlled location sharing
6. **Session Management**: Persistent login with secure logout

---

## 🌐 External Resources

### **Safety Information**
- UN Women - Violence Against Women resources
- Integrated FAQ with safety guidelines

### **Emergency Services**
- Direct dial integration for emergency numbers
- Quick access to women's helpline services

---

## 🔄 Version Control

- **Version Name**: 1.0
- **Version Code**: 1
- **Database Version**: 3
- **Fallback Strategy**: Destructive migration enabled

---

## 📝 Notes

### **Known Configurations**
- Legacy external storage support enabled
- Request legacy external storage for compatibility
- Main thread queries allowed (for testing - should be removed in production)
- Fallback database creation for error recovery

### **Lint Configuration**
- Abort on error: disabled
- Check release builds: disabled

---

## 👥 Development Team
Project: 2447216_Appathon_IronWalk

---

## 📄 License
This project is developed as part of the Appathon competition.

---

## 🆘 Support
For technical support or emergency assistance:
- Emergency Helpline: 100 (Police)
- Women's Helpline: 1091
- Emergency Services: 112

---

## 🔮 Future Enhancements
- Real-time contact notification system
- AI-powered threat detection
- Community safety reporting
- Multilingual support
- Wearable device integration
- Voice-activated SOS
- Offline mode with cached maps

---

**Built with ❤️ for Women's Safety**

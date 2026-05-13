ಶಿಲ್ಪ-ಕಲಾ · Shilpa-Kala
<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Min%20SDK-API%2024-brightgreen?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge"/>
</p>
<p align="center">
  <b>A Digital Portfolio Assistant for Karnataka's Traditional Artisans</b><br/>
  Empowering wood carvers and Gombe makers of Channapatna to professionally brand and market their handcrafted products.
</p>

📖 About The Project
Wood carvers and Gombe makers in places like Channapatna and Kinnala, Karnataka produce world-class handcrafted goods. However, their online presence is limited to low-quality WhatsApp photos that fail to communicate the true value of their craftsmanship — making premium products look cheap to city buyers.
Shilpa-Kala solves this by giving artisans a simple, guided tool to:

📸 Capture professional product photos with a guided frame overlay
🎨 Automatically apply a "Handmade in Karnataka" heritage brand label
🏷️ Add artisan name, wood type, and price as a styled overlay
📤 Share catalog-ready branded photos directly to WhatsApp & Facebook


✨ Features
FeatureDescription🎯 Guided CameraGold-framed overlay guides artisans to perfectly position their product🏺 Heritage BrandingAutomatic "Handmade in Karnataka" badge applied to every photo🏷️ Product LabelingArtisan name, wood type, and price overlaid in a luxury style🖼️ Live PreviewReview the branded photo before saving💾 Local GalleryAll branded photos saved in a dedicated ShilpaKala folder📤 One-tap SharingShare directly to WhatsApp, Facebook, Instagram, and more🌟 Splash ScreenAnimated Kannada-themed splash screen with cultural identity

🛠️ Tech Stack
LayerTechnologyLanguageKotlinUI FrameworkJetpack ComposeCameraCameraX (camera-core, camera2, camera-lifecycle, camera-view)Image ProcessingAndroid Bitmap & Canvas APINavigationJetpack Navigation ComposeImage LoadingCoil ComposeFile SharingFileProvider + Android Share IntentStorageMediaStore / External Files DirectoryPermissionsAccompanist PermissionsMin SDKAPI 24 (Android 7.0)Target SDKAPI 34 (Android 14)

## 📁 Project Structure

| File | Purpose |
|---|---|
| `MainActivity.kt` | Entry point, permission handling, navigation |
| `Routes.kt` | Navigation route constants |
| `BrandingEngine.kt` | Bitmap overlay logic — heritage label, price tag, border |
| `ImageSaver.kt` | Save and load branded photos from local storage |
| `SplashScreen.kt` | Animated Kannada-themed splash screen |
| `CameraScreen.kt` | CameraX preview + guide overlay + artisan input fields |
| `PreviewScreen.kt` | Branded photo preview + save and share |
| `GalleryScreen.kt` | Grid gallery of all saved branded photos |
| `Color.kt` | Maroon and gold color palette |
| `Theme.kt` | Material3 dark theme |
| `Typography.kt` | Serif and sans-serif type scale |

🚀 Getting Started
Prerequisites

Android Studio Hedgehog or later
Android device or emulator running API 24+
JDK 8 or higher

Installation

Clone the repository

bash   git clone https://github.com/Sai004437/Shilpa-Kala.git

Open in Android Studio

Launch Android Studio
Click Open and select the cloned folder


Let Gradle sync

Wait for Android Studio to download all dependencies automatically


Run the app

Connect your Android phone via USB with USB Debugging enabled
Click the ▶ Run button




📱 App Flow
Splash Screen  →  Camera Screen  →  Preview Screen  →  Gallery
     ↓                  ↓                  ↓
Kannada logo      Guided frame        Branded photo
 animation      + artisan inputs      + save & share

🎯 Impact Goals

🇮🇳 Brand India — Enhancing the perceived value of Indian handicrafts globally
📱 Digital Literacy — Teaching artisans the power of visual branding
💰 Economic Empowerment — Enabling artisans to command premium pricing independently
🏺 Cultural Preservation — Celebrating Karnataka's GI-tagged craft heritage digitally


👨‍💻 Developer
Sai Krishna B
VTU · MindMatrix Internship Program · Android App Development using GenAI
Project 10 — Shilpa-Kala (Self-Employment)

📄 License
This project is licensed under the MIT License.

<p align="center">
  Made with ❤️ for the artisans of Karnataka 🪵
</p>

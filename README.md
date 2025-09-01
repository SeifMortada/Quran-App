# 🕌 Quran App - Islamic Companion for Android

<div align="center">

![Quran App Logo](https://img.shields.io/badge/Quran%20App-Islamic%20Companion-green?style=for-the-badge&logo=android)

[![Android](https://img.shields.io/badge/Android-Clean%20Architecture-brightgreen?style=flat&logo=android)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.20-blue?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-orange?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)](LICENSE)

*A modern, feature-rich Islamic companion app built with the latest Android technologies*

[Features](#-features) • [Screenshots](#-screenshots) • [Architecture](#-architecture) • [Installation](#-installation) • [Contributing](#-contributing)

</div>

## 📱 About

The **Quran App** is a comprehensive Islamic companion application designed to provide Muslims with
easy access to the Holy Quran, beautiful recitations, and spiritual guidance. Built using modern
Android development practices with Clean Architecture and Jetpack Compose.

### ✨ Key Highlights

- 🎨 **Modern UI** - Built with Jetpack Compose and Material3 design
- 🏗️ **Clean Architecture** - Scalable, maintainable, and testable codebase
- 🎵 **High-Quality Audio** - Beautiful Quran recitations from renowned reciters
- 📱 **Offline-First** - Full functionality without internet connection
- 🌙 **Islamic Design** - Beautiful Arabic typography and Islamic aesthetics
- 🔄 **Real-time Sync** - Seamless data synchronization across devices

## 🚀 Features

### 📖 Quran Reading

- **Complete Quran**: All 114 chapters (Surahs) with accurate Arabic text
- **Beautiful Typography**: Elegant Arabic font rendering optimized for readability
- **Chapter Navigation**: Smooth navigation between Surahs with quick access
- **Offline Reading**: Complete offline access to all Quranic text
- **Search Functionality**: Find specific verses or topics quickly

### 🎵 Audio Recitations

- **Multiple Reciters**: Library of renowned Quran reciters from around the world
- **High-Quality Audio**: Crystal clear audio streaming and downloading
- **Background Playback**: Continue listening while using other apps
- **Download Manager**: Intelligent background downloading with progress tracking
- **Playback Controls**: Play, pause, seek, fast-forward, and rewind
- **Offline Playback**: Listen to downloaded recitations without internet

### ⚙️ Smart Settings

- **Theme Options**: Light, Dark, and System theme adaptation
- **Language Support**: Arabic and English with RTL layout support
- **Audio Preferences**: Customize reciter selection and playback settings
- **Storage Management**: Manage downloaded content and app storage
- **Notification Controls**: Customize notification preferences

### 🤲 Zikr & Remembrance

- **Islamic Supplications**: Collection of authentic Islamic prayers (Dhikr)
- **Daily Remembrances**: Morning and evening supplications
- **Categorized Content**: Organized by occasions, times, and purposes
- **Arabic & Translation**: Original Arabic text with English translations

## 📸 Screenshots

<div align="center">

| Home Screen                   | Quran Reading                   | Audio Player                    | Settings                              |
|-------------------------------|---------------------------------|---------------------------------|---------------------------------------|
| ![Home](screenshots/home.png) | ![Quran](screenshots/quran.png) | ![Audio](screenshots/audio.png) | ![Settings](screenshots/settings.png) |

*Clean, intuitive interface designed for the best user experience*

</div>

## 🏗️ Architecture

This project follows **Clean Architecture** principles with **MVVM** pattern, ensuring:

### 🎯 Architecture Principles

- **Separation of Concerns**: Clear layer boundaries with single responsibilities
- **Dependency Inversion**: Dependencies point inward toward business logic
- **Testability**: Each layer can be independently tested
- **Scalability**: Easy to add new features without breaking existing code

### 📦 Module Structure

```
├── 📱 app/                    # Main application module
├── 🏗️ core/                   # Core shared modules
│   ├── 💾 data/               # Data layer implementations
│   ├── 🎯 domain/             # Business logic & entities
│   ├── 🔗 di/                 # Dependency injection
│   ├── 🎵 service/            # Background services
│   └── 🎨 ui/                 # UI components & theming
└── ✨ features/               # Feature modules
    ├── 📖 quran/              # Quran reading feature
    ├── 🎵 reciter/            # Audio recitation feature  
    ├── ⚙️ settings/           # App settings feature
    └── 🤲 zikr/               # Islamic remembrance feature
```

### 🔧 Technology Stack

#### **Core Technologies**

- ![Kotlin](https://img.shields.io/badge/Kotlin-2.0.20-blue?style=flat&logo=kotlin) **Language**
- ![Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-orange?style=flat) **UI Framework
  **
- ![Architecture](https://img.shields.io/badge/Clean%20Architecture-MVVM-green?style=flat) **Pattern
  **
- ![DI](https://img.shields.io/badge/Koin-3.2.2-red?style=flat) **Dependency Injection**

#### **Android Stack**

- **Navigation**: Compose Navigation with type-safe routing
- **Audio**: Media3 ExoPlayer for high-quality audio playback
- **Storage**: Room database + DataStore for preferences
- **Network**: Retrofit + OkHttp for API communication
- **Background**: Foreground services for downloads and audio

#### **Development Tools**

- **Build**: Gradle with Kotlin DSL and version catalogs
- **Code Generation**: KSP for compile-time processing
- **Testing**: JUnit, Mockk, and Compose testing tools
- **CI/CD**: GitHub Actions for automated building and testing
---

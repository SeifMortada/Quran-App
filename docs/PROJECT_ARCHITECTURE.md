# Quran App - Project Architecture Documentation

## 📋 Overview

This document provides a comprehensive understanding of the Quran Android app's architecture,
structure, and technical implementation for AI models and developers.

## 🏗️ Architecture Pattern

- **Clean Architecture** with **MVVM** pattern
- **Multi-module structure** for scalability and maintainability
- **Dependency Injection** using Koin
- **Reactive Programming** with Kotlin Coroutines and Flow

## 📁 Project Structure

```
Quran-App/
├── app/                           # Main application module
│   ├── src/main/java/.../
│   │   ├── app/                   # Application class and global config
│   │   ├── core/                  # Core app components
│   │   │   ├── navigation/        # App-level navigation
│   │   │   ├── service/           # App-specific services
│   │   │   └── ui/                # App-specific UI components
│   │   ├── data/                  # App-level data implementations
│   │   │   └── repository/        # Repository implementations
│   │   ├── di/                    # Dependency injection modules
│   │   │   └── koin/              # Koin modules
│   │   ├── features/              # App-level features
│   │   │   ├── home/              # Home screen components
│   │   │   └── splash/            # Splash screen
│   │   ├── service/               # System services
│   │   │   └── DownloadService.kt # Background download service
│   │   └── utils/                 # Utility functions and components
│   └── build.gradle.kts           # App module dependencies
│
├── core/                          # Core modules (shared across features)
│   ├── data/                      # Data layer implementations
│   │   ├── database/              # Room database
│   │   ├── remote/                # Network implementations
│   │   └── repository/            # Repository implementations
│   ├── domain/                    # Domain layer (business logic)
│   │   ├── entity/                # Domain entities
│   │   ├── repository/            # Repository interfaces
│   │   └── usecase/               # Use cases (business logic)
│   ├── di/                        # Dependency injection
│   │   └── koin modules           # Core DI modules
│   ├── service/                   # Background services
│   │   ├── AudioPlayerService.kt  # Audio playback service
│   │   ├── DownloadHelper.kt      # Download management
│   │   └── ServiceConstants.kt    # Service constants
│   └── ui/                        # UI components and utilities
│       ├── components/            # Reusable UI components
│       ├── permissions/           # Permission management
│       ├── theme/                 # App theming
│       └── locale/                # Localization
│
└── features/                      # Feature modules (presentation layer)
    ├── quran/                     # Quran reading feature
    │   ├── chapters/              # Quran chapters (Surahs)
    │   ├── navigation/            # Feature navigation
    │   └── chapter/               # Individual chapter view
    ├── reciter/                   # Audio recitation feature
    │   ├── all/                   # All reciters list
    │   ├── recitation/            # Recitation playback
    │   └── navigation/            # Feature navigation
    ├── settings/                  # App settings feature
    │   ├── SettingsScreen.kt      # Settings UI
    │   ├── SettingsViewModel.kt   # Settings logic
    │   └── navigation/            # Feature navigation
    └── zikr/                      # Islamic remembrance feature
        ├── ZikrScreen.kt          # Zikr UI
        └── navigation/            # Feature navigation
```

## 🏛️ Clean Architecture Layers

### 1. **Presentation Layer** (`features/` + `app/`)

- **Features**: Independent feature modules with their own UI and ViewModels
- **Navigation**: Type-safe navigation using Jetpack Compose Navigation with Kotlin Serialization
- **UI State Management**: MVVM pattern with StateFlow and Compose State
- **Dependency Injection**: Koin ViewModels with automatic injection

### 2. **Domain Layer** (`core/domain/`)

- **Entities**: Pure Kotlin data classes representing business objects
- **Use Cases**: Business logic implementation with single responsibility
- **Repository Interfaces**: Contracts for data access
- **No Android Dependencies**: Pure Kotlin module

### 3. **Data Layer** (`core/data/`)

- **Repository Implementations**: Concrete implementations of domain repositories
- **Data Sources**: Remote (API) and Local (Room database)
- **Network**: Retrofit with OkHttp for API communication
- **Database**: Room for local data persistence

## 🔧 Technology Stack

### **Core Technologies**

- **Language**: Kotlin 2.0.20
- **UI Framework**: Jetpack Compose with Material3
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Koin 3.2.2
- **Async Programming**: Coroutines + Flow

### **Android Components**

- **Navigation**: Compose Navigation with Kotlin Serialization
- **Permissions**: Runtime permission management
- **Services**: Foreground services for audio and downloads
- **Notifications**: Android 13+ compatible notification system
- **Storage**: DataStore for preferences, Room for complex data

### **Network & Data**

- **HTTP Client**: Retrofit 2.9.0 + OkHttp 4.12.0
- **Serialization**: Gson for JSON parsing
- **Database**: Room 2.6.0 with KTX extensions
- **Caching**: In-memory and disk caching strategies

### **Media & Audio**

- **Audio Playback**: Media3 ExoPlayer 1.5.1
- **Background Audio**: MediaBrowserService for background playback
- **Audio Controls**: Media session and notification controls

### **Build & Development**

- **Build System**: Gradle 8.6.0 with Kotlin DSL
- **Code Generation**: KSP 2.0.20-1.0.24
- **Modularization**: Multi-module architecture
- **Version Catalog**: Centralized dependency management

## 📱 Features Overview

### **1. Quran Reading** (`features:quran`)

- **Chapters List**: Display all 114 Quran chapters (Surahs)
- **Chapter Reading**: View individual chapters with Arabic text
- **Navigation**: Smooth navigation between chapters
- **Offline Support**: Local storage of Quran text

### **2. Audio Recitation** (`features:reciter`)

- **Reciters Library**: Multiple renowned Quran reciters
- **Audio Playback**: High-quality audio streaming and offline playback
- **Download Management**: Background download with progress tracking
- **Playback Controls**: Play, pause, seek, fast-forward, rewind
- **Background Play**: Continue playback when app is backgrounded

### **3. Settings** (`features:settings`)

- **Theme Management**: Light, Dark, System theme options
- **Language Support**: Arabic and English localization
- **Audio Preferences**: Reciter selection, playback settings
- **Storage Management**: Downloaded content management

### **4. Zikr (Dhikr)** (`features:zikr`)

- **Islamic Remembrances**: Collection of authentic Islamic supplications
- **Categories**: Organized by different occasions and times
- **Arabic & Translation**: Original Arabic with English translations

## 🔄 Data Flow Architecture

### **Repository Pattern Implementation**

```kotlin
// Domain Layer - Interface
interface QuranRepository {
    suspend fun getSurahs(): Flow<List<Surah>>
    suspend fun getSurahById(id: Int): Surah?
}

// Data Layer - Implementation  
class QuranRepositoryImpl(
    private val remoteDataSource: QuranApiService,
    private val localDataSource: QuranDao
) : QuranRepository {
    override suspend fun getSurahs(): Flow<List<Surah>> = 
        localDataSource.getAllSurahs()
}

// Presentation Layer - ViewModel
class QuranViewModel(
    private val getSurahsUseCase: GetSurahsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        loadSurahs()
    }
}
```

### **Use Case Pattern**

```kotlin
class GetSurahsUseCase(
    private val repository: QuranRepository
) {
    suspend operator fun invoke(): Flow<List<Surah>> = 
        repository.getSurahs()
}
```

## 📡 Service Architecture

### **1. Audio Player Service** (`core:service`)

- **Background Playback**: Foreground service for audio playback
- **Media Session**: Integration with system media controls
- **Notification Controls**: Play/pause/skip controls in notification
- **State Management**: Reactive state updates via Flow

### **2. Download Service** (`app/service`)

- **Background Downloads**: Foreground service for file downloads
- **Progress Tracking**: Real-time download progress updates
- **Notification System**: Android 13+ compatible notifications
- **Error Handling**: Robust error handling and retry mechanisms

## 🎨 UI Architecture

### **Compose-First Design**

- **Material3 Design System**: Modern Material Design implementation
- **Adaptive Theming**: Light/Dark/System theme support
- **Responsive Layouts**: Optimized for different screen sizes
- **Custom Components**: Reusable UI components in `core:ui`

### **Navigation Architecture**

- **Type-Safe Navigation**: Kotlin Serialization for navigation arguments
- **Modular Navigation**: Feature-specific navigation graphs
- **Deep Linking**: Support for navigation via deep links

### **State Management**

- **Unidirectional Data Flow**: State flows down, events flow up
- **Reactive UI**: Compose state automatically updates on data changes
- **Error Handling**: Centralized error state management

## 🔐 Permission Management

### **Runtime Permissions**

- **Storage Permissions**: For downloading and caching audio files
- **Notification Permissions**: For background service notifications (Android 13+)
- **Permission Dialogs**: User-friendly permission request flow

## 🌐 Localization Support

### **Multi-Language Support**

- **Arabic**: Primary language for Islamic content
- **English**: Secondary language for UI and translations
- **RTL Support**: Right-to-left layout support for Arabic
- **Dynamic Locale**: Runtime language switching

## 🏗️ Module Dependencies

### **Dependency Graph**

```
app
├── features:quran
├── features:reciter  
├── features:settings
├── features:zikr
├── core:ui
├── core:domain
├── core:data
├── core:service
└── core:di

features:* modules depend on:
├── core:ui
├── core:domain  
└── core:di

core modules dependency order:
core:domain (no dependencies)
├── core:data (depends on domain)
├── core:ui (depends on domain)
├── core:service (depends on domain, ui)
└── core:di (depends on all core modules)
```

## 🚀 Build Configuration

### **Gradle Configuration**

- **Version Catalog**: Centralized dependency management in `gradle/libs.versions.toml`
- **Kotlin DSL**: All build files use Kotlin DSL
- **Multi-Module Setup**: Independent feature modules for scalability
- **Compose Compiler**: Kotlin Compose Compiler 2.0.20

### **Module Types**

- **Application Module**: `app` - Main Android application
- **Android Library**: All `core` and `features` modules
- **Pure Kotlin**: `core:domain` for business logic

## 🔍 Key Implementation Details

### **Download System**

- **Channel Management**: Smart notification channel handling for Android 13+
- **Service Architecture**: Robust foreground service with proper lifecycle
- **Error Recovery**: Comprehensive error handling and user feedback
- **Progress Tracking**: Real-time progress updates via broadcasts

### **Audio System**

- **Media3 Integration**: Modern Android media framework
- **Background Playback**: Proper MediaBrowserService implementation
- **State Synchronization**: Reactive state management across UI and service

### **Data Persistence**

- **Room Database**: For complex relational data (Quran text, metadata)
- **DataStore**: For simple key-value preferences (settings, theme)
- **File System**: For downloaded audio files with proper organization

## 🎯 Architecture Benefits

### **Scalability**

- **Modular Architecture**: Easy to add new features without affecting existing code
- **Clean Separation**: Clear boundaries between layers and responsibilities
- **Independent Development**: Teams can work on different modules simultaneously

### **Maintainability**

- **Single Responsibility**: Each module has a clear, focused purpose
- **Dependency Direction**: Dependencies flow inward toward domain
- **Testability**: Each layer can be tested independently

### **Performance**

- **Lazy Loading**: Modules loaded only when needed
- **Efficient Navigation**: Compose navigation with proper state management
- **Background Processing**: Proper use of services for long-running tasks

This architecture ensures a robust, scalable, and maintainable Quran app that follows Android
development best practices and modern architectural patterns.
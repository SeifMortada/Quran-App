# Quran App

This Quran app is a work in progress, built using a modern architecture to ensure scalability, maintainability, and testability. The app is designed to provide a smooth and seamless experience for reading, listening, and exploring the Quran, with an emphasis on clean architecture principles and modularity.


## Description

An in-depth paragraph about your project and overview of use.

## Features

Display Surahs and Ayahs in a RecyclerView
Listen to Quranic recitations by various reciters with various telawahs 
Share verses via social media or messaging platforms
Search for specific Surahs, Ayahs, or verses
Save progress and bookmark verses
Offline reading support with locally stored Quran data



### Architecture

This app follows a multi-modular architecture, making it easier to manage and scale as the project grows. The architecture is based on the Clean Architecture pattern and uses MVVM (Model-View-ViewModel) for separating concerns and ensuring a clear flow of data and UI updates. The app is divided into multiple modules for better maintainability.


### Key Components:

* ViewModel: Handles UI-related data and communicates with the Repository layer to fetch or update data.
* Repository: Acts as a single source of truth and abstracts data fetching from multiple sources like APIs, databases, or assets.
* Room Database: Used for local storage of Quran data, bookmarks, and user preferences.
* Retrofit: For making API calls to fetch Quranic content and recitation data.
* Koin: A lightweight dependency injection framework to manage dependencies across the app.

### Technologies Used:
 
* Kotlin: The primary language for development
* Clean Architecture: For a well-structured and scalable codebase
* MVVM Desgin pattern: For managing the ui states in the ui layer 
* Koin: Dependency Injection
* Retrofit: For making network requests
* Room Database: For local data storage
* LiveData & ViewModel: For managing UI-related data lifecycle-aware


## Current Status

This app is still a work in progress, with several features in development. The following modules and components have been implemented:

* Quran data retrieval from an API (including audio links for recitation)
* Display of Surahs and Ayahs in a list
* Quran recitations with playback functionality
* Modular architecture setup with clean separation of concerns

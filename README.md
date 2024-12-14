# **Quran App**

**Welcome to the Quran App!**  
This app is a **work in progress** designed to provide a smooth, seamless experience for reading, listening, and exploring the Quran. Built using **modern architecture** principles, this app ensures **scalability**, **maintainability**, and **testability**. With a focus on clean architecture and modularity, the app is crafted for a responsive and user-friendly experience.

---

## **Features** 🌟

- **Display Surahs and Ayahs** in a **RecyclerView**
- **Listen to Quranic recitations** by various **reciters** with multiple **telawahs**
- **Share verses** via **social media** or **messaging platforms**
- **Search** for specific **Surahs**, **Ayahs**, or **verses**
- **Save progress** and **bookmark verses**
- **Offline reading** support with locally stored Quran data

---

### **Architecture** 🏗️

This app follows a **multi-modular architecture**, which allows for **easy management** and **scalability** as the project evolves. It is based on the **Clean Architecture** pattern and uses the **MVVM** (Model-View-ViewModel) design to separate concerns and ensure a **clear flow** of data and **UI updates**.

The project is divided into **multiple modules**, promoting **better maintainability** and a **cleaner structure**.

---

### **Key Components** ⚙️

- **ViewModel**: Manages UI-related data and communicates with the **Repository layer** to fetch or update data.
- **Repository**: Acts as a **single source of truth**, abstracting data fetching from multiple sources like **APIs**, **databases**, or **assets**.
- **Room Database**: For **local storage** of Quran data, bookmarks, and user preferences.
- **Retrofit**: For making **API calls** to fetch Quranic content and recitation data.
- **Koin**: A **lightweight dependency injection** framework for managing dependencies throughout the app.

---

### **Technologies Used** 💻

- **Kotlin**: The primary language for development
- **UI**: XML+viewBinding & Compose
- **Clean Architecture**: To create a well-structured and scalable codebase
- **MVVM Design Pattern**: To manage UI states in the UI layer effectively
- **Koin**: Dependency Injection to simplify dependency management
- **Retrofit**: For making network requests to fetch Quranic content
- **Room Database**: For efficient local data storage
- **LiveData & ViewModel**: For managing lifecycle-aware UI-related data

---

## **Current Status** 🚧

This app is still under **active development**, with several features in progress. Below are the implemented modules and components:

- **Quran data retrieval** from an API (including audio links for recitation)
- **Display of Surahs and Ayahs** in a list
- **Quran recitations** with playback functionality
- **Modular architecture setup** ensuring clean separation of concerns

---

## **Planned Features** 🔮

- Full support for **offline reading**
- **Search** functionality for verses and chapters
- **User preferences** for themes, recitations, and display settings
- **Bookmarking** and **note-taking** capabilities
- Advanced **sharing** options for verses

---

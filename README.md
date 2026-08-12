# Recipeo

Recipeo is a modern, professional Android application designed for browsing, searching, and managing food recipes. Built with Kotlin and Jetpack Compose, it follows **Clean Architecture** and the **MVI (Model-View-Intent)** pattern. One of its core missions is to provide a reliable **offline-first experience**, ensuring users can access their favorite meals and previously viewed recipes even without an active internet connection.

## 1. Project Overview

Recipeo provides a seamless user journey focused on discoverability and persistence:

*   **Splash Screen**: Automatically checks for an existing user session to route the user.
*   **Authentication**: Secure Sign In and Sign Up screens with field validation.
*   **Home**: Explore recipes by categories (e.g., Beef, Seafood, Dessert) or browse all available recipes.
*   **Search**: Real-time recipe search by name with local fallback.
*   **Recipe Details**: View comprehensive instructions, origin area, and watch cooking videos on YouTube.
*   **Favorites**: Save recipes locally for quick access, fully functional offline.

## 2. Main Features

### Authentication
*   **Sign In & Sign Up**: Full authentication flow.
*   **Persistent Session**: Users stay logged in across app restarts using **Jetpack DataStore**.

### Recipe Browsing
*   **Dynamic Categories**: Filter recipes by category using a horizontal navigation bar.
*   **Powerful Search**: Find recipes by name with integrated debouncing for performance.

### Recipe Details
*   **Rich Content**: Complete preparation instructions and YouTube integration.
*   **Favorite Management**: Toggle favorite status directly from the details screen.

### Offline Support
*   **Smart Caching**: API responses are automatically stored in a **Room** database.
*   **Network Awareness**: The app detects connectivity states and seamlessly switches to local data.
*   **Offline Search & Details**: Access previously cached search results and full recipe details without internet.

### Error Handling
*   **Graceful Fallbacks**: Distinct handling for no internet, timeouts, and server errors.
*   **User-Friendly Messages**: Technical exceptions are mapped to clear, actionable domain-level errors.

## 3. Tech Stack

| Technology | Purpose |
| --- | --- |
| **Kotlin** | Main programming language |
| **Jetpack Compose** | Modern Declarative UI |
| **MVI** | Unidirectional State Management |
| **Clean Architecture** | Decoupled and Testable project structure |
| **Retrofit** | Type-safe REST API communication |
| **OkHttp** | HTTP client with logging capabilities |
| **Room** | Local SQLite database abstraction |
| **Hilt** | Standard Dependency Injection |
| **Coroutines & Flow** | Asynchronous and Reactive programming |
| **DataStore** | Persistent user preferences |
| **Navigation Compose** | In-app navigation |
| **TheMealDB API** | External recipe data source |

## 4. Architecture

Recipeo is organized into four main modules following Clean Architecture principles:

### `:presentation`
Contains the UI layer, including **Compose Screens**, **ViewModels**, **UiStates**, and **Events**. It observes state changes and sends user actions to the domain layer.

### `:domain`
The core business logic containing **Domain Models**, **Repository Interfaces**, **Use Cases**, and **AppErrors**. It is a pure Kotlin module independent of Android frameworks.

### `:data`
Handles data operations. It includes **Repository Implementations**, **Retrofit Services**, **Room Entities/DAOs**, **Mappers**, and **Local/Remote Data Sources**.

### `:app`
The glue module responsible for **Hilt initialization**, **App Navigation setup**, and the **MainActivity**.

**Dependency Flow**:
`UI → ViewModel → UseCase → Repository Interface (Domain) ← Repository Implementation (Data) → Remote/Local Source`

## 5. Architecture Diagram

```mermaid
flowchart TD
    App[App Module - Hilt/Nav] --> Presentation
    Presentation --> Domain
    Data --> Domain
    
    subgraph Presentation
        UI[Compose UI] --> VM[ViewModel]
        VM --> State[UiState]
    end
    
    subgraph Domain
        UC[Use Cases]
        RI[Repository Interface]
        Models[Domain Models]
    end
    
    subgraph Data
        RImpl[Repository Implementation]
        Remote[Retrofit / API Service]
        Local[Room DB / DAOs]
        DS[DataStore]
    end
    
    VM --> UC
    UC --> RI
    RI <|-- RImpl
    RImpl --> Remote
    RImpl --> Local
    RImpl --> DS
```

## 6. MVI Architecture

Recipeo uses a unidirectional data flow to ensure the UI is predictable and easy to debug:

1.  **User Action**: The user interacts with the UI (e.g., types in search).
2.  **Event**: The UI sends an `Event` (e.g., `SearchQueryChanged`) to the ViewModel.
3.  **ViewModel**: Processes the event, cancels any stale operations (like previous searches), and calls the appropriate Use Case.
4.  **UiState**: The ViewModel updates a single `StateFlow<UiState>` containing loading states, data, and errors.
5.  **UI**: The Compose screen observes the `UiState` and re-renders accordingly.

## 7. Offline-First & Caching

The application prioritizes a smooth experience regardless of network quality.

### Smart Caching Strategy
*   **Online**: The app fetches data from the API, updates the Room database, and presents the fresh data.
*   **Offline**: The Repository detects the lack of connection and immediately pulls the latest data from the Room database.
*   **API Failure**: If the API call fails due to a timeout or server error, the app falls back to the local cache. If no cache is available, a `NoCacheAvailable` error is shown.

### Data Integrity (`mergeWithCache`)
TheMealDB list APIs often return partial data. To prevent losing full recipe details (like instructions) when browsing a list, Recipeo implements a `mergeWithCache` function. This ensures that when new data arrives from a list API, any existing rich details in the local database are preserved.

## 8. Room Database

*   **`CachedMealEntity`**: Stores recipe data including `cachedAt` timestamps for ordering and full details like instructions and YouTube URLs.
*   **`FavoriteMealEntity`**: A dedicated table for IDs and basic metadata of favorite recipes.
*   **Relationships**: Uses Room's `@Relation` (`FavoriteMealWithCached`) to join favorites with their full cached details, allowing the Favorites screen to show detailed information even offline.

## 9. Authentication Flow

Recipeo persists the login state using **Jetpack DataStore**:

1.  **App Start**: `SplashViewModel` triggers a session check.
2.  **Session Verification**: The `UserPreferencesRepository` reads the `is_logged_in` flag from DataStore.
3.  **Navigation**: If `true`, the user is routed to `Home`; otherwise, they are sent to the `Login` screen.

## 10. Navigation

Navigation is defined in `AppNavigation.kt` using the following routes:
*   `splash`: Initial branding and session check.
*   `login` / `signup`: Authentication screens.
*   `home`: The main recipe list and category browser.
*   `favorites`: A personalized list of saved recipes.
*   `details/{mealId}`: Detailed view of a specific recipe.

## 11. How to Run

1.  **Clone the repository**: `git clone https://github.com/user/recipeo.git`
2.  **Open in Android Studio**: Use Android Studio Ladybug (2024.2.1) or newer.
3.  **Sync Gradle**: Allow the IDE to download dependencies and sync the project structure.
4.  **Run**: Click the "Run" button to deploy the `:app` module to an emulator or physical device.

## 12. Screenshots

| Splash | Sign In | Home |
| --- | --- | --- |
| ![Splash](screenshots/splash.png) | ![Sign In](screenshots/sign_in.png) | ![Home](screenshots/home.png) |

| Search | Details | Favorites |
| --- | --- | --- |
| ![Search](screenshots/search.png) | ![Details](screenshots/details.png) | ![Favorites](screenshots/favorites.png) |

*(Note: Images above are placeholders)*

## 13. APK & GitHub

*   **APK**: [Download Recipeo APK](apk/recipeo-app.apk) *(Placeholder)*
*   **Repository**: [Recipeo GitHub](https://github.com/user/recipeo) *(Placeholder)*

---

## 14. Presentation Guide (5-10 Minutes)

### 1. Project Overview (1m)
*   **Show**: Splash and Home Screen.
*   **Say**: "Recipeo is an offline-first recipe app built with Compose. It solves the problem of unreliable internet in the kitchen by ensuring that every recipe you browse is instantly cached for offline use."

### 2. Architecture & Tech Stack (2m)
*   **Show**: Architecture Diagram.
*   **Say**: "I implemented Clean Architecture with four modules to separate concerns. I used MVI for state management, Hilt for dependency injection, and Room combined with Retrofit for the data layer."

### 3. MVI & Flow (1.5m)
*   **Show**: `RecipesListViewModel.kt`.
*   **Say**: "I used a unidirectional data flow. The UI sends events, the ViewModel handles business logic and debouncing, and updates a single UI state. This makes the app's behavior predictable and easy to test."

### 4. Offline Caching Demo (2m)
*   **Show**: Browse recipes, then enable Flight Mode and show Details/Favorites.
*   **Say**: "The core feature is the `executeWithCacheFallback` logic. When the API fails or the network is gone, we fallback to Room. I also implemented a `mergeWithCache` function to ensure that partial list updates don't overwrite full recipe details."

### 5. Conclusion (30s)
*   **Say**: "Recipeo demonstrates modern Android best practices, focusing on performance, persistence, and a clean user experience."

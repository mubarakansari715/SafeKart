# SafeKart App Architecture

## Overview
This document describes the modern architecture and structure of the SafeKart Android application.

## Architecture Pattern
The app follows **Clean Architecture** with **MVVM (Model-View-ViewModel)** pattern:

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (UI/Compose Screens, ViewModels)      │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│            Domain Layer                  │
│  (Use Cases, Repository Interfaces)     │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│             Data Layer                  │
│  (Repository Impl, Data Sources, API)   │
└─────────────────────────────────────────┘
```

## Project Structure

```
com.safekart.safekart/
├── data/                    # Data Layer
│   ├── model/              # Data models (API responses)
│   ├── remote/             # Remote data sources (API, Network)
│   │   ├── api/            # Retrofit API services
│   │   ├── auth/           # Auth data source
│   │   └── user/           # User data source
│   └── repository/         # Repository implementations
│
├── domain/                  # Domain Layer (Business Logic)
│   ├── repository/         # Repository interfaces
│   └── usecase/            # Use cases (Business logic)
│       ├── auth/           # Authentication use cases
│       └── user/           # User use cases
│
├── ui/                      # Presentation Layer
│   ├── components/         # Reusable UI components
│   ├── presentation/       # Screens and ViewModels
│   │   ├── auth/          # Auth screens
│   │   ├── home/          # Home screen
│   │   ├── search/        # Search screen
│   │   ├── profile/       # Profile screen
│   │   ├── cart/          # Cart screen
│   │   └── splash/        # Splash screen
│   └── theme/             # Theme, Colors, Typography
│
├── navigation/             # Navigation setup
├── di/                     # Dependency Injection (Hilt)
├── util/                   # Utilities (Constants, ErrorHandler)
└── MainActivity.kt         # Main entry point
```

## Key Architectural Principles

### 1. **Separation of Concerns**
- **Data Layer**: Handles API calls, local storage, data transformation
- **Domain Layer**: Contains business logic, use cases, repository interfaces
- **Presentation Layer**: UI components, ViewModels, state management

### 2. **Dependency Injection**
- Uses **Hilt** for dependency injection
- All dependencies are injected through constructors
- ViewModels are scoped to navigation graph

### 3. **State Management**
- **StateFlow** for reactive state management
- **UiState** data classes for screen states
- Unidirectional data flow (UI → ViewModel → UseCase → Repository)

### 4. **Error Handling**
- Centralized `ErrorHandler` utility
- Result-based error handling (Result<T>)
- User-friendly error messages

### 5. **Modern Android Practices**
- **Jetpack Compose** for UI
- **Material3** design system
- **Navigation Compose** for navigation
- **Coroutines & Flow** for async operations
- **Retrofit** for networking
- **Edge-to-Edge** design

## Data Flow

```
User Action
    ↓
UI (Composable)
    ↓
ViewModel (handles UI logic)
    ↓
UseCase (business logic)
    ↓
Repository (data access)
    ↓
Data Source (API/Local)
    ↓
Response flows back through layers
    ↓
UI updates reactively
```

## Best Practices Implemented

✅ **Clean Architecture** - Clear separation of layers
✅ **MVVM Pattern** - ViewModels manage UI state
✅ **Dependency Injection** - Hilt for DI
✅ **State Management** - StateFlow for reactive updates
✅ **Error Handling** - Centralized error handling
✅ **Resource Management** - Strings, dimensions in resources
✅ **Type Safety** - Kotlin data classes, sealed classes
✅ **Performance** - Cached data, optimized navigation
✅ **Security** - No logging in production builds
✅ **Maintainability** - Well-organized, modular structure

## Navigation Structure

- **Auth Flow**: Splash → Login/Register → Home
- **Main App**: Home, Search, Profile (with bottom navigation)
- **Cart**: Accessible from Home (TopAppBar)

## Future Improvements

- [ ] Add sealed classes for UI states (Loading, Success, Error)
- [ ] Implement type-safe navigation (if needed)
- [ ] Add local database (Room) for offline support
- [ ] Add image loading library (Coil)
- [ ] Implement pull-to-refresh
- [ ] Add analytics and crash reporting

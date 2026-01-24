# Offline Behavior & Crash Prevention

## ✅ **The App Will NOT Crash When Offline**

The app is designed to handle offline scenarios gracefully. Here's why:

## Current Offline Protection

### 1. **Splash Screen (App Launch)**
- ✅ **No Network Calls**: Only checks local cache (`SharedPreferences`) to determine if user is logged in
- ✅ **Safe**: Uses `authRepository.currentUser` which reads from local storage
- ✅ **Result**: App opens successfully even when offline

### 2. **Home Screen**
- ✅ **No Automatic Network Calls**: Currently empty, doesn't fetch data on load
- ✅ **Safe**: Even if `loadUserData()` is called, it has try-catch and fallback to cached data

### 3. **Profile Screen**
- ✅ **Initializes from Cache**: ViewModel loads user data from local cache on initialization
- ✅ **No Automatic API Calls**: Only makes network calls when `refreshUserData()` is explicitly called
- ✅ **Error Handling**: All network calls wrapped in try-catch with fallback to cached data

### 4. **Login/Register Screens**
- ✅ **Proper Error Handling**: All network calls return `Result<T>` with error handling
- ✅ **User-Friendly Messages**: Shows "Network error. Please check your connection" instead of crashing
- ✅ **Try-Catch Blocks**: All exceptions are caught and handled gracefully

### 5. **Network Error Handling**
- ✅ **Centralized Error Handler**: `ErrorHandler.kt` provides consistent error messages
- ✅ **Network Detection**: `NetworkUtils.kt` can check connectivity before making calls
- ✅ **Timeout Protection**: 30-second timeout prevents indefinite hanging
- ✅ **Result Pattern**: All network operations use `Result<T>` for safe error handling

## Network Error Messages

When offline, users will see friendly messages instead of crashes:
- "Network error. Please check your connection"
- "Cannot connect to server. Please check if server is running"
- "Connection timeout. Please try again"

## Code Examples

### ✅ Safe: Splash Screen
```kotlin
// Only checks local cache - no network call
if (viewModel.isUserLoggedIn()) {
    onNavigateToHome()
} else {
    onNavigateToLogin()
}
```

### ✅ Safe: Profile ViewModel
```kotlin
// Initializes from cache - no network call on init
private val _uiState = MutableStateFlow(
    run {
        val currentUser = authRepository.currentUser // Local cache
        ProfileUiState(...)
    }
)
```

### ✅ Safe: Network Calls with Error Handling
```kotlin
suspend fun signInWithEmailAndPassword(...): Result<User> {
    return try {
        // Network call
    } catch (e: Exception) {
        Result.failure(Exception(
            when {
                e.message?.contains("Unable to resolve host") == true -> 
                    "Network error. Please check your connection"
                // ... more error handling
            }
        ))
    }
}
```

## Testing Offline Behavior

To test offline behavior:
1. **Enable Airplane Mode** on your device/emulator
2. **Open the app** - Should open successfully (shows splash, then login/home)
3. **Try to login** - Should show error message, not crash
4. **Navigate to Profile** - Should show cached user data
5. **Try any action requiring network** - Should show error message gracefully

## Future Improvements

- [ ] Add network connectivity monitoring
- [ ] Show offline indicator in UI
- [ ] Implement offline data caching (Room database)
- [ ] Queue actions when offline, sync when online
- [ ] Add retry mechanism for failed network calls

## Conclusion

**The app is safe from crashes when offline** because:
1. ✅ No unhandled exceptions
2. ✅ All network calls wrapped in try-catch
3. ✅ Result-based error handling
4. ✅ Fallback to cached data
5. ✅ User-friendly error messages
6. ✅ No automatic network calls on app launch
